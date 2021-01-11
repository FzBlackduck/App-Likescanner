package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.example.starproduct.Star
import com.example.starproduct.StarAdapter
import com.google.firebase.database.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_product_recyclerview.*

class StarList : AppCompatActivity() {


    val star = ArrayList<Star>()
    val adapter = StarAdapter(star)
    var arraychild : ArrayList<String> = ArrayList()


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
        showstar()


        /**---------------------------------------------------------------------------------------------------*/
        val  bottomnavigationView: ChipNavigationBar = findViewById(R.id.tabbar)

        //bottomnavigationView.selectedItemId = R.id.home

        bottomnavigationView.setOnItemSelectedListener(object:
            ChipNavigationBar.OnItemSelectedListener{
            override fun onItemSelected(id: Int) {
                if (id == R.id.home){
                    startActivity(Intent(applicationContext, MainActivity::class.java))
//
                }
                if (id == R.id.list){
                    startActivity(Intent(applicationContext, Showproduct::class.java))
//
                }
                if (id == R.id.scanbarcode){
                    startActivity(Intent(applicationContext, StillImageActivity::class.java))
//
                }
                else{
                    bottomnavigationView.setItemSelected(R.id.star,true);
                }
            }
        })






    }





    private fun showstar() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product").child("subproduct")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val num = dataSnapshot.childrenCount
                for (datas in dataSnapshot.children) {

                    val getchild = datas.value.toString()
                    arraychild.add("" + getchild)


                    Log.v(
                        VisionProcessorBase.MANUAL_TESTING_LOG,
                        "////////////[[[[filter]]]]]]////////////// ${num}" +
                                "[[[[array]]]]] $arraychild"

                    )

                }
                //recyclerView.adapter = adapter
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }

}
