package com.example.login

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.workshop1.CameraXLivePreviewActivity
import com.example.workshop1.R
import com.example.workshop1.modurn_main.Main
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login2.*


class Loginpage : AppCompatActivity(){
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: AuthStateListener? = null

    override fun onStart() {
        super.onStart()
        //val user = mAuth!!.currentUser
        mAuth!!.addAuthStateListener(mAuthListener!!);

//        if (user != null) {
//            val intent = Intent(applicationContext, Profile::class.java)
//            startActivity(intent)
//        }
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login2)
        setStatusBarTransparent(this@Loginpage)

        mAuth = FirebaseAuth.getInstance()

        createRequest()

        findViewById<View>(R.id.google_signIn).setOnClickListener { signIn() }

        findViewById<Button>(R.id.button_signup).setOnClickListener {
            startActivity(Intent(this@Loginpage, SignupActivity::class.java))
        }


        button_login.setOnClickListener {
            loginUser()
        }

         mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                startActivity(Intent(this@Loginpage, Main::class.java))
            } else {
                // User is signed out
            }
            // ...
        }








    }

    private fun createRequest() {


        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                Log.d(CameraXLivePreviewActivity.toString(), "Error it : " + e.message)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task ->

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth!!.currentUser
                    val intent = Intent(applicationContext, Profile::class.java)
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@Loginpage, "fail", Toast.LENGTH_SHORT)
                        .show()
                }


                }

                // ...
            }

    private fun setStatusBarTransparent(activity: AppCompatActivity){
        //Make Status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        //Make status bar icons color dark
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.statusBarColor = Color.WHITE
        }
    }


    private fun loginUser() {
        val username1: String = et_username1.text.toString()
        val password1: String = et_password1.text.toString()

        if(username1 == ""){
            Toast.makeText(this@Loginpage, "please wite username.", Toast.LENGTH_LONG).show()
        }else if (password1 == ""){
            Toast.makeText(this@Loginpage, "please wite password.", Toast.LENGTH_LONG).show()
        }else{
            mAuth!!.signInWithEmailAndPassword(username1, password1)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful)
                    {
                        val intent = Intent(this@Loginpage, Main::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }else
                    {
                        Toast.makeText(
                            this@Loginpage,
                            "" + username1 + "" + password1,
                            Toast.LENGTH_LONG
                        ).show()
                        Toast.makeText(
                            this@Loginpage,
                            "error message" + task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

        }

    }

    companion object {
        private const val RC_SIGN_IN = 1
    }
}