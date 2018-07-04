package com.draco.renderhell

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.VideoView
import android.media.MediaPlayer
import android.util.Log
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var activityMain: LinearLayout
    private lateinit var start: Button
    private lateinit var delay: EditText
    private lateinit var renders: EditText
    private var currentCount = 0

    private var delay_ms: Int = 500
    private var renders_num: Int = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityMain = findViewById(R.id.activity_main)
        start = findViewById(R.id.start)
        delay = findViewById(R.id.delay)
        renders = findViewById(R.id.renders)

        layoutParams = WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        layoutParams.width = 50
        layoutParams.height = 50
        layoutParams.horizontalMargin = 0f

        start.setOnClickListener {
            delay_ms = Integer.parseInt(delay.text.toString())
            renders_num = Integer.parseInt(renders.text.toString())
            activityMain.removeAllViews()
            for (i in 0..renders_num) {
                createVideoView()
                layoutParams.horizontalMargin += layoutParams.width
            }
        }

    }

    fun createVideoView() {
        val videoView = VideoView(this)
        videoView.setVideoURI(Uri.parse("android.resource://$packageName/${R.raw.vid}"))
        videoView.layoutParams = layoutParams

        videoView.start()
        activityMain.addView(videoView)

        // if cant play dont add it to the counter and just refresh it now
        var errored = false
        videoView.setOnErrorListener { mp, what, extra ->
            errored = true
            return@setOnErrorListener true
        }

        // refresh video view after it loads
        videoView.setOnPreparedListener {
            android.os.Handler().postDelayed({
                activityMain.removeView(videoView)
                createVideoView()
                if (!errored) addTitleCounter()
            }, delay_ms.toLong())
        }
    }

    fun addTitleCounter() {
        title = "${++currentCount}"
    }
}
