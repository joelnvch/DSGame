package com.android.dsgame.model

class TripleCard{
    var card1: Card? = null
    var card2: Card? = null
    var card3: Card? = null

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as TripleCard

        return card1!!.id == other.card1!!.id   &&   card1!!.cardColor == other.card1!!.cardColor &&
               card2!!.id == other.card2!!.id   &&   card2!!.cardColor == other.card2!!.cardColor &&
               card2!!.id == other.card3!!.id   &&   card3!!.cardColor == other.card3!!.cardColor
    }

    companion object {
        fun createTripleCardList(cards: List<Card>): List<TripleCard>{
            var tripleCard = TripleCard()
            val tripleCardList = mutableListOf<TripleCard>()
            var isAdded: Boolean
            for (i in cards.indices) {
                isAdded = false
                if ((i + 1) % 3 == 1) {
                    tripleCard = TripleCard()
                    tripleCard.card1 = cards[i]

                } else if ((i + 1) % 3 == 2)
                    tripleCard.card2 = cards[i]

                else if ((i + 1) % 3 == 0) {
                    tripleCard.card3 = cards[i]
                    tripleCardList.add(tripleCard)
                    isAdded = true
                }

                if (i == cards.size-1)
                    if (!isAdded && !tripleCardList.contains(tripleCard))
                        tripleCardList.add(tripleCard)
            }

            return tripleCardList
        }
    }
}