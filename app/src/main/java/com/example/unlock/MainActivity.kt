package com.example.unlock

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val view = findViewById<PicUnlockView>(R.id.my_view)

        view.callback = {
            Log.d(TAG, "密码是: $it")
        }

    }




}