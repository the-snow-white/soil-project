package com.example.soilproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.soilproject.activities.PestActivity
import com.example.soilproject.activities.Register

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button_pest: Button = findViewById(R.id.button_pest)
        button_pest.setOnClickListener {
            val intent = Intent(this, PestActivity::class.java)
            startActivity(intent)
            finish()
        }

        val button_analyze: Button = findViewById(R.id.button_analyze)
        button_analyze.setOnClickListener {
            val intent = Intent(this, UploadImage::class.java)
            startActivity(intent)
            finish()
        }
    }
}