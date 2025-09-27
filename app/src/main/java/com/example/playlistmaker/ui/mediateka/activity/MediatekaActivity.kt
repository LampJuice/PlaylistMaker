package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActiviyMediatekaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediatekaActivity : AppCompatActivity() {

    private lateinit var binding: ActiviyMediatekaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiviyMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        binding.viewPager.adapter = MediatekaViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_favorites)
                else -> getString(R.string.tab_playlists)
            }
        }
        tabMediator.attach()


    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}