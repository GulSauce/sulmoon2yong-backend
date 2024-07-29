package com.sbl.sulmun2yong.drawing.domain

import com.sbl.sulmun2yong.drawing.domain.ticket.NonWinningTicket
import com.sbl.sulmun2yong.drawing.domain.ticket.Ticket
import com.sbl.sulmun2yong.drawing.domain.ticket.WinningTicket
import com.sbl.sulmun2yong.drawing.exception.InvalidDrawingBoardException

class TicketFactory {
    companion object {
        fun createTickets(
            rewards: Array<Reward>,
            maxTicketCount: Int,
        ): Array<Ticket> {
            val tickets = mutableListOf<Ticket>()
            rewards.map { reward ->
                repeat(reward.count) {
                    tickets.add(WinningTicket.create(rewardName = reward.name))
                    require(tickets.size <= maxTicketCount) { throw InvalidDrawingBoardException() }
                }
            }

            repeat(maxTicketCount - tickets.size) {
                tickets.add(NonWinningTicket.create())
            }

            tickets.shuffle()
            return tickets.toTypedArray()
        }
    }
}
