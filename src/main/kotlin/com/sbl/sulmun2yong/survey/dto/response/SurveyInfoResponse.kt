package com.sbl.sulmun2yong.survey.dto.response

import com.sbl.sulmun2yong.survey.domain.Survey
import com.sbl.sulmun2yong.survey.domain.SurveyStatus
import com.sbl.sulmun2yong.survey.domain.reward.Reward
import java.util.Date

// TODO: 설문 제작자 정보도 추가하기
data class SurveyInfoResponse(
    val title: String,
    val description: String,
    val status: SurveyStatus,
    val finishedAt: Date,
    val currentParticipants: Int,
    val targetParticipants: Int,
    val thumbnail: String,
    val rewards: List<RewardInfoResponse>,
) {
    companion object {
        fun of(
            survey: Survey,
            currentParticipants: Int,
        ) = SurveyInfoResponse(
            title = survey.title,
            description = survey.description,
            status = survey.status,
            finishedAt = survey.finishedAt,
            currentParticipants = currentParticipants,
            targetParticipants = survey.targetParticipantCount,
            thumbnail = survey.thumbnail ?: Survey.DEFAULT_THUMBNAIL_URL,
            rewards = survey.rewards.map { it.toResponse() },
        )
    }

    data class RewardInfoResponse(
        val item: String,
        val count: Int,
    )
}

private fun Reward.toResponse() =
    SurveyInfoResponse.RewardInfoResponse(
        item = this.name,
        count = this.count,
    )
