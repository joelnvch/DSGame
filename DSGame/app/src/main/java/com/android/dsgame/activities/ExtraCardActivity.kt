package com.android.dsgame.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.dsgame.R
import com.android.dsgame.activities.MyApplication.Companion.board
import com.android.dsgame.databinding.ActivityExtraCardBinding
import com.android.dsgame.managers.GameManager
import com.android.dsgame.model.Card
import com.android.dsgame.model.TripleCard
import com.android.dsgame.view.adaptors.CardElementAdaptor

class ExtraCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtraCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cards = board.extraCards.values.toList()
        var tripleCardList = TripleCard.createTripleCardList(cards as List<Card>)
        binding.selectedExtraCards.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.selectedExtraCards.adapter = CardElementAdaptor(tripleCardList.toTypedArray(), "EXTRA_CARDS_SELECTED")

        cards = GameManager.ALL_EXTRA_CARDS.values.toList()
        tripleCardList = TripleCard.createTripleCardList(cards)
        binding.extraCardList.layoutManager = LinearLayoutManager(this)
        binding.extraCardList.adapter = CardElementAdaptor(tripleCardList.toTypedArray(), "EXTRA_CARDS")


    }
}