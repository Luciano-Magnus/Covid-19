package com.magnus.covid_19

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.*
import android.view.WindowManager
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
       var  myVideoView = findViewById<VideoView>(R.id.videoView);
       var path= ("android.resource://" + getPackageName() + "/" + R.raw.animacao_covid_19)
        var uri= Uri.parse(path)
        myVideoView.setVideoURI(uri)
        myVideoView.start()
        supportActionBar
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        Handler().postDelayed({

            val i = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            startActivity(i)

            finish()
        }, SPLASH_TIME_OUT.toLong())

    }
}
