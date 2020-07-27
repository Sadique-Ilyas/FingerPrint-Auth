package com.example.myfingerprintauthapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val biometricManager = androidx.biometric.BiometricManager.from(this)
        when (biometricManager.canAuthenticate()) {
            androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS ->
                Toast.makeText(this, "App can authenticate using biometrics.", Toast.LENGTH_LONG)
                    .show()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Toast.makeText(
                    this,
                    "No Fingerprint Scanner available on this device.",
                    Toast.LENGTH_LONG
                ).show()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Toast.makeText(
                    this,
                    "Biometric features are currently unavailable.",
                    Toast.LENGTH_LONG
                ).show()
            androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Toast.makeText(
                    this, "The user hasn't associated \" +\n" +
                            "        \"any Fingerprint with their account.", Toast.LENGTH_LONG
                ).show()
        }

        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt: androidx.biometric.BiometricPrompt =
            androidx.biometric.BiometricPrompt(
                this,
                executor,
                object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(
                            applicationContext,
                            "Authentication error: $errString", Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(
                            applicationContext, "Authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(
                            applicationContext,
                            "Authentication succeeded!", Toast.LENGTH_SHORT
                        ).show()
                    }
                })

        val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint Authentication")
            .setSubtitle("Login using fingerprint. Place your finger on Fingerprint Scanner")
            .setNegativeButtonText("Cancel")
            .build()

        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}