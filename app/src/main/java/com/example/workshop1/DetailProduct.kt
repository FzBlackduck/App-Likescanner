package com.example.workshop1

import android.app.AlertDialog
import android.app.slice.Slice
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.CircleIndicator.Product
import com.example.CircleIndicator.ViewPagerAdapter
import com.google.android.material.slider.Slider
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detailproduct.*
import kotlinx.android.synthetic.main.activity_list_detailproduct.*
import me.relex.circleindicator.CircleIndicator3


class DetailProduct : AppCompatActivity() {

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
    val adapter = ViewPagerAdapter(product)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailproduct)

        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Product Detail"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)



        name_detail.text = getIntent().getStringExtra("name_detail")
        price_detail.text = getIntent().getStringExtra("price_detail")
        status_detail.text = getIntent().getStringExtra("status_detail")
        quantity_detail.text = getIntent().getStringExtra("quantity_detail")
        category_detail2.text = getIntent().getStringExtra("category_detail")
        val input = getIntent().getStringExtra("image_detail")
        Picasso.get()
                .load("" + input)
                .into(image_detail)

        /**------ slide view -----*/
        recommentfirebase()
        //countDB()
        //rootbarcode()
       // filteritemDB()
       //orderby()

        //postToList()
        // view_pager2.adapter = adapter
        // view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL






    }


//    /**------ count limit barcode -----*/
//    private fun countDB() {
//
//        mDatabase = FirebaseDatabase.getInstance().reference.child("Product")
//        mDatabase!!.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (datas in dataSnapshot.children) {
//                    getcountDB = datas.child("${1}").value as ArrayList<String>
//
//                    for (i in getcountDB.indices) {
//                        savenum = i
//                    }
//
//                    Log.v(
//                            VisionProcessorBase.MANUAL_TESTING_LOG,
//                            "////////////[[[[count]]]]]]////////////// ${getcountDB}," +
//                                    "${savenum?.plus(1)}"
//                    )
//                }
//
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//    }
//
//
//    /**------ GetDB find barcode and ADD to ArrayList<>  -----*/
//
//    private fun rootbarcode() {
//
//        mDatabase = FirebaseDatabase.getInstance().reference.child("Product")
//        mDatabase!!.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (i in getcountDB.indices) {
//                    for (datas in dataSnapshot.children) {
//                        getbarcodeDB2 = datas.child("${1}/${i}").value.toString()
//                        getbarcodeDB.add("" + getbarcodeDB2)
//                        ///////
//
//                        num2 = i
//                        filteritemDB(num2!!)
//                    }
//
//                    Log.v(
//                            VisionProcessorBase.MANUAL_TESTING_LOG,
//                            "////////////[[[[b]]]]]]////////////// $getbarcodeDB"
//                    )
//
//
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {}
//        })
//    }
//
//    /**------ find price or image for ArrayList<> -----*/
//
//    private fun filteritemDB(num2: Int?) {
//
//        mDatabase = FirebaseDatabase.getInstance().reference
//        mQuery = mDatabase!!.child("Product").orderByChild("0/${getbarcodeDB[num2!!]}/category").equalTo("${category_detail.text}")
//                 mQuery!!.addListenerForSingleValueEvent(object : ValueEventListener {
//                     override fun onDataChange(dataSnapshot: DataSnapshot) {
//                         //for ((index, value) in getbarcodeDB.withIndex()) {
//                         //  num = index
//                         for (datas in dataSnapshot.children) {
//
//
//                             getpriceDB_detail = datas.child("0/${getbarcodeDB[num2!!]}/price").value.toString()
//                             getimageDB_detail = datas.child("0/${getbarcodeDB[num2!!]}/image").value.toString()
//
//
//                         }
//
//                         priceListprice.add(getpriceDB_detail)
//
//                        // product.add(Product(getpriceDB_detail, getimageDB_detail))
//                         view_pager2.adapter = adapter
//                         view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//
//
//
//                         Log.v(
//                                 VisionProcessorBase.MANUAL_TESTING_LOG,
//                                 "////////////[[[[price]]]]]]////////////// ${priceListprice},"
//
//
//                         )
//
//                     }
//
//
//                     //}
//
//                     override fun onCancelled(databaseError: DatabaseError) {}
//                 })
//
//        }





    private fun recommentfirebase() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product").child("barcode")
        refUsers.orderByChild("price").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val total = dataSnapshot.childrenCount.toInt()
                categoryList.add("${category_detail2.text}")
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
                            getstatusDB_detail = datas.child("status").value.toString()
                            getquantityDB_detail = datas.child("quantity").value.toString()
                            getcategoryDB_detail = datas.child("category").value.toString()


                            product.add(Product(getpriceDB_detail, getimageDB_detail, getnameDB_detail, getstatusDB_detail, getquantityDB_detail, getcategoryDB_detail))
                            view_pager2.adapter = adapter
                        }
                    }
                }
                view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                val indicator = findViewById<CircleIndicator3>(R.id.indicator)
                indicator.setViewPager(view_pager2)
                Log.v(
                        VisionProcessorBase.MANUAL_TESTING_LOG,
                        "////////////[[[[price]]]]]]////////////// ${categoryList},"


                )
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

