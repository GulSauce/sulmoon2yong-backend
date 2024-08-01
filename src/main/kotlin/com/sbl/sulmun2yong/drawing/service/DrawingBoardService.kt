package com.sbl.sulmun2yong.drawing.service

import com.sbl.sulmun2yong.drawing.adapter.DrawingBoardAdapter
import com.sbl.sulmun2yong.drawing.adapter.DrawingHistoryAdapter
import com.sbl.sulmun2yong.drawing.domain.DrawingBoard
import com.sbl.sulmun2yong.drawing.domain.DrawingHistory
import com.sbl.sulmun2yong.drawing.domain.Reward
import com.sbl.sulmun2yong.drawing.domain.drawingResult.NonWinnerDrawingResult
import com.sbl.sulmun2yong.drawing.domain.drawingResult.WinnerDrawingResult
import com.sbl.sulmun2yong.drawing.dto.response.DrawingBoardResponse
import com.sbl.sulmun2yong.drawing.dto.response.DrawingResultResponse
import com.sbl.sulmun2yong.drawing.dto.response.NonWinnerDrawingResultResponse
import com.sbl.sulmun2yong.drawing.dto.response.WinnerDrawingResultResponse
import com.sbl.sulmun2yong.drawing.exception.AlreadyParticipatedDrawingException
import com.sbl.sulmun2yong.drawing.exception.FinishedDrawingException
import com.sbl.sulmun2yong.drawing.exception.InvalidDrawingException
import com.sbl.sulmun2yong.global.data.PhoneNumber
import com.sbl.sulmun2yong.survey.adapter.ParticipantAdapter
import com.sbl.sulmun2yong.survey.adapter.SurveyAdapter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date
import java.util.UUID

// TODO : mongoDB 트랜잭션 테스트 필요
@Service
class DrawingBoardService(
    private val surveyAdapter: SurveyAdapter,
    private val participantAdapter: ParticipantAdapter,
    private val drawingBoardAdapter: DrawingBoardAdapter,
    private val drawingHistoryAdapter: DrawingHistoryAdapter,
) {
    fun getDrawingBoard(surveyId: UUID): DrawingBoardResponse {
        val drawingBoard = drawingBoardAdapter.getBySurveyId(surveyId)
        return DrawingBoardResponse.of(drawingBoard)
    }

    @Transactional
    fun doDrawing(
        participantId: UUID,
        selectedNumber: Int,
        phoneNumber: String,
    ): DrawingResultResponse {
        // 유효성 검증
        // 참가했는가
        val participant = participantAdapter.getParticipant(participantId)
        // 추첨 기록이 있는가
        val phoneNumberData = PhoneNumber.createWithNonNullable(phoneNumber)
        val drawingHistory = drawingHistoryAdapter.findByParticipantIdOrPhoneNumber(participantId, phoneNumberData)
        if (drawingHistory != null) {
            throw AlreadyParticipatedDrawingException()
        }
        // 설문이 종료되었는가
        val surveyId = participant.surveyId
        val survey = surveyAdapter.getSurvey(surveyId)
        if (Date().after(survey.finishedAt)) {
            throw FinishedDrawingException()
        }

        // 뽑기
        // 추첨 보드 가져오기
        val drawingBoard = drawingBoardAdapter.getBySurveyId(surveyId)
        // 뽑기
        val drawingResult = drawingBoard.getDrawingResult(selectedNumber)

        // 후속 처리
        // 보드 업데이트
        drawingBoardAdapter.save(drawingResult.changedDrawingBoard)
        // 추첨 기록 저장
        drawingHistoryAdapter.save(
            DrawingHistory.create(
                participantId = participantId,
                phoneNumber = phoneNumberData,
                drawingBoardId = drawingBoard.id,
                selectedTicketIndex = selectedNumber,
                ticket = drawingBoard.tickets[selectedNumber],
            ),
        )

        // dto 반환
        val drawingResultResponse =
            when (drawingResult) {
                is WinnerDrawingResult -> WinnerDrawingResultResponse(drawingResult.rewardName)
                is NonWinnerDrawingResult -> NonWinnerDrawingResultResponse()
                else -> throw InvalidDrawingException()
            }
        return drawingResultResponse
    }

    fun makeDrawingBoard(
        surveyId: UUID,
        boardSize: Int,
        surveyRewards: List<Reward>,
    ) {
        // TODO: 적절한 다른 패키지간 도메인 변환 로직 도입
        val rewards =
            surveyRewards
                .map {
                    Reward(
                        name = it.name,
                        category = it.category,
                        count = it.count,
                    )
                }

        val drawingBoard =
            DrawingBoard.create(
                surveyId = surveyId,
                boardSize = boardSize,
                rewards = rewards,
            )
        drawingBoardAdapter.save(drawingBoard)
    }
}
