package com.ysered.custombuttons

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ysered.custombuttons.utils.extensions.debug
import com.ysered.custombuttons.views.CircleButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CircleButton>(R.id.circleButton).onClickListener = {
            debug("############# on circle clicked!")
        }
    }
}
