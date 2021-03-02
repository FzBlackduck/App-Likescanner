package com.example.workshop1

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.example.workshop1.modern_main.Main
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_product_recyclerview.*
import kotlin.collections.ArrayList


class Showproduct : AppCompatActivity(), BarcodeAdapter.OnBarcodeClickListner,BarcodeAdapter.ClickdeleteListner {

    var getnameDB: ArrayList<String> = ArrayList()
    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null
    var arrayname : ArrayList<String> = ArrayList()
    var arraystore : ArrayList<String> = ArrayList()
    var loopstore : Int? = null
    var num: Int? = null
    var nameDB: String = ""
    var priceDB: String = ""
    var quantityDB: String = ""
    var statusDB: String = ""
    var imageDB:String = ""
    var categoryDB:String = ""
    var idDB:String = ""
    var getid_btndelete:String =""
    ////
    val users = ArrayList<User>()
    val adapter = BarcodeAdapter(users, this, this)
    val promotion = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_recyclerview)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        //recyclerView.adapter = BarcodeAdapter(arrayListOf<User>(User("xx", "", "", ""), User("yy", "", "", "")))
        //recyclerView.adapter!!.notifyDataSetChanged()


                getstoretest()


            //getname()
            //loaddata()

       //Connectfirebase()

        var home = findViewById<View>(R.id.home)
        home.setOnClickListener {
            val i = Intent(this, Main::class.java)
            startActivity(i)
        }


        removeall.setOnClickListener{
            firebaseUser = FirebaseAuth.getInstance().currentUser
            refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                    .child(firebaseUser!!.uid)
                    .child("datalist")
            refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeValue()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
            Toast.makeText(this, "click" , Toast.LENGTH_SHORT).show()
            users.removeAll(users);
            adapter.notifyDataSetChanged()
            recyclerView.adapter = adapter
        }

        /**---------------------------------------------------------------------------------------------------*/



        if (savedInstanceState != null) {

            getnameDB = savedInstanceState.getStringArrayList("savebarcode") as ArrayList<String>
            Log.v(
                    VisionProcessorBase.MANUAL_TESTING_LOG,
                    "////////////[[[[USE]]]]]]////////////// ${getnameDB},"

            )

        }

    }

//    private fun getname() {
//        var refUsers: DatabaseReference? = null
//        refUsers = FirebaseDatabase.getInstance().reference.child("Product").child("barcode")
//        refUsers.orderByChild("name").addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // val namecount = dataSnapshot.childrenCount
//                for (datas in dataSnapshot.children) {
//                    val getname = datas.child("name").value.toString()
//                    arrayname.add("" + getname)
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//    }

    private  fun getstoretest() {
        var refUsers: DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("datalist")
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
                for(i in arraystore) {
                    test(i)
                    Connecttest(i)
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }




    private  fun test(store: String) {
        Log.v(
                VisionProcessorBase.MANUAL_TESTING_LOG,
                "////////////[[[[test2]]]]]]////////////// ${store}"


        )
        var refUsers: DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("datalist")
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
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
                .child(store)
                .child("barcode")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //val num = dataSnapshot.childrenCount
                for (datas in dataSnapshot.children) {
                    idDB = datas.child("name").value.toString()

                    var filterbarcodeid = arrayname.any { it == idDB }


                    if (filterbarcodeid.equals(true)) {
                        nameDB = datas.child("name").value.toString()
                        priceDB = datas.child("price").value.toString()
                        quantityDB = datas.child("quantity").value.toString()
                        statusDB = datas.child("status").value.toString()
                        imageDB = datas.child("image").value.toString()
                        categoryDB = datas.child("category").value.toString()
                        priceDB = ((priceDB.toInt()-promotion).toString())


                        users.add(
                                User(
                                        "" + nameDB,
                                        "฿" + priceDB,
                                        "$quantityDB",
                                        "" + statusDB,
                                        "" + imageDB,
                                        "" + categoryDB,
                                        ""+store


                                )
                        )

                        recyclerView.adapter = adapter
                    }
                    Log.v(
                            VisionProcessorBase.MANUAL_TESTING_LOG,
                            "////////////[[[[filter1]]]]]]////////////// ${filterbarcodeid},"

                    )

                }
              arrayname.clear()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }



//    private  fun loaddata(){
//        var refUsers: DatabaseReference? = null
//        firebaseUser = FirebaseAuth.getInstance().currentUser
//        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
//                .child(firebaseUser!!.uid)
//                .child("datalist")
//        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (i in arrayname.indices) {
//                    //for (datas in dataSnapshot.children) {
//
//                    val getchild = dataSnapshot.child("${arrayname[i]}/status").value.toString()
//
//                    if (getchild == "Have") {
//                        val getchildname = arrayname[i]
//                        getnameDB.add("" + getchildname)
//                    }
//                    Log.v(
//                            VisionProcessorBase.MANUAL_TESTING_LOG,
//                            "////////////[[[[getname Event Star  ]]]]]]////////////// ${getnameDB}"
//
//
//                    )
//
//                    //}
//                    //recyclerView.adapter = adapter
//                }
//                //Connectfirebase()
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//
//            }
//
//        })
//    }


/** -----------------  click list on recycler view ------------------- */
    override fun onClick(userList: User, position: Int) {
        //Toast.makeText(this, userList.name ,Toast.LENGTH_SHORT).show()

        val intent = Intent(this, DetailProduct::class.java)
        intent.putExtra("name_detail", userList.name)
        intent.putExtra("price_detail", userList.price)
        intent.putExtra("quantity_detail", userList.quantity)
         intent.putExtra("status_detail", userList.status)
        intent.putExtra("image_detail", userList.image)
       intent.putExtra("category_detail", userList.category)
       intent.putExtra("storeid",userList.storeid)
        startActivity(intent)

    }

    override fun onClickdelete(userList: User, position: Int) {

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                .child(firebaseUser!!.uid)
                .child("datalist")
                .child("${userList.storeid}")
                .child("${userList.name}")
        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.ref.removeValue()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        users.removeAt(position)
        adapter.notifyItemChanged(position)
        adapter.notifyItemRangeRemoved(position, 1)
//        val intent = Intent(this@Showproduct, Showproduct::class.java)
//        startActivity(intent)
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("savebarcode", getnameDB)


    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        //get value
        savedInstanceState.getStringArrayList("savebarcode")
        super.onRestoreInstanceState(savedInstanceState)

    }


}





