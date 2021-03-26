package com.example.workshop1

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.login.Loginpage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import java.io.IOException

class Account : AppCompatActivity() {


    var name: TextView? = null

    var logout: Button? = null
    var image : ImageView? = null

    var getbarcodeaccount: ArrayList<String> = ArrayList()
    lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_account)

        val bundle = intent.extras
        if (bundle != null) {
            getbarcodeaccount = bundle.getStringArrayList("barcodeaccount")!!
        }

        logout = findViewById(R.id.logout_account)


         /** Toolbar */
//        val toolbar = findViewById<Toolbar>(R.id.toolbaraccount)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        toolbar.setNavigationOnClickListener { super.onBackPressed() }

        logout?.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            createRequest()
            mGoogleSignInClient.revokeAccess().addOnCompleteListener {
                val intent= Intent(this, Loginpage::class.java)
                startActivity(intent)
                finish()
            }
        }


    }
    private fun createRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
// pass the same server client ID used while implementing the LogIn feature earlier.
    }


//    fun loaduser(){
//
//        firebaseUser = FirebaseAuth.getInstance().currentUser
//        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
//            .child(firebaseUser!!.uid)
//            .child("profile")
//        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // for (datas in dataSnapshot.children) {
//                val nameDB = dataSnapshot.child("name").value.toString()
//                val emailDB = dataSnapshot.child("email").value.toString()
//                val phoneDB = dataSnapshot.child("phone").value.toString()
//                val imageDB = dataSnapshot.child("image").value.toString()
//                imageBitmap = decodeFromFirebaseBase64(imageDB)
//
//                name?.text = nameDB
//                mail?.text = emailDB
//                phone?.text = phoneDB
//                image?.setImageBitmap(imageBitmap)
//                // }
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//
//    }

//    @Throws(IOException::class)
//    private fun decodeFromFirebaseBase64(value: String?): Bitmap {
//        val decodedByteArray = android.util.Base64.decode(value, android.util.Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.size)
//    }



}