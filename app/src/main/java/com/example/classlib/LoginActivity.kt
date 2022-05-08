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
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

// login activity based on the FirebaseEmailAuthExample
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
        registerText.text = Html.fromHtml("<u><strong>Register</strong></u>")
        registerText.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        loginBtn.setOnClickListener { loginUserAccount() }
    }

    // function to login to the user Account
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

        // check if the login was successful
        mAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG)
                        .show()
                    // send the user's email to the next activity
                    intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                    intent.putExtra("username", email)
                    startActivity(intent)
                } else {
                    // means that the login was unsuccessful and will show the reason behind it
                    val errorCode = (task.exception as FirebaseAuthException?)!!.errorCode
                    var errorText = ""
                    errorText =
                        if (errorCode == "ERROR_INVALID_EMAIL" || errorCode == "ERROR_USER_MISMATCH" ||
                            errorCode == "ERROR_USER_NOT_FOUND" || errorCode == "ERROR_WRONG_PASSWORD"
                        ) {
                            "Incorrect email or password"
                        } else if (errorCode == "ERROR_OPERATION_NOT_ALLOWED" || errorCode == "ERROR_OPERATION_NOT_ALLOWED"
                            || errorCode == "ERROR_OPERATION_NOT_ALLOWED" || errorCode == "ERROR_OPERATION_NOT_ALLOWED"
                        ) {
                            "An error has occurred from the server. Please try again later"
                        } else {
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
