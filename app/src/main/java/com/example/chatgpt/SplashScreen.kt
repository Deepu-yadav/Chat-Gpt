package com.example.chatgpt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
         val splashTimeOut: Long = 3000 // Splash screen duration in milliseconds
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Create a Handler to delay the navigation to the main activity
        Handler().postDelayed({
            // Start the main activity
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)

            // Close the splash activity
            finish()
        }, splashTimeOut)


    }
}