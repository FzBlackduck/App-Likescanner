package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
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


//        val selecting: Button = findViewById(R.id.button)
//
//        selecting.setOnClickListener {
//            val intent = Intent(this@MainActivity, StillImageActivity::class.java)
//            startActivity(intent)
//
//
//        }

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
                if (id == R.id.scanbarcode){
                    val intent = Intent(this@MainActivity, StillImageActivity::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcodescan", getbarcodemain)
                    }
                    startActivity(intent)
                    //startActivity(Intent(this@MainActivity, StillImageActivity::class.java))

                }
                else{
                    bottomnavigationView.setItemSelected(R.id.home,true);
                }
            }
        })




//        bottomnavigationView.setOnItemSelectedListener(object:
//            BottomNavigationView.OnNavigationItemSelectedListener {
//            override fun onNavigationItemSelected(item: MenuItem): Boolean {
//                when (item.itemId) {
//                    R.id.scanbarcode -> {
//                        startActivity(Intent(applicationContext, StillImageActivity::class.java))
//                        overridePendingTransition(0,0)
//                        return true
//                    }
//                    R.id.list -> {
//                        startActivity(Intent(applicationContext, Showproduct::class.java))
//                        overridePendingTransition(0,0)
//                        return true
//                    }
//                    R.id.star -> {
//                        startActivity(Intent(applicationContext, StarList::class.java))
//                        overridePendingTransition(0,0)
//                        return true
//                    }
//                    R.id.home -> return true
//                }
//                return false
//            }
//        })

//        val camera : Button = findViewById(R.id.camera)
//        camera.setOnClickListener{
//            val intent = Intent(applicationContext, CameraXLivePreviewActivity::class.java)
//            startActivity(intent)
//        }




    }





}