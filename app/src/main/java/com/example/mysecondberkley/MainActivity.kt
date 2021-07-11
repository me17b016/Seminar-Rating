package com.example.mysecondberkley

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(){
    var groupNo : String = ""
    var candidate1: String = ""
    var candidate2: String = ""
    var candidate1Roll : String = ""
    var candidate2Roll : String = ""
    var stat = false
    var nameOfUser : String = ""
    var emailAsKey : String = ""
    var userEmail : String = ""
    lateinit var token : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        token = getSharedPreferences("user", Context.MODE_PRIVATE)
        userEmail = token.getString("loginId", " ")
        emailAsKey = userEmail.replace('.', ',')
        test()    // retrive user information
        check()   // check whether admin enabled the rating system or not
    }

    fun check() {
        FirebaseDatabase.getInstance().getReference("Enable").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (e in p0.children) {
                        if (e.child("value").getValue(Boolean::class.java)!!) {
                            stat = true
                            groupNo = e.child("stat").getValue(String()::class.java)!!
                            main_submit.isEnabled = true
                            FirebaseDatabase.getInstance().getReference("seminargroups").addValueEventListener(object : ValueEventListener{
                                override fun onCancelled(p0: DatabaseError) {
                                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                                }
                                override fun onDataChange(p0: DataSnapshot) {
                                    for (e in p0.children) {
                                        if (e.child("group").getValue().toString() == groupNo) {
                                            candidate1 = e.child("candidate1").child("name").getValue().toString()
                                            candidate1Roll = e.child("candidate1").child("rollno").getValue().toString()
                                            candidate2 = e.child("candidate2").child("name").getValue().toString()
                                            candidate2Roll = e.child("candidate2").child("rollno").getValue().toString()
                                            candidate1Name.text = candidate1
                                            candidate2Name.text = candidate2
                                            candidate1RollNo.text = candidate1Roll
                                            candidate2RollNo.text = candidate2Roll
                                            break
                                        }
                                    }
                                }
                            })
                        }
                        else {
                            stat = false
                            main_submit.isEnabled = false
                        }
                    }
                }
            }
        })
    }

    private fun test() {
        FirebaseDatabase.getInstance().getReference("UserInformation").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    for (e in p0.children) {
                        if (e.child("email").getValue(String::class.java)!! == userEmail) {
                            nameOfUser = e.child("name").getValue(String::class.java)!!
                            main_text.text = nameOfUser
                            main_email.text = userEmail
                            break
                        }
                    }
                }

            }
        })
    }

    fun changeToLogin(view: View) {
        FirebaseAuth.getInstance().signOut()
        var editor = token.edit();
        editor.putString("loginId", " ")
        editor.commit()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun submitRating(view: View) {
        val database = FirebaseDatabase.getInstance()
        var newNode = groupNo
        val rat11 : Float = candidate1MotiAudi.rating.toFloat()
        val rat12 : Float = candidate1PreSkill.rating.toFloat()
        val rat13 : Float = candidate1Enthusiasm.rating.toFloat()
        val rat14 : Float = candidate1ExplFinPoints.rating.toFloat()
        val rat15 : Float = candidate1ResQues.rating.toFloat()
        val sugg1 : String = candidate1Sugg.text.toString()
        val rat21 : Float = candidate2MotiAudi.rating.toFloat()
        val rat22 : Float = candidate2PreSkill.rating.toFloat()
        val rat23 : Float = candidate2Enthusiasm.rating.toFloat()
        val rat24 : Float = candidate2ExplFinPoints.rating.toFloat()
        val rat25 : Float = candidate2ResQues.rating.toFloat()
        val sugg2 : String = candidate2Sugg.text.toString()
        if (rat11 > 0 && rat12 > 0 && rat13 > 0 && rat14 > 0 && rat15 > 0 && sugg1 != "" &&
            rat21 > 0 && rat22 > 0 && rat23 > 0 && rat24 > 0 && rat25 > 0 && sugg2 != "") {
            main_submit.isEnabled = false
            val myRef = database.getReference("${newNode}")
            val id = myRef.push().key
            val node = User(userEmail!!, "${nameOfUser}", rat11, rat12, rat13, rat14, rat15, sugg1, rat21, rat22, rat23, rat24, rat25, sugg2)
            myRef.child(emailAsKey).setValue(node)
            Toast.makeText(this, "Your responce has successfully submitted", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }
    }
}
