package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetector.CameraXFaceDetector
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    var getbarcodemain: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = intent.extras
        if (bundle != null) {
            getbarcodemain = bundle.getStringArrayList("barcodemain")!!
        }

        val votebtn : Button  = findViewById(R.id.votebtn)
        votebtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, CameraXFaceDetector::class.java))
        }

        val  bottomnavigationView: ChipNavigationBar = findViewById(R.id.tabbar)

        //bottomnavigationView.selectedItemId = R.id.home
        bottomnavigationView.setItemSelected(R.id.home,true);

        bottomnavigationView.setOnItemSelectedListener(object:
        ChipNavigationBar.OnItemSelectedListener{
            override fun onItemSelected(id: Int) {
                if (id == R.id.list){
                    val intent = Intent(this@MainActivity, Showproduct::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcode", getbarcodemain)
                    }
                    startActivity(intent)
                    //startActivity(Intent(applicationContext, Showproduct::class.java))

                }
                if (id == R.id.star){
                    val intent = Intent(this@MainActivity, StarList::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcodestar", getbarcodemain)
                    }
                    startActivity(intent)
                    //startActivity(Intent(this@MainActivity, StarList::class.java))
                }
                if (id == R.id.scanbarcode) {
                    val intent = Intent(this@MainActivity, StillImageActivity::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcodescan", getbarcodemain)
                    }
                    startActivity(intent)
                }
                if (id == R.id.Account) {
                    val intent = Intent(this@MainActivity, Account::class.java)
                        if (bundle != null) {
                        intent.putExtra("barcodeaccount", getbarcodemain)
                    }
                    startActivity(intent)

                }
                else{
                    bottomnavigationView.setItemSelected(R.id.home,true);
                }
            }
        })

    }

}