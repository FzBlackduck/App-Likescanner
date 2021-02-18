package com.example.workshop1

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import java.io.IOException

class Account : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null
    var name: TextView? = null
    var mail: TextView? = null
    var phone: TextView? = null
    var logout: Button? = null
    var image : ImageView? = null
    var imageBitmap: Bitmap? = null
    var getbarcodeaccount: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        val bundle = intent.extras
        if (bundle != null) {
            getbarcodeaccount = bundle.getStringArrayList("barcodeaccount")!!
        }

        name = findViewById(R.id.name_account)
        mail = findViewById(R.id.email_account)
        image =findViewById(R.id.image_account)
        logout = findViewById(R.id.logout_account)
        phone = findViewById(R.id.phone_account)

         /** Toolbar */
//        val toolbar = findViewById<Toolbar>(R.id.toolbaraccount)
//        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        toolbar.setNavigationOnClickListener { super.onBackPressed() }

        logout?.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, Loginpage::class.java)
            startActivity(intent)
        }

        loaduser()

        val  bottomnavigationView: ChipNavigationBar = findViewById(R.id.tabbar)

        //bottomnavigationView.selectedItemId = R.id.home
        bottomnavigationView.setItemSelected(R.id.Account,true);

        bottomnavigationView.setOnItemSelectedListener(object:
            ChipNavigationBar.OnItemSelectedListener{
            override fun onItemSelected(id: Int) {
                if (id == R.id.list){
                    val intent = Intent(this@Account, Showproduct::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcode", getbarcodeaccount)
                    }
                    startActivity(intent)
                    //startActivity(Intent(applicationContext, Showproduct::class.java))

                }
                if (id == R.id.star){
                    val intent = Intent(this@Account, StarList::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcodestar", getbarcodeaccount)
                    }
                    startActivity(intent)
                    //startActivity(Intent(this@MainActivity, StarList::class.java))
                }
                if (id == R.id.scanbarcode) {
                    val intent = Intent(this@Account, StillImageActivity::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcodescan", getbarcodeaccount)
                    }
                    startActivity(intent)
                }
                if (id == R.id.home){
                    val intent = Intent(this@Account, MainActivity::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcodemain", getbarcodeaccount)
                    }
                    startActivity(intent)
                    //startActivity(Intent(applicationContext, MainActivity::class.java))
//
                }
                else{
                    bottomnavigationView.setItemSelected(R.id.Account,true);
                }
            }
        })


    }


    fun loaduser(){

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
            .child(firebaseUser!!.uid)
            .child("profile")
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // for (datas in dataSnapshot.children) {
                val nameDB = dataSnapshot.child("name").value.toString()
                val emailDB = dataSnapshot.child("email").value.toString()
                val phoneDB = dataSnapshot.child("phone").value.toString()
                val imageDB = dataSnapshot.child("image").value.toString()
                imageBitmap = decodeFromFirebaseBase64(imageDB)

                name?.text = nameDB
                mail?.text = emailDB
                phone?.text = phoneDB
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