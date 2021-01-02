package com.fullpagedeveloper.mysound

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fullpagedeveloper.mysound.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var sp: SoundPool
    private var soundId: Int = 0
    private var spLoaded = false

    //mediaplayer
    private var mMediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = false

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


        //media player
        init()

        val btnPlay = binding.btnPlay
        val btnStop = binding.btnStop

        btnPlay.setOnClickListener {
            if (!isReady) {
                mMediaPlayer?.prepareAsync()
            } else {
                if (mMediaPlayer?.isPlaying as Boolean) {
                    mMediaPlayer?.pause()
                } else {
                    mMediaPlayer?.start()
                }
            }
        }

        btnStop.setOnClickListener {
            if (mMediaPlayer?.isPlaying as Boolean || isReady) {
                mMediaPlayer?.stop()
                isReady = false
            }
        }
    }

    private fun init() {
        mMediaPlayer = MediaPlayer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val attribute = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            mMediaPlayer?.setAudioAttributes(attribute)
        } else {
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        }
        val afd = applicationContext.resources.openRawResourceFd(R.raw.cow)
        try {
            mMediaPlayer?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mMediaPlayer?.setOnPreparedListener {
            isReady = true
            mMediaPlayer?.start()
        }

        mMediaPlayer?.setOnErrorListener { mp, what, extra -> false }
    }
}