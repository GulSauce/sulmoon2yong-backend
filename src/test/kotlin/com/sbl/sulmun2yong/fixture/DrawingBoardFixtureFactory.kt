package com.sbl.sulmun2yong.fixture

import com.sbl.sulmun2yong.drawing.domain.DrawingBoard
import com.sbl.sulmun2yong.drawing.domain.Reward
import java.util.UUID

object DrawingBoardFixtureFactory {
    const val SURVEY_PARTICIPANT_COUNT = 200

    private val rewards =
        arrayOf(
            Reward(UUID.randomUUID(), "아메리카노", "커피", 3),
            Reward(UUID.randomUUID(), "카페라떼", "커피", 2),
            Reward(UUID.randomUUID(), "햄버거", "음식", 2),
        )

    val totalRewardCount = rewards.sumOf { it.count }

    fun createDrawingBoard(id: UUID) =
        DrawingBoard.create(
            id = id,
            boardSize = SURVEY_PARTICIPANT_COUNT,
            rewards = rewards,
        )
}
