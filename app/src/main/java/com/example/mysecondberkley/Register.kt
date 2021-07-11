package com.example.mysecondberkley

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        reg_progress.visibility = View.INVISIBLE
    }

    fun backToSignIn(view: View) {                                  // back to Login
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun registerUser(view : View) {                                 // Register user
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        val username = etUsername.text.toString()

        if (email.isEmpty()) {                                      // conditions for valid email and password
            etEmail.error = "enter your mail"
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            etPassword.error = "enter your password"
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (username.isEmpty()) {
            etUsername.error = "enter your username"
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (!check_mail(email)) {
            Toast.makeText(this@Register, "Restricted Version", Toast.LENGTH_SHORT).show()
            return
        }

        bRegister.isEnabled = false                                 // disable register and "back to login"
        backToLog.isEnabled = false
        reg_progress.visibility = View.VISIBLE
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                reg_progress.visibility = View.INVISIBLE
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, Login::class.java)
                    var n_email = email.replace('.', ',')
                    FirebaseDatabase.getInstance().getReference("UserInformation").child(n_email)
                        .setValue(user_information(username, email)).addOnCompleteListener({
                            if (it.isSuccessful) {
                                startActivity(intent)
                                finish()
                                return@addOnCompleteListener
                            }
                        })
                    return@addOnCompleteListener
                }
                else {
                    bRegister.isEnabled = true
                    backToLog.isEnabled = true
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed Registeration ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun check_mail(emai : String): Boolean {                    // checks email (this is restricted version)
        var domain = ""
        var num = ""
        if (emai == "admin123@iittp.ac.in") return true
        if (emai.length != 20) return false
        // me17b016@iittp.ac.in
        // admin123@iittp.ac.in
        // 0123456789
        for (i in 8..19) {
            domain += emai[i]
        }
        if (!Character.isDigit(emai[5])) {
            return false
        }
        else num += emai[5]
        if (!Character.isDigit(emai[6])) {
            return false
        }
        else num += emai[6]
        if (!Character.isDigit(emai[7])) {
            return false
        }
        else num += emai[7]
        var rollNo : Int = num.toInt()
        //Log.d("string", f.toString())
        if (emai[0] != 'm' || emai[1] != 'e' || emai[2] != '1'
            || emai[3] != '7' || emai[4] != 'b' || emai[5] != '0'
            || domain != "@iittp.ac.in" || rollNo > 28 || rollNo <= 0 || rollNo == 7 || rollNo == 11 || rollNo == 15 || rollNo == 13) {
            return false
        }
        return true
    }
}
