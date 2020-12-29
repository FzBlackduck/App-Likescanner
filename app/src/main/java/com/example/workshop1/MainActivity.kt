package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selecting: Button = findViewById(R.id.btn_click)

        selecting.setOnClickListener {
            val intent = Intent(this@MainActivity, StillImageActivity::class.java)
            startActivity(intent)


        }
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")


        //myRef.setValue("/55")

        /** -----------------  */
        /** -----------------  */
        var facestring:String? = "1234567891"


        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                     var getname = datas.child("$facestring/name").value.toString()
                     var getid = datas.child("$facestring/id").value.toString()

                    if(getid == facestring) {
                        Text1.text  = getname
                    }
                    else {
                        Text1.text = "null"
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })



    }

}