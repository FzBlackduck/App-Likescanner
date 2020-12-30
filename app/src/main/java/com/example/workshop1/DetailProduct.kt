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


    //private var priceList = mutableListOf<String>()
    //private var imagesList = mutableListOf<Int>()

    var getbarcodeDB: ArrayList<String> = ArrayList()
    var getbarcodeDB2:String = ""

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


        rootbarcode()
        getitemDB()
        //postToList()
        view_pager2.adapter = adapter
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(view_pager2)
    }

//    private fun addToList(price: String, image: String) {
//        priceList.add(price)
//        imagesList.add(image)
//    }


    /** -------------------------------- */
//    private fun postToList() {
//
//        for (i in 1..10) {
//            addToList("$priceDB_detail", imageDB_detail)
//
//        }
//    }


    private fun rootbarcode() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                    getbarcodeDB2 = datas.child("${1}").value.toString()
                }
                getbarcodeDB.add(""+getbarcodeDB2)
                Log.v(
                        VisionProcessorBase.MANUAL_TESTING_LOG,
                        "////////////[[[[]]]]]]////////////// $getbarcodeDB"
                )
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getitemDB() {
        var refUsers: DatabaseReference? = null
        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var index = 0
                for (datas in dataSnapshot.children) {
                    nameDB_detail = datas.child("0/${getbarcodeDB[index]}/name").value.toString()
                    priceDB_detail = datas.child("0/${getbarcodeDB[index]}/price").value.toString()
                    quantityDB_detail = datas.child("0/${getbarcodeDB[index]}/quantity").value.toString()
                    statusDB_detail = datas.child("0/${getbarcodeDB[index]}/status").value.toString()
                    imageDB_detail = datas.child("0/${getbarcodeDB[index]}/image").value.toString()
                    categoryDB_detail = datas.child("0/${getbarcodeDB[index]}/category").value.toString()
                    index++
                }
                getbarcodeDB.add(""+getbarcodeDB2)
                Log.v(
                        VisionProcessorBase.MANUAL_TESTING_LOG,
                        "////////////[[[[]]]]]]////////////// $getbarcodeDB"
                )
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }









}
