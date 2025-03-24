package com.bruno13palhano.sleeptight

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class SplashScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }
}