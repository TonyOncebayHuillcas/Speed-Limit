package com.forward.controllerspeed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.forward.controllerspeed.ui.login.LoginActivity
import com.forward.controllerspeed.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        goToUserScreen()
    }

    private fun goToUserScreen() {
        Handler().postDelayed({
            Intent(this, LoginActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }, 2000L)

    }
}
