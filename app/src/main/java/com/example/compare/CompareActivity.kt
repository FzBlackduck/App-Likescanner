package com.example.compare

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_compare.*
import kotlinx.android.synthetic.main.activity_product_recyclerview.*

class CompareActivity: AppCompatActivity() {

    var namestore : String? = null
    var storeid : String? = null
    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null
    var Arraydatalist : ArrayList<String> = ArrayList()
    var ArrayListname : ArrayList<String> = ArrayList()
    var ArrayListprice : ArrayList<String> = ArrayList()

    /**--  Cheaper  ---*/
    val cheaper = ArrayList<Cheaper>()
    val adapter_c = CheaperAdapter(cheaper)

    /**--  Expensive  ---*/
    val expensive = ArrayList<Expensive>()
    val adapter_s = ExpensiveAdapter(expensive)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_compare)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { super.onBackPressed() }



       namestore = intent.getStringExtra("namestore")
        storeid = intent.getStringExtra("storeid")


        val recyclerView_c = findViewById<RecyclerView>(R.id.recyclerViewCheaper)
        recyclerView_c.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val recyclerView_s = findViewById<RecyclerView>(R.id.recyclerViewExpensive)
        recyclerView_s.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        getdatalist()
    }


    private fun getdatalist(){

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("datalist")
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                    val getname =  datas.key.toString()

                  if(storeid.equals(getname)) { }else{ Arraydatalist.add("" + getname) }
                    Log.i("Checkarray",""+Arraydatalist+"@@$storeid")
                }
                for(id in Arraydatalist){
                    compare(id)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun compare(id: String) {
        var refUsers: DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("datalist")
                .child(id)
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                    val key = datas.key.toString()
                    //ArrayListname.add("$key")
                    Log.i("Checkarray0", "" + ArrayListname)
                        val price = dataSnapshot.child("$key/price").value.toString()
                        val image = dataSnapshot.child("$key/image").value.toString()
                    val nameDB = dataSnapshot.child("$key/name").value.toString()
                    val idDB = dataSnapshot.child("$key/id").value.toString()
                        Log.i("Checkarray1", "" + key)
                        selected(price, id, idDB, image,nameDB)
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun selected(pricedatalist: String, id: String, idDB: String, image: String,nameDB:String) {
        Log.i("Checkarray2",""+pricedatalist)
        Log.i("Checkarray3",""+storeid)
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
                .child(storeid!!)
                .child("barcode")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val num = dataSnapshot.childrenCount
                for (datas in dataSnapshot.children) {
                    val idlist = datas.child("id").value.toString()

                    //var filter = ArrayListname.any { it == name }
                    if (idDB == idlist) {
                        val price = datas.child("price").value.toString()
                        val different = (pricedatalist.toInt() - price.toInt())

                        if (different <= 0) {
                            cheaper.add(
                                    Cheaper(
                                            "$image",
                                            "$nameDB",
                                            "" + id,
                                            "$storeid",
                                            "฿$pricedatalist",
                                            "฿$price",
                                            "฿${-different}"


                                    )
                            )

                            recyclerViewCheaper.adapter = adapter_c

                        }
                        if(different > 0){
                            expensive.add(
                                    Expensive(
                                            "$image",
                                            "$nameDB" ,
                                            "$id",
                                            "$storeid" ,
                                            "฿$pricedatalist",
                                            "฿$price",
                                            "฿${different}"


                                    )
                            )

                            recyclerViewExpensive.adapter = adapter_s
                        }
                    }


                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }





}