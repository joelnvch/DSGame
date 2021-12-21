package com.android.dsgame.model

data class TripleCard(val color: String){
    var card1: Card? = null
    var card2: Card? = null
    var card3: Card? = null

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as TripleCard

        return card1!!.id == other.card1!!.id
                && card2!!.id == other.card2!!.id
                && card2!!.id == other.card3!!.id
    }
}