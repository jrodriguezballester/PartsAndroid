package com.example.examen3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start home activity
        startActivity(Intent(this, AuthActivity::class.java))
        // close splash activity
        finish()
    }
}