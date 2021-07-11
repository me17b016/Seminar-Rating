package com.example.mysecondberkley

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin_rating.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.candidate1Enthusiasm
import kotlinx.android.synthetic.main.activity_main.candidate1ExplFinPoints
import kotlinx.android.synthetic.main.activity_main.candidate1MotiAudi
import kotlinx.android.synthetic.main.activity_main.candidate1PreSkill
import kotlinx.android.synthetic.main.activity_main.candidate1ResQues
import kotlinx.android.synthetic.main.activity_main.candidate1Sugg
import kotlinx.android.synthetic.main.activity_main.candidate2Enthusiasm
import kotlinx.android.synthetic.main.activity_main.candidate2ExplFinPoints
import kotlinx.android.synthetic.main.activity_main.candidate2MotiAudi
import kotlinx.android.synthetic.main.activity_main.candidate2PreSkill
import kotlinx.android.synthetic.main.activity_main.candidate2ResQues
import kotlinx.android.synthetic.main.activity_main.candidate2Sugg

class AdminRating : AppCompatActivity() {
    lateinit var token : SharedPreferences
    val userEmail : String = "admin123@iittp.ac.in"
    val emailAsKey : String = "admin123@iittp,ac,in"
    var groupNo : Int = 0
    val nameOfUser : String = "Dr. Venkaiah N"
    private var candidate1: String = ""
    private var candidate2: String = ""
    private var candidate1Roll : String = ""
    private var candidate2Roll : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_rating)
        admin_text.text = nameOfUser
        admin_email.text = userEmail
        token = getSharedPreferences("user", Context.MODE_PRIVATE)
        groupNo = token.getString("groupNo", "0").toInt()
        test.text = groupNo.toString()
        init()
    }
    fun subRating(view: View) {
        val database = FirebaseDatabase.getInstance()
        var newNode = groupNo
        val rat11 : Float = candidat1MotiAudi.rating.toFloat()
        val rat12 : Float = candidat1PreSkill.rating.toFloat()
        val rat13 : Float = candidat1Enthusiasm.rating.toFloat()
        val rat14 : Float = candidat1ExplFinPoints.rating.toFloat()
        val rat15 : Float = candidat1ResQues.rating.toFloat()
        val sugg1 : String = candidat1Sugg.text.toString()
        val rat21 : Float = candidat2MotiAudi.rating.toFloat()
        val rat22 : Float = candidat2PreSkill.rating.toFloat()
        val rat23 : Float = candidat2Enthusiasm.rating.toFloat()
        val rat24 : Float = candidat2ExplFinPoints.rating.toFloat()
        val rat25 : Float = candidat2ResQues.rating.toFloat()
        val sugg2 : String = candidat2Sugg.text.toString()
        if (rat11 > 0 && rat12 > 0 && rat13 > 0 && rat14 > 0 && rat15 > 0 && sugg1 != "" &&
            rat21 > 0 && rat22 > 0 && rat23 > 0 && rat24 > 0 && rat25 > 0 && sugg2 != "") {
            val myRef = database.getReference("${newNode}")
            val id = myRef.push().key
            val node = User(userEmail!!, "${nameOfUser}", rat11, rat12, rat13, rat14, rat15, sugg1, rat21, rat22, rat23, rat24, rat25, sugg2)
            myRef.child(emailAsKey).setValue(node).addOnCompleteListener {
                Toast.makeText(this, "Your responce has successfully submitted", Toast.LENGTH_SHORT).show()
                intent = Intent(this, admin::class.java)
                startActivity(intent)
                finish()
            }

        }
        else {
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }
    }

    private fun init() {
        FirebaseDatabase.getInstance().getReference("seminargroups").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (e in p0.children) {
                    if (e.child("group").getValue().toString() == groupNo.toString()) {
                        candidate1 = e.child("candidate1").child("name").getValue().toString()
                        candidate1Roll = e.child("candidate1").child("rollno").getValue().toString()
                        candidate2 = e.child("candidate2").child("name").getValue().toString()
                        candidate2Roll = e.child("candidate2").child("rollno").getValue().toString()
                        candidate1Nam.text = candidate1
                        candidate2Nam.text = candidate2
                        candidate1RollN.text = candidate1Roll
                        candidate2RollN.text = candidate2Roll
                        var editor = token.edit()
                        editor.putString("candidate1", candidate1)
                        editor.putString("candidate2", candidate2)
                        editor.putString("candidate1rollno", candidate1Roll)
                        editor.putString("candidate2rollno", candidate2Roll)
                        editor.commit()
                        break
                    }
                }
            }
        })
    }

}
