package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starproduct.Star
import com.example.starproduct.StarproductAdapter
import com.example.workshop1.modurn_main.Main
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detailproduct.*
import kotlinx.android.synthetic.main.activity_product_recyclerview.*
import kotlinx.android.synthetic.main.activity_star_recyclerview.*


class StarList : AppCompatActivity(), StarproductAdapter.OndelClickListner {

    var getbarcodestar: ArrayList<String> = ArrayList()
    val star = ArrayList<Star>()
    val adapter = StarproductAdapter(star, this)


    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null

    var Saveuserstar : ArrayList<String> = ArrayList()
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
        CheckUsershowstar()

        var home = findViewById<View>(R.id.home)
        home.setOnClickListener {
            val i = Intent(this, Main::class.java)
            i.putExtra("barcodemain", getbarcodestar)
            startActivity(i)
        }
        //showliststar()
        /**---------------------------------------------------------------------------------------------------*/
    }





    private fun CheckUsershowstar() {
        var refUsers: DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
            .child(firebaseUser!!.uid)
            .child("starlist")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in arrayname.indices) {
                    //for (datas in dataSnapshot.children) {

                    val getchild = dataSnapshot.child("${arrayname[i]}/star").value.toString()

                    if (getchild == "Show") {
                        val getchildname = arrayname[i]
                        Saveuserstar.add("" + getchildname)
                    }
                    Log.v(
                            VisionProcessorBase.MANUAL_TESTING_LOG,
                            "////////////[[[[getname Event Star  ]]]]]]////////////// ${Saveuserstar}"


                    )

                    //}
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

                    var chackname = Saveuserstar.any { it == nameDB }

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

         Toast.makeText(this, "${starList.name} : DELETE", Toast.LENGTH_SHORT).show()

//            var map2 = mutableMapOf<String, Any>()
//            map2["star"] = "unshowstar"
//            var update: DatabaseReference? = null
//            update = FirebaseDatabase.getInstance().reference
//                .child("Product")
//                .child("subproduct")
//                .child(starList.name)
//            update.updateChildren(map2)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
            .child(firebaseUser!!.uid)
            .child("starlist")
            .child("${starList.name}")

//        val userHashMap = HashMap<String, Any>()
//        //userHashMap["uid"]= firebaseUserID
//        userHashMap["star"] = "unShow"
//        refUsers!!.updateChildren(userHashMap)


        refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.ref.removeValue()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        star.removeAt(position)
        adapter.notifyItemChanged(position)
        adapter.notifyItemRangeRemoved(position,1)

//            val intent = Intent(this, StarList::class.java)
//            intent.putExtra("barcodestar", getbarcodestar)
//            startActivity(intent)




    }

}

