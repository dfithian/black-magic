package com.fithian.dan.blackmagic

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.util.Log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch)
        //System.loadLibrary("black_magic")
    }

    override fun onResume() {
        super.onResume()
        setContentView(R.layout.main)
        Log.i("MainActivity", sessionInfo())
        findViewById<Button>(R.id.teammatesButton).setOnClickListener(View.OnClickListener {
            startActivity(Intent("com.fithian.dan.blackmagic.TeammatesActivity"))
        })
    }

    external fun sessionInfo(): String
}
