package com.android.dsgame.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.dsgame.databinding.ActivityAppSelectionBinding

class AppSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBoardCalculator.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }
}