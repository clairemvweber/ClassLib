package com.example.classlib

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {

    private lateinit var userEmail: EditText
    private lateinit var userPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var registerText: TextView
    private lateinit var progressBar: ProgressBar
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()
        registerText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        registerText.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        loginBtn.setOnClickListener { loginUserAccount() }
    }

    private fun loginUserAccount() {
        progressBar.visibility = View.VISIBLE

        val email: String = userEmail.text.toString()
        val password: String = userPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG)
                        .show()
                    startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
                } else {
                    val errorCode = (task.exception as FirebaseAuthException?)!!.errorCode
                    var errorText = ""
                    errorText = if(errorCode == "ERROR_INVALID_EMAIL" || errorCode == "ERROR_USER_MISMATCH" ||
                        errorCode == "ERROR_USER_NOT_FOUND" || errorCode == "ERROR_WRONG_PASSWORD"){
                        "Incorrect email or password"
                    } else if(errorCode == "ERROR_OPERATION_NOT_ALLOWED" || errorCode == "ERROR_OPERATION_NOT_ALLOWED"
                        || errorCode == "ERROR_OPERATION_NOT_ALLOWED" || errorCode == "ERROR_OPERATION_NOT_ALLOWED"){
                        "An error has occurred from the server. Please try again later"
                    } else{
                        "An unknown error has occurred. Please try again later"
                    }
                    Toast.makeText(
                        applicationContext,
                        errorText,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun initializeUI() {
        userEmail = findViewById(R.id.email)
        userPassword = findViewById(R.id.password)
        registerText = findViewById(R.id.registertext)
        loginBtn = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)
    }
}
