package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val search = findViewById<Button>(R.id.search_button)
        val media = findViewById<Button>(R.id.media_button)
        val settings = findViewById<Button>(R.id.settings_button)

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)

                startActivity(searchIntent)
            }

        }
        search.setOnClickListener(searchClickListener)

        media.setOnClickListener {
            startActivity(Intent(this@MainActivity, MediatekaActivity::class.java))


        }
        settings.setOnClickListener { startActivity( Intent(this@MainActivity, SettingsActivity::class.java)) }


    }
}