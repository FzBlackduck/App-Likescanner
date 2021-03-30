package com.example.workshop1

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starproduct.Star
import com.example.starproduct.StarproductAdapter
import com.example.workshop1.modern_main.Main
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_product_recyclerview.*
import kotlinx.android.synthetic.main.activity_star_recyclerview.*


class StarList : AppCompatActivity(), StarproductAdapter.OndelClickListner, StarproductAdapter.OnitemviewClickListner {

    var getbarcodestar: ArrayList<String> = ArrayList()
    val star = ArrayList<Star>()
    val adapter = StarproductAdapter(star, this, this)


    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null

    // var Saveuserstar : ArrayList<String> = ArrayList()
    var arraystore: ArrayList<String> = ArrayList()
    var arrayname: ArrayList<String> = ArrayList()
    var nameDB: String = ""
    var priceDB: String = ""
    var imageDB: String = ""
    var quantityDB: String = ""
    var statusDB: String = ""
    var categoryDB: String = ""
    var promotion = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_star_recyclerview)


        val recyclerViewstar = findViewById<RecyclerView>(R.id.recyclerViewstar)

        //adding a layoutmanager
        recyclerViewstar.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        Log.v(
                VisionProcessorBase.MANUAL_TESTING_LOG,
                "////////////[]][HELLO]]]]]]////////////// "

        )

        getstoretest()


        var home = findViewById<View>(R.id.home)
        home.setOnClickListener {
            val i = Intent(this, Main::class.java)
            startActivity(i)
        }

        removeallstar.setOnClickListener {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                    .child(firebaseUser!!.uid)
                    .child("starlist")
            refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeValue()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            star.removeAll(star)
            adapter.notifyDataSetChanged()
            recyclerViewstar.adapter = adapter
        }
    }

    override fun onStart() {
        super.onStart()
        object : CountDownTimer(500, 500) {
            var dialog: ProgressDialog? = null
            override fun onTick(millisUntilFinished: Long) {
                dialog = ProgressDialog.show(this@StarList, "",
                        "Loading. Please wait...", true)
            }

            override fun onFinish() {
                dialog!!.dismiss()

            }
        }.start()
    }


    private fun getpromotion(store: String) {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
                .child(store)
                .child("promotion")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                promotion = dataSnapshot.child("sale").value.toString().toInt()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun getstoretest() {
        var refUsers: DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("starlist")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                    val key = datas.key
                    arraystore.add("" + key)
                }
                //loopstore = arraystore.indices
                Log.v(
                        VisionProcessorBase.MANUAL_TESTING_LOG,
                        "////////////[[[[test1]]]]]]////////////// ${arraystore.indices}"


                )
                for (i in arraystore) {
                    getpromotion(i)
                    test(i)
                    Connecttest(i)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun test(store: String) {
        Log.v(
                VisionProcessorBase.MANUAL_TESTING_LOG,
                "////////////[[[[test2]]]]]]////////////// ${store}"


        )
        var refUsers: DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("starlist")
                .child(store)
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                    val key = datas.key
                    arrayname.add("" + key)

                }
                Log.v(
                        VisionProcessorBase.MANUAL_TESTING_LOG,
                        "////////////[[[[key]]]]]]////////////// ${arrayname}"

                )
                //Connecttest(store)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun Connecttest(store: String) {
        Log.v(
                VisionProcessorBase.MANUAL_TESTING_LOG,
                "////////////[[[[test3]]]]]]////////////// ${store}"
        )
        var ref: DatabaseReference? = null
        ref = FirebaseDatabase.getInstance().reference.child("Product")
                .child(store)
                .child("barcode")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (datas in dataSnapshot.children) {


                    nameDB = datas.child("name").value.toString()

                    var chackname = arrayname.any { it == nameDB }

                    if (chackname.equals(true)) {

                        priceDB = datas.child("price").value.toString()
                        imageDB = datas.child("image").value.toString()
                        quantityDB = datas.child("quantity").value.toString()
                        statusDB = datas.child("status").value.toString()
                        categoryDB = datas.child("category").value.toString()
                        priceDB = ((priceDB.toInt() - promotion).toString())

                        star.add(Star("$nameDB", "฿$priceDB", "${quantityDB}",
                                "$statusDB", "$imageDB", "$categoryDB", "$store","$promotion"))
                        Log.v(
                                VisionProcessorBase.MANUAL_TESTING_LOG,
                                "////////////[[[[name]]]]]]////////////// ${nameDB}" +
                                        "[[[[priceDB]]]]] $priceDB" +
                                        "[[[imageDB]]]]]$imageDB"

                        )
                        recyclerViewstar.adapter = adapter
                    }
                }
                arrayname.removeAll(arrayname)

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }


//    private fun showliststar() {
//        var ref: DatabaseReference? = null
//        ref = FirebaseDatabase.getInstance().reference.child("Product").child("barcode")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                for (datas in dataSnapshot.children) {
//
//
//                    nameDB = datas.child("name").value.toString()
//
//                    var chackname = Saveuserstar.any { it == nameDB }
//
//                    if (chackname.equals(true)) {
//
//                        priceDB = datas.child("price").value.toString()
//                        imageDB = datas.child("image").value.toString()
//
//                        star.add(Star("$nameDB", "฿$priceDB", "$imageDB"))
//                        Log.v(
//                                VisionProcessorBase.MANUAL_TESTING_LOG,
//                                "////////////[[[[name]]]]]]////////////// ${nameDB}" +
//                                        "[[[[priceDB]]]]] $priceDB" +
//                                        "[[[imageDB]]]]]$imageDB"
//
//                        )
//                        recyclerViewstar.adapter = adapter
//
//                    }
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//
//        })
//    }


    override fun onClick(starList: Star, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("starlist")
                .child("${starList.storeid}")
                .child("${starList.name}")
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.ref.removeValue()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        star.removeAt(position)
        adapter.notifyItemChanged(position)
        adapter.notifyItemRangeRemoved(position, 1)

    }

    override fun itemviewClick(starList: Star, position: Int) {

        val intent = Intent(this, DetailProduct::class.java)
        intent.putExtra("name_detail", starList.name)
        intent.putExtra("price_detail", starList.price)
        intent.putExtra("quantity_detail", starList.quantity)
        intent.putExtra("status_detail", starList.status)
        intent.putExtra("image_detail", starList.image)
        intent.putExtra("category_detail", starList.category)
        intent.putExtra("storeid", starList.storeid)
        intent.putExtra("promotion", starList.promotion)
        intent.putExtra("event","star")
        startActivity(intent)

    }
}

