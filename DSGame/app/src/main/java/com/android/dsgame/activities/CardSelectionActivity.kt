package com.android.dsgame.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dsgame.databinding.ActivityCardSelectionBinding
import com.android.dsgame.managers.GameManager
import com.android.dsgame.model.TripleCard
import com.android.dsgame.view.adaptors.CardElementAdaptor


class CardSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        var color = ""
        if (bundle != null)
            color = bundle.getString("color").toString()

        showBestCardsByColor(color)
        showCardsByColor(color)
    }

    private fun showCardsByColor(color: String) {
        val cards = GameManager.getCardsByColor(color)

        var tripleCard = TripleCard(color)
        val tripleCardList = mutableListOf<TripleCard>()
        var isAdded = false
        for (i in cards.indices) {
            if ((i + 1) % 3 == 1) {
                tripleCard = TripleCard(color)
                tripleCard.card1 = cards[i]

            } else if ((i + 1) % 3 == 2)
                tripleCard.card2 = cards[i]

            else if ((i + 1) % 3 == 0) {
                tripleCard.card3 = cards[i]
                tripleCardList.add(tripleCard)
                isAdded = true
            }

            if (isAdded && !tripleCardList.contains(tripleCard)) {
                tripleCardList.add(tripleCard)
                isAdded = false  // reset variable val
            }
        }

        binding.cardList.layoutManager = LinearLayoutManager(this)
        binding.cardList.adapter = CardElementAdaptor(tripleCardList.toTypedArray())
    }

    private fun showBestCardsByColor(color: String) {
        val bestCards = GameManager.getBestCards(color)
        val i = Intent(this, HomeActivity::class.java)
        i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        binding.ceCard1.setCardElement(bestCards[0])
        binding.ceCard1.setOnClickListener {
            GameManager.setCard(color, bestCards[0].id!!)
            startActivity(i)
        }

        binding.ceCard2.setCardElement(bestCards[1])
        binding.ceCard2.setOnClickListener {
            GameManager.setCard(color, bestCards[1].id!!)
            startActivity(i)
        }

        binding.ceCard3.setCardElement(bestCards[2])
        binding.ceCard3.setOnClickListener {
            GameManager.setCard(color, bestCards[2].id!!)
            startActivity(i)
        }
    }
}