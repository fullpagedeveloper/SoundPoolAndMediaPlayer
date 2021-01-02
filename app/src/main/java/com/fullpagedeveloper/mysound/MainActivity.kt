package com.fullpagedeveloper.mysound

import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.fullpagedeveloper.mysound.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSoundpool.setOnClickListener {
            if (spLoaded) {
                sp.play(soundId, 1f, 1f, 0, 0, 1f)
            }
        }


        sp = SoundPool.Builder()
                .setMaxStreams(10)
                .build()

        sp.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                spLoaded = false
            } else {
                Toast.makeText(this, "Gagal load", Toast.LENGTH_SHORT).show()
            }
        }

        soundId = sp.load(this, R.raw.cow, 1)
    }
}