package com.example.workshop1.modern_main

import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.facedetector.CameraXFaceDetector
import com.example.googlemap.MapsActivity
import com.example.workshop1.R
import com.example.workshop1.Select
import com.example.workshop1.Showproduct
import com.example.workshop1.StarList
import kotlinx.android.synthetic.main.activity_choicescan.*
import kotlinx.android.synthetic.main.activity_showdetail.*

class Choicescan: AppCompatActivity(), MyAdapter.MainClickListner {
    var myLists: MutableList<MyList>? = null
    var rv: RecyclerView? = null
    var adapter: MyAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_choicescan)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { super.onBackPressed() }

        rv = findViewById<View>(R.id.recyclerView_choice) as RecyclerView
        rv!!.setHasFixedSize(true)
        rv!!.layoutManager = GridLayoutManager(this, 2)
        myLists = ArrayList()

        getdata()
    }
    private fun getdata() {
        myLists!!.add(MyList(R.drawable.a,"IMAGE SCANNER"))
        myLists!!.add(MyList(R.drawable.b,"REALTIME SCANNER"))

        adapter = MyAdapter(myLists!!, this,this)
        rv!!.adapter = adapter
    }

    override fun onClick(myList: MyList, position: Int) {

          if(myList.titlename == "IMAGE SCANNER"){
            val intent = Intent(this, Select::class.java)
              intent.putExtra("select","IMAGE SCANNER")
            startActivity(intent)
          }
        if(myList.titlename == "REALTIME SCANNER"){
            val intent = Intent(this, Select::class.java)
            intent.putExtra("select","REALTIME SCANNER")
            startActivity(intent)
        }


    }

}