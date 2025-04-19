package com.codezone.musicplayersimple.splash


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TypeWriterTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var mText: CharSequence = ""
    private var mIndex = 0
    private var mDelay: Long = 100

    private val handler = Handler(Looper.getMainLooper())

    private val characterAdder: Runnable = object : Runnable {
        override fun run() {
            text = mText.subSequence(0, mIndex++)
            if (mIndex <= mText.length) {
                handler.postDelayed(this, mDelay)
            }
        }
    }

    fun animateText(txt: CharSequence, delay: Long = 100) {
        mText = txt
        mIndex = 0
        mDelay = delay
        text = ""
        handler.removeCallbacks(characterAdder)
        handler.postDelayed(characterAdder, mDelay)
    }
}
