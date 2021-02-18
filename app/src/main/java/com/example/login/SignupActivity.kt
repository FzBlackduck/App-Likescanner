package com.example.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.workshop1.R
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.ByteArrayOutputStream

class SignupActivity : AppCompatActivity() {
    private lateinit var  mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""
    private var selectedProtoUri: Uri? = null
    private var bitmapimage:Bitmap? = null
    private var imageEncoded:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { super.onBackPressed() }
        setStatusBarWhite(this@SignupActivity)

        ///////////////////////////

        val add_image = findViewById<ImageView>(R.id.add_image)
        add_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        mAuth = FirebaseAuth.getInstance()
        button_signin.setOnClickListener {
            registerUser()
        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedProtoUri = data.data
             bitmapimage = MediaStore.Images.Media.getBitmap(contentResolver,selectedProtoUri)

            //val bitmapDrawble = Bitmap(bitmap)
            var image2 = findViewById<ImageView>(R.id.image_account)
          image2.setImageBitmap(bitmapimage)

            val baos = ByteArrayOutputStream()
            bitmapimage!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
            imageEncoded = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT)
        }
    }

    private fun registerUser() {
        val name: String = et_name.text.toString()
        val email: String = et_username.text.toString()
        val password: String = et_password.text.toString()
        val phone : String = et_phone.text.toString()



        if (name == ""){
            Toast.makeText(this@SignupActivity,"please wite name.",Toast.LENGTH_LONG).show()
        }
         else if (email == ""){
            Toast.makeText(this@SignupActivity,"please wite username.",Toast.LENGTH_LONG).show()
        }else if (password == "") {
            Toast.makeText(this@SignupActivity, "please wite password.", Toast.LENGTH_LONG).show()
        }else if (image_account.drawable.constantState == ContextCompat.getDrawable(this, R.drawable.ic_person)?.constantState){
            Toast.makeText(this@SignupActivity, "please wite image.", Toast.LENGTH_LONG).show()
        }else{
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful)
                    {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                                .child(firebaseUserID)
                                .child("profile")
                        val userHashMap = HashMap<String, Any>()
                        //userHashMap["uid"]= firebaseUserID
                        userHashMap["name"] = name
                        userHashMap["email"] = email
                        userHashMap["phone"] = phone
                        userHashMap["image"] = ""+imageEncoded
                        refUsers.updateChildren(userHashMap)

                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this@SignupActivity, Loginpage::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                    }else{
                        Toast.makeText(this@SignupActivity,"error message" + task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                    }


                }
        }

    }

    private fun setStatusBarWhite(activity: AppCompatActivity){
        //Make status bar icons color dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.WHITE
        }
    }



}
