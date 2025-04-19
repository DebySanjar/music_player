package com.codezone.musicplayersimple.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.codezone.musicplayersimple.MainActivity
import com.codezone.musicplayersimple.R
import com.codezone.musicplayersimple.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    private lateinit var typeWriter: TypeWriterTextView
    private lateinit var typeWriterd: TypeWriterTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        typeWriter = findViewById(R.id.typewriterText)
        typeWriter.animateText("Musico", 200)

        typeWriterd = findViewById(R.id.creator)
        typeWriterd.animateText("Creator by Abduganiyev", 90)


        val imageView = findViewById<ImageView>(R.id.logo_image)
        val rotate = AnimationUtils.loadAnimation(this, R.anim.play_anim)
        imageView.startAnimation(rotate)

        // 3 soniyadan keyin MainActivityga o‘tish
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2800)


    }
}