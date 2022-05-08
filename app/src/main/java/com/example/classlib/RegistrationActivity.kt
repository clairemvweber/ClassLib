package com.example.classlib

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

// registrationActivity based on the FirebaseEmailAuthExample
class RegistrationActivity : AppCompatActivity() {

    private lateinit var emailTV: EditText
    private lateinit var passwordTV: EditText
    private lateinit var regBtn: Button
    private lateinit var progressBar: ProgressBar
    private var validator = Validators()

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        regBtn.setOnClickListener { registerNewUser() }
    }

    private fun registerNewUser() {
        progressBar.visibility = View.VISIBLE

        val email: String = emailTV.text.toString()
        val password: String = passwordTV.text.toString()

        if (!validator.validEmail(email)) {
            Toast.makeText(applicationContext, "Please enter a valid email...", Toast.LENGTH_LONG)
                .show()
            return
        }
        if (!validator.validPassword(password)) {
            Toast.makeText(
                applicationContext,
                "Password must contain 8 characters with one letter and one number!",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val x = mAuth!!.createUserWithEmailAndPassword(email, password)

        x.addOnCompleteListener { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.register_success_string),
                    Toast.LENGTH_LONG
                ).show()
                startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
            } else {
                val errorCode = (task.exception as FirebaseAuthException?)!!.errorCode
                var errorMessage = ""
                errorMessage =
                    if (errorCode == "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" || errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                        "An account already exists with the same email"
                    } else {
                        "An unknown error has occurred. Please try again later"
                    }
                Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)
        regBtn = findViewById(R.id.register)
        progressBar = findViewById(R.id.progressBar)
    }
}
