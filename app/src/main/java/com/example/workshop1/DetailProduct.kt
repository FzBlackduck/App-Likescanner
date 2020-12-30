package com.example.workshop1

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.CircleIndicator.Product
import com.example.CircleIndicator.ViewPagerAdapter
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detailproduct.*
import kotlinx.android.synthetic.main.activity_list_detailproduct.*
import kotlinx.android.synthetic.main.activity_product_list.*
import me.relex.circleindicator.CircleIndicator3


class DetailProduct : AppCompatActivity() {

    var num:Int? = null
    var savenum:Int? = null
    //private var priceList = mutableListOf<String>()
    //private var imagesList = mutableListOf<Int>()

    var priceList55: ArrayList<String> = ArrayList()

    var getcountDB: ArrayList<String> = ArrayList()
    var sizecount : Int? = null

    ////////////rootDB
    var getbarcodeDB: ArrayList<String> = ArrayList()
    var getbarcodeDB2:String = ""
    /////////////
    var nameDB_detail: String = ""
    var priceDB_detail: String = ""
    var quantityDB_detail: String = ""
    var statusDB_detail: String = ""
    var imageDB_detail:String = ""
    var categoryDB_detail:String = ""

    val product = ArrayList<Product>()
    val adapter = ViewPagerAdapter(product)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailproduct)

        name_detail.text = getIntent().getStringExtra("name_detail")
        price_detail.text = getIntent().getStringExtra("price_detail")
        status_detail.text = getIntent().getStringExtra("status_detail")
        quantity_detail.text = getIntent().getStringExtra("quantity_detail")
        category_detail.text = getIntent().getStringExtra("category_detail")
        val input = getIntent().getStringExtra("image_detail")
        Picasso.get()
                .load("" + input)
                .into(image_detail)

        /**------ slide view -----*/

        countDB()
        rootbarcode()
        getitemDB()
        //postToList()
        view_pager2.adapter = adapter
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(view_pager2)
    }


    /**------ count limit barcode -----*/
    private fun countDB() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (datas in dataSnapshot.children) {
                            getcountDB = datas.child("${1}").value as ArrayList<String>

                        for (i in getcountDB.indices) {
                             savenum = i
                        }

                        Log.v(
                            VisionProcessorBase.MANUAL_TESTING_LOG,
                            "////////////[[[[count]]]]]]////////////// ${getcountDB}," +
                                    "${savenum?.plus(1)}"
                        )
                    }


            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }




    /**------ GetDB find barcode and ADD to ArrayList<>  -----*/

    private fun rootbarcode() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in getcountDB.indices) {
                    for (datas in dataSnapshot.children) {
                        getbarcodeDB2 = datas.child("${1}/${i}").value.toString()
                        getbarcodeDB.add("" + getbarcodeDB2)
                    }

                    Log.v(
                        VisionProcessorBase.MANUAL_TESTING_LOG,
                        "////////////[[[[b]]]]]]////////////// $getbarcodeDB"
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    /**------ find price or image for ArrayList<> -----*/

    private fun getitemDB() {
        var refItem: DatabaseReference? = null
        refItem = FirebaseDatabase.getInstance().reference.child("Product")
        //refUsers.orderByChild("price")
        refItem.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for ((index, value) in getbarcodeDB.withIndex()) {
                    num = index
                    for (datas in dataSnapshot.children) {
                        // nameDB_detail = datas.child("0/${getbarcodeDB[num!!]}/name").value.toString()
                        priceDB_detail = datas.child("0/${getbarcodeDB[num!!]}/price").value.toString()
                        // quantityDB_detail = datas.child("0/${getbarcodeDB[num!!]}/quantity").value.toString()
                        // statusDB_detail = datas.child("0/${getbarcodeDB[num!!]}/status").value.toString()
                        imageDB_detail = datas.child("0/${getbarcodeDB[num!!]}/image").value.toString()
                        // categoryDB_detail = datas.child("0/${getbarcodeDB[num!!]}/category").value.toString()

                        priceList55.add("" + priceDB_detail)
                        product.add(Product(priceDB_detail, imageDB_detail))
                    }
                    view_pager2.adapter = adapter
                    view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                }

                Log.v(
                    VisionProcessorBase.MANUAL_TESTING_LOG,
                    "////////////[[[[price]]]]]]////////////// $priceList55"
                )


            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}
