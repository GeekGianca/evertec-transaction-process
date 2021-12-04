package com.evertecinc.processtransaction.presentation.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.evertecinc.processtransaction.presentation.checkout.CheckoutActivity
import com.evertecinc.processtransaction.presentation.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, CheckoutActivity::class.java))
    }
}