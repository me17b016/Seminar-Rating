package com.example.mysecondberkley

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.core.Context
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class Login : AppCompatActivity() {

    lateinit var token : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        token = getSharedPreferences("user", android.content.Context.MODE_PRIVATE)
        signin_progress.visibility = View.INVISIBLE

        if (token.getString("loginId", " ") != " "
            && check_mail(token.getString("loginId", " "))) {
            if (token.getString("loginId", " ") == "admin123@iittp.ac.in") {
                val intent = Intent(this, admin::class.java)
                startActivity(intent)
                finish()
            }
            else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    fun signInUser (view: View) {
        val email = etsignEmail.text.toString()
        val password = etsignPassword.text.toString()

        if (email.isEmpty()) {
            etsignEmail.error = "enter your mail"
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            etsignPassword.error = "enter your password"
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
            return
        }
        if (!check_mail(email)) {
            Toast.makeText(this@Login, "Restricted Version", Toast.LENGTH_SHORT).show()
            return
        }

        btRegisterHere.isEnabled = false;
        bLogin.isEnabled = false
        signin_progress.visibility = View.VISIBLE
        var editor = token.edit()
        editor.putString("loginId", email)
        editor.commit()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                signin_progress.visibility = View.INVISIBLE
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
                    if (email == "admin123@iittp.ac.in") {
                        val intent = Intent(this, admin::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                else {
                    btRegisterHere.isEnabled = true
                    bLogin.isEnabled = true

                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Please check your email and password", Toast.LENGTH_SHORT).show()
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

    fun changeToRegister(view : View) {
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
        finish()
    }
}
