package com.sbl.sulmun2yong.survey.service

import com.sbl.sulmun2yong.global.fingerprint.FingerprintApi
import com.sbl.sulmun2yong.survey.adapter.ParticipantAdapter
import com.sbl.sulmun2yong.survey.adapter.ResponseAdapter
import com.sbl.sulmun2yong.survey.adapter.SurveyAdapter
import com.sbl.sulmun2yong.survey.domain.Participant
import com.sbl.sulmun2yong.survey.dto.request.SurveyResponseRequest
import com.sbl.sulmun2yong.survey.dto.response.SurveyParticipantResponse
import com.sbl.sulmun2yong.survey.exception.AlreadyParticipatedException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SurveyResponseService(
    val surveyAdapter: SurveyAdapter,
    val participantAdapter: ParticipantAdapter,
    val responseAdapter: ResponseAdapter,
    val fingerprintApi: FingerprintApi,
) {
    // TODO: 트랜잭션 처리 추가하기
    fun responseToSurvey(
        surveyId: UUID,
        surveyResponseRequest: SurveyResponseRequest,
        isAdmin: Boolean,
    ): SurveyParticipantResponse {
        val visitorId =
            if (!isAdmin) {
                // Admin이 아닌 경우 visitorId의 유효성을 검증
                validateIsAlreadyParticipated(surveyId, surveyResponseRequest.visitorId)
                fingerprintApi.validateVisitorId(surveyResponseRequest.visitorId)
                surveyResponseRequest.visitorId
            } else {
                // Admin인 경우 visitorId를 랜덤으로 생성
                UUID.randomUUID().toString()
            }
        val survey = surveyAdapter.getSurvey(surveyId)
        val surveyResponse = surveyResponseRequest.toDomain(surveyId)
        survey.validateResponse(surveyResponse)
        // TODO: 참가자 객체의 UserId에 실제 유저 값 넣기
        val participant = Participant.create(visitorId, surveyId, null)
        participantAdapter.insert(participant)
        responseAdapter.insertSurveyResponse(surveyResponse, participant.id)
        return SurveyParticipantResponse(participant.id, survey.isImmediateDraw())
    }

    private fun validateIsAlreadyParticipated(
        surveyId: UUID,
        visitorId: String,
    ) {
        val participant = participantAdapter.findBySurveyIdAndVisitorId(surveyId, visitorId)
        participant?.let {
            throw AlreadyParticipatedException()
        }
    }
}
