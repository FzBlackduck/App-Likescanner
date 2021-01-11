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




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selecting: Button = findViewById(R.id.button)

        selecting.setOnClickListener {
            val intent = Intent(this@MainActivity, StillImageActivity::class.java)
            startActivity(intent)


        }

        val  bottomnavigationView: ChipNavigationBar = findViewById(R.id.tabbar)

        //bottomnavigationView.selectedItemId = R.id.home


        bottomnavigationView.setOnItemSelectedListener(object:
        ChipNavigationBar.OnItemSelectedListener{
            override fun onItemSelected(id: Int) {
                if (id == R.id.list){
                    startActivity(Intent(applicationContext, Showproduct::class.java))
//
                }
                if (id == R.id.star){
                    startActivity(Intent(applicationContext, StarList::class.java))
//
                }
                if (id == R.id.scanbarcode){
                    startActivity(Intent(applicationContext, StillImageActivity::class.java))
//
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

    }





}