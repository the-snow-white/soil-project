package com.example.soilproject.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import com.example.soilproject.MainActivity
import com.example.soilproject.R

class PestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pest)

        val HomeArrow:ImageView = findViewById(R.id.HomeArrow)
        HomeArrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val maize:RelativeLayout = findViewById(R.id.maize)
        maize.setOnClickListener {
            val intent = Intent(this, MaizeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cabbage:RelativeLayout = findViewById(R.id.cabbage)
        cabbage.setOnClickListener {
            val intent = Intent(this, CabbageActivity::class.java)
            startActivity(intent)
            finish()
        }
        val carrot:RelativeLayout = findViewById(R.id.carrot)
        carrot.setOnClickListener {
            val intent = Intent(this, CarrotActivity::class.java)
            startActivity(intent)
            finish()
        }
        val potatoes:RelativeLayout = findViewById(R.id.potatoes)
        potatoes.setOnClickListener {
            val intent = Intent(this, PototoeActivity::class.java)
            startActivity(intent)
            finish()
        }
        val onions:RelativeLayout = findViewById(R.id.onions)
        onions.setOnClickListener {
            val intent = Intent(this, OnionActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}