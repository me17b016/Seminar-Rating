package com.example.mysecondberkley

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_admin.*
import java.util.*

class admin : AppCompatActivity() {
    var status = false
    var curr_grp : Int = 0
    var isEnable: Boolean = false

    // defining variables for calculations

    var totalParticipants : Int = 0;

    // for candidate1
    var motivationSkills1 : Double = 0.0
    var enthusiasm1 : Double = 0.0
    var presentationSkills1 : Double = 0.0
    var explOfFinerPoints1 : Double = 0.0
    var responceToQues1 : Double = 0.0

    // for admin (1)
    var admin11 : Double = 0.0
    var admin12 : Double = 0.0
    var admin13 : Double = 0.0
    var admin14 : Double = 0.0
    var admin15 : Double = 0.0

    // for candidate2
    var motivationSkills2 : Double = 0.0
    var enthusiasm2 : Double = 0.0
    var presentationSkills2 : Double = 0.0
    var explOfFinerPoints2 : Double = 0.0
    var responceToQues2 : Double = 0.0

    // for admin (2)
    var admin21 : Double = 0.0
    var admin22 : Double = 0.0
    var admin23 : Double = 0.0
    var admin24 : Double = 0.0
    var admin25 : Double = 0.0

    // over all rating variable

    var overall1 : Double = 0.0
    var overall2 : Double = 0.0
    lateinit var token : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        token = getSharedPreferences("user", Context.MODE_PRIVATE)
        isEnable = token.getString("isEnable", "false").toBoolean()
        if (isEnable) {
            bEnable.text = "Get Results"
            curr_grp = token.getString("groupNo", "").toString().toInt()
            admin_group.text = token.getString("groupNo", "").toString()
            admin_group.text = curr_grp.toString()
        }
    }

    fun changeToLog(view : View) {
        FirebaseAuth.getInstance().signOut()
        var editor = token.edit();
        editor.putString("loginId", " ")
        editor.commit()
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    fun Enable(view: View) {
        if (bEnable.text == "Enable") {

            bEnable.text = "Get Results"
            var editor = token.edit()
            editor.putString("isEnable", "true")
            editor.commit()
            status = true
            curr_grp = etAdmin_value.text.toString().toInt()
            editor.putString("groupNo", "${curr_grp}");
            editor.commit()
            var node = NodeStatus("${curr_grp}", true) // enable curr group

            FirebaseDatabase.getInstance().getReference("Enable")
                .child("admin").setValue(node).addOnCompleteListener{
                        val intent = Intent(this, AdminRating::class.java)
                        startActivity(intent)
                }
        }
        else {
            candidate1.text = token.getString("candidate1", "").toString()
            candidate2.text = token.getString("candidate2", "").toString()
            candidaterollno1.text = token.getString("candidate1rollno", "").toString()
            candidaterollno2.text = token.getString("candidate2rollno", "").toString()
            admin_group.text = token.getString("groupNo", "").toString()
            bEnable.text = "Enable"
            var editor = token.edit()
            editor.putString("isEnable", "false")
            editor.commit()
            editor.putString("groupNo", "0");
            editor.commit()
            var node = NodeStatus("${curr_grp}", false)

            FirebaseDatabase.getInstance().getReference("Enable")
                .child("admin").setValue(node).addOnCompleteListener{
                    if (it.isSuccessful) {
                        FirebaseDatabase.getInstance().getReference("${curr_grp}").addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0!!.exists()) {
                                    totalParticipants = 0
                                    motivationSkills1 = 0.0
                                    enthusiasm1 = 0.0
                                    presentationSkills1 = 0.0
                                    explOfFinerPoints1 = 0.0
                                    responceToQues1 = 0.0

                                    // for admin (1)
                                    admin11 = 0.0
                                    admin12 = 0.0
                                    admin13 = 0.0
                                    admin14 = 0.0
                                    admin15 = 0.0

                                    // for candidate2
                                    motivationSkills2 = 0.0
                                    enthusiasm2 = 0.0
                                    presentationSkills2 = 0.0
                                    explOfFinerPoints2 = 0.0
                                    responceToQues2 = 0.0

                                    // for admin (2)
                                    admin21 = 0.0
                                    admin22 = 0.0
                                    admin23 = 0.0
                                    admin24 = 0.0
                                    admin25 = 0.0

                                    for (e in p0.children) {
                                        if (e.child("id").getValue().toString() != "admin123@iittp.ac.in") {
                                            motivationSkills1 += e.child("motivatingskills1").getValue().toString().toDouble()
                                            enthusiasm1 += e.child("enthusiasm1").getValue().toString().toDouble()
                                            presentationSkills1 += e.child("presentationskills1").getValue().toString().toDouble()
                                            explOfFinerPoints1 += e.child("explanationoffinerpoints1").getValue().toString().toDouble()
                                            responceToQues1 += e.child("responcetoquestions1").getValue().toString().toDouble()
                                            motivationSkills2 += e.child("motivatingskills2").getValue().toString().toDouble()
                                            enthusiasm2 += e.child("enthusiasm2").getValue().toString().toDouble()
                                            presentationSkills2 += e.child("presentationskills2").getValue().toString().toDouble()
                                            explOfFinerPoints2 += e.child("explanationoffinerpoints2").getValue().toString().toDouble()
                                            responceToQues2 += e.child("responcetoquestions2").getValue().toString().toDouble()
                                            totalParticipants++
                                        }
                                        else {
                                            admin11 = e.child("motivatingskills1").getValue().toString().toDouble()
                                            admin12 = e.child("enthusiasm1").getValue().toString().toDouble()
                                            admin13 = e.child("presentationskills1").getValue().toString().toDouble()
                                            admin14 = e.child("explanationoffinerpoints1").getValue().toString().toDouble()
                                            admin15 = e.child("responcetoquestions1").getValue().toString().toDouble()
                                            admin21 = e.child("motivatingskills2").getValue().toString().toDouble()
                                            admin22 = e.child("enthusiasm2").getValue().toString().toDouble()
                                            admin23 = e.child("presentationskills2").getValue().toString().toDouble()
                                            admin24 = e.child("explanationoffinerpoints2").getValue().toString().toDouble()
                                            admin25 = e.child("responcetoquestions2").getValue().toString().toDouble()
                                        }
                                        tvnumberPar.text = totalParticipants.toString()
                                        overall1 = ((((motivationSkills1 / totalParticipants) + admin11) / 2)+
                                                (((enthusiasm1 / totalParticipants) + admin12) / 2) +
                                                (((presentationSkills1 / totalParticipants) + admin13) / 2) +
                                                (((explOfFinerPoints1 / totalParticipants) + admin14) / 2) +
                                                (((responceToQues1 / totalParticipants) + admin15) / 2)) / 5
                                        overall2 = ((((motivationSkills2 / totalParticipants) + admin21) / 2)+
                                                (((enthusiasm2 / totalParticipants) + admin22) / 2) +
                                                (((presentationSkills2 / totalParticipants) + admin23) / 2) +
                                                (((explOfFinerPoints2 / totalParticipants) + admin24) / 2) +
                                                (((responceToQues2 / totalParticipants) + admin25) / 2)) / 5
                                        var o1 = String.format("%.1f", overall1).toString()
                                        var o2 = String.format("%.1f", overall2).toString()
                                        tvoverallrating1.text = o1
                                        tvoverallrating2.text = o2
                                    }
                                }
                            }

                        })
                    }
                }
        }
    }
}
