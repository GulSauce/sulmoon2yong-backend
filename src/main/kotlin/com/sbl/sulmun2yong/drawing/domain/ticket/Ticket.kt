package com.sbl.sulmun2yong.drawing.domain.ticket

interface Ticket {
    var isSelected: Boolean
    val isWinning: Boolean

    override fun toString(): String
}
