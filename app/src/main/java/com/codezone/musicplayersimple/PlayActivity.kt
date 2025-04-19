package com.codezone.musicplayersimple

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.codezone.musicplayersimple.databinding.ActivityPlayBinding
import java.io.File

@Suppress("DEPRECATION")
class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding
    private lateinit var songs: ArrayList<File>
    private var position: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topRecy.setOnClickListener {
            finish()
        }


        val songList = intent.getSerializableExtra("songs") as ArrayList<Song>
        songs = ArrayList()
        songList.forEach {
            songs.add(File(it.file.path))
        }

        position = intent.getIntExtra("position", 0)


        MyMediaPlayer.instance?.stop()
        MyMediaPlayer.instance?.release()
        MyMediaPlayer.instance = null

        mediaPlayer = MediaPlayer().apply {
            setDataSource(songs[position].path)
            prepare()
            start()
        }
        MyMediaPlayer.instance = mediaPlayer

        setupUI()
        updateSeekBar()
    }

    private fun setupUI() {
        binding.txtSongName.text = songs[position].name
        binding.seekBar.max = mediaPlayer?.duration ?: 0
        binding.btnPlayPause.text = "Pause"

        mediaPlayer?.setOnCompletionListener {
            position = (position + 1) % songs.size
            changeSong()
        }

        binding.btnNext.setOnClickListener {
            position = (position + 1) % songs.size
            changeSong()
        }

        binding.btnPrev.setOnClickListener {
            position = if (position - 1 < 0) songs.size - 1 else position - 1
            changeSong()
        }

        binding.btnPlayPause.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    binding.btnPlayPause.text = "Play"
                } else {
                    it.start()
                    binding.btnPlayPause.text = "Pause"
                }
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun changeSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(songs[position].path)
            prepare()
            start()
        }
        MyMediaPlayer.instance = mediaPlayer

        setupUI()
        updateSeekBar()
    }

    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                mediaPlayer?.let {
                    binding.seekBar.progress = it.currentPosition
                    binding.txtCurrentTime.text = formatTime(it.currentPosition)
                    binding.txtTotalTime.text = formatTime(it.duration)
                    if (it.isPlaying) {
                        handler.postDelayed(this, 1000)
                    }
                }
            }
        }, 0)
    }

    private fun formatTime(ms: Int): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)

    }
}
