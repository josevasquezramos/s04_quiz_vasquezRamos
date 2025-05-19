package com.uns.s04_quiz_vasquezramos.presentation

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.uns.s04_quiz_vasquezramos.R
import kotlin.text.*

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 1)
        val time = intent.getLongExtra("time", 0L)

        MediaPlayer.create(this, R.raw.result).start()

        findViewById<TextView>(R.id.txtScore).text = "$score / $total"
        val percentage = if (total != 0) (score * 100 / total) else 0
        findViewById<TextView>(R.id.txtPercentage).text = "$percentage%"
        findViewById<TextView>(R.id.txtTime).text = formatTime(time)

        findViewById<Button>(R.id.btnRestart).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}