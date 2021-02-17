package com.example.login

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.workshop1.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.io.IOException


class Profile : AppCompatActivity() {
    var name: TextView? = null
    var mail: TextView? = null
    var logout: Button? = null

    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null
    var image : ImageView? = null
    private var imageBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        logout = findViewById(R.id.logout)
        name = findViewById(R.id.name)
        mail = findViewById(R.id.mail)
        image =findViewById(R.id.image_account)


        val signInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (signInAccount != null) {
            name?.text = signInAccount.displayName
            mail?.text = signInAccount.email
        }



        logout?.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, Loginpage::class.java)
                startActivity(intent)
            }



        /**-----------------------------------------------------------------------------------------*/
        loaduser()


        }


    fun loaduser(){

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account").child(firebaseUser!!.uid)
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
               // for (datas in dataSnapshot.children) {
                    val nameDB = dataSnapshot.child("name").value.toString()
                    val usernameDB = dataSnapshot.child("username").value.toString()
                    val imageDB = dataSnapshot.child("image").value.toString()
                    imageBitmap = decodeFromFirebaseBase64(imageDB)

                    name?.text = nameDB
                    mail?.text = usernameDB
                    image?.setImageBitmap(imageBitmap)
               // }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    @Throws(IOException::class)
    private fun decodeFromFirebaseBase64(value: String?): Bitmap {
        val decodedByteArray = android.util.Base64.decode(value, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
    }


}





