package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.example.starproduct.Star

import com.example.starproduct.StarproductAdapter
import com.google.firebase.database.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_product_recyclerview.*
import kotlinx.android.synthetic.main.activity_product_recyclerview.recyclerView
import kotlinx.android.synthetic.main.activity_star_recyclerview.*

class StarList : AppCompatActivity(), StarproductAdapter.OnBarcodeClickListner {


    val star = ArrayList<Star>()
    val adapter = StarproductAdapter(star,this)

    var arraychild : ArrayList<String> = ArrayList()
    var arrayname : ArrayList<String> = ArrayList()
    var nameDB : String = ""
    var priceDB : String = ""
    var imageDB : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_star_recyclerview)


        val recyclerViewstar = findViewById<RecyclerView>(R.id.recyclerViewstar)

        //adding a layoutmanager
        recyclerViewstar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        Log.v(
                VisionProcessorBase.MANUAL_TESTING_LOG,
                "////////////[]][HELLO]]]]]]////////////// "

        )
        getname()
        showstar()
        //showliststar()

        /**---------------------------------------------------------------------------------------------------*/
        val bottomnavigationView: ChipNavigationBar = findViewById(R.id.tabbar)

        bottomnavigationView.setItemSelected(R.id.star,true);

        bottomnavigationView.setOnItemSelectedListener(object :
                ChipNavigationBar.OnItemSelectedListener {
            override fun onItemSelected(id: Int) {
                if (id == R.id.home) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
//
                }
                if (id == R.id.list) {
                    startActivity(Intent(applicationContext, Showproduct::class.java))
//
                }
                if (id == R.id.scanbarcode) {
                    startActivity(Intent(applicationContext, StillImageActivity::class.java))
//
                } else {
                    bottomnavigationView.setItemSelected(R.id.star, true);
                }
            }
        })


    }









    private fun showstar() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in arrayname.indices) {
                    for (datas in dataSnapshot.children) {

                        val getchild = datas.child("${arrayname[i]}/star").value.toString()
                        if(getchild == "showstar"){
                            val getchildname = arrayname[i]
                            arraychild.add(""+getchildname)
                        }
                        Log.v(
                                VisionProcessorBase.MANUAL_TESTING_LOG,
                                "////////////[[[[getname Event Star  ]]]]]]////////////// ${arraychild}"


                        )

                    }
                    //recyclerView.adapter = adapter
                }
                showliststar()
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }
    private fun getname() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product").child("barcode")
        refUsers.orderByChild("name").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val namecount = dataSnapshot.childrenCount
                for (datas in dataSnapshot.children) {

                    val getname = datas.child("name").value.toString()
                    arrayname.add("" + getname)


                    Log.v(
                            VisionProcessorBase.MANUAL_TESTING_LOG,
                            "////////////[[[[namecount]]]]]]////////////// ${namecount}" +
                                    "[[[[getnameAll]]]]] $arrayname"

                    )

                }
                //recyclerView.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }

    private fun showliststar() {
        var ref: DatabaseReference? = null
        ref = FirebaseDatabase.getInstance().reference.child("Product").child("barcode")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (datas in dataSnapshot.children) {


                        nameDB = datas.child("name").value.toString()

                        var chackname = arraychild.any { it == nameDB }

                        if (chackname.equals(true)) {

                            priceDB = datas.child("price").value.toString()
                            imageDB = datas.child("image").value.toString()

                            star.add(Star("$nameDB", "$priceDB", "$imageDB"))
                            Log.v(
                                    VisionProcessorBase.MANUAL_TESTING_LOG,
                                    "////////////[[[[name]]]]]]////////////// ${nameDB}" +
                                            "[[[[priceDB]]]]] $priceDB" +
                                            "[[[imageDB]]]]]$imageDB"

                            )
                            recyclerViewstar.adapter = adapter

                        }
                    }
                }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }


    override fun onClick(starList: Star, position: Int) {

         Toast.makeText(this, starList.name ,Toast.LENGTH_SHORT).show()
        //val del : Button = findViewById(R.id.delete_btn)

        //del.setOnClickListener{

            var map2 = mutableMapOf<String, Any>()
            map2["star"] = "unshowstar"
            var update: DatabaseReference? = null
            update = FirebaseDatabase.getInstance().reference
                .child("Product")
                .child("subproduct")
                .child("${starList.name}")
            update.updateChildren(map2)

        //}




    }
}
