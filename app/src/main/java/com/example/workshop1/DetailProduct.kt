package com.example.workshop1

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CircleIndicator.Product
import com.example.CircleIndicator.ProductAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_showdetail.*


class DetailProduct : AppCompatActivity() {

    private lateinit var refUsers: DatabaseReference
    var firebaseUser: FirebaseUser? = null
    var promotion = ""
    var store:String? = null

    private var mDatabase: DatabaseReference? = null
    private var mQuery: Query? = null

    private var num2: Int? = null
    var num: Int? = null
    var savenum: Int? = null

    var categoryList: ArrayList<String> = ArrayList()
    var categoryList2: ArrayList<String> = ArrayList()
    var priceListprice: ArrayList<String> = ArrayList()


    var getcountDB: ArrayList<String> = ArrayList()
    var sizecount: Int? = null

    ////////////rootDB
    var getbarcodeDB: ArrayList<String> = ArrayList()
    var getbarcodeDB2: String = ""

    /////////////
    var getidDB:String = ""
    var getnameDB_detail: String = ""
    var getpriceDB_detail: String = ""
    var getquantityDB_detail: String = ""
    var getstatusDB_detail: String = ""
    var getimageDB_detail: String = ""
    var getcategoryDB_detail: String = ""

     val product = ArrayList<Product>()
     val adapter = ProductAdapter(product)


    var rv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation =  (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_showdetail)

        rv = findViewById<View>(R.id.recyclerView_detail) as RecyclerView
        rv!!.setHasFixedSize(true)
        rv!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



        name_detail.text = intent.getStringExtra("name_detail")
        price_detail.text = intent.getStringExtra("price_detail")
        status_detail.text = intent.getStringExtra("status_detail")
        quantity_detail.text = intent.getStringExtra("quantity_detail")
        category_detail.text = intent.getStringExtra("category_detail")
        store =  intent.getStringExtra("storeid")
        promotion =  intent.getStringExtra("promotion")
        val input = intent.getStringExtra("image_detail")
        val getevent = intent.getStringExtra("event")

        Picasso.get()
                .load("" + input)
                .into(image_detail)

        /**------ slide view -----*/
        recommentfirebase()

        /**-------------check like-----------*/
        //var refUsers: DatabaseReference? = null
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
            .child(firebaseUser!!.uid)
            .child("starlist")
                .child(""+store)
            .child("${name_detail.text}")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //for (datas in dataSnapshot.children) {
                    val star = dataSnapshot.child("star").value.toString()
                    if (star == "Show") {
                        star_btn2.isLiked = true
                    }

               // }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        /**-------------event like & unlike-----------*/

        star_btn2.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                Toast.makeText(this@DetailProduct, "Liked!", Toast.LENGTH_SHORT).show()
                likeButton.isLiked = true

                firebaseUser = FirebaseAuth.getInstance().currentUser
                refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                    .child(firebaseUser!!.uid)
                    .child("starlist")
                        .child(""+store)
                    .child("${name_detail.text}")
                val userHashMap = HashMap<String, Any>()
                //userHashMap["uid"]= firebaseUserID
                userHashMap["star"] = "Show"
                refUsers!!.updateChildren(userHashMap)




            }
            override fun unLiked(likeButton: LikeButton) {
                Toast.makeText(this@DetailProduct, "UnLiked!", Toast.LENGTH_SHORT).show()
//                var map2 = mutableMapOf<String, Any>()
//                map2["star"] = "unshowstar"
//                var refupdate2: DatabaseReference? = null
//                refupdate2 = FirebaseDatabase.getInstance().reference
//                        .child("Product")
//                        .child("subproduct")
//                        .child("${name_detail.text}")
//                refupdate2.updateChildren(map2)

                firebaseUser = FirebaseAuth.getInstance().currentUser
                refUsers =  FirebaseDatabase.getInstance().reference.child("Account")
                    .child(firebaseUser!!.uid)
                    .child("starlist")
                        .child(""+store)
                    .child("${name_detail.text}")

                refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.ref.removeValue()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
//                val userHashMap = HashMap<String, Any>()
//                //userHashMap["uid"]= firebaseUserID
//                userHashMap["star"] = "unShow"
//                refUsers!!.updateChildren(userHashMap)



            }

        })

        if(getevent == "showproduct") {
            findViewById<ImageView>(R.id.back_detail).setOnClickListener {
                startActivity(Intent(this, Showproduct::class.java))
            }
        }
        if(getevent == "star"){
            findViewById<ImageView>(R.id.back_detail).setOnClickListener {
                startActivity(Intent(this, StarList::class.java))
            }
        }


    }


    private fun recommentfirebase() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
                .child(""+store)
                .child("barcode")
        refUsers.orderByChild("price").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val total = dataSnapshot.childrenCount.toInt()
                categoryList.add("${category_detail.text}")
                categoryList2.add("${name_detail.text}")
                for (datas in dataSnapshot.children) {
                    getcategoryDB_detail = datas.child("category").value.toString()
                    getnameDB_detail = datas.child("name").value.toString()
                    var filterbarcodeid = categoryList.any { it == getcategoryDB_detail }
                    val filterbarcodeid2 = categoryList2.none { it == getnameDB_detail }
                    if (filterbarcodeid.equals(true) ) {
                        if (filterbarcodeid2.equals(true)) {
                            getpriceDB_detail = datas.child("price").value.toString()
                            getimageDB_detail = datas.child("image").value.toString()
//                            getstatusDB_detail = datas.child("status").value.toString()
//                            getquantityDB_detail = datas.child("quantity").value.toString()
//                            getcategoryDB_detail = datas.child("category").value.toString()


                            //product.add(Product(getpriceDB_detail, getimageDB_detail, getnameDB_detail, getstatusDB_detail, getquantityDB_detail, getcategoryDB_detail))
                            product.add(Product(getpriceDB_detail, getimageDB_detail, getnameDB_detail, ""+store,promotion.toInt()))
                            //view_pager2.adapter = adapter
                            rv!!.adapter = adapter
                        }
                    }
                }
//                view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//                val indicator = findViewById<CircleIndicator3>(R.id.indicator)
//                indicator.setViewPager(view_pager2)
//                Log.v(
//                        VisionProcessorBase.MANUAL_TESTING_LOG,
//                        "////////////[[[[price]]]]]]////////////// ${categoryList},"
//
//
//                )
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}

