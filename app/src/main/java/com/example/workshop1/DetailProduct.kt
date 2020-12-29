package com.example.workshop1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.CircleIndicator.ViewPagerAdapter
import com.example.barcodescanner.User
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detailproduct.*
import kotlinx.android.synthetic.main.activity_list_detailproduct.*
import kotlinx.android.synthetic.main.activity_product_list.*
import me.relex.circleindicator.CircleIndicator3


class DetailProduct : AppCompatActivity() {


    private var priceList = mutableListOf<String>()
    private var imagesList = mutableListOf<Int>()
    val products = arrayListOf<Product>()


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


        //Con()
        postToList()
        view_pager2.adapter = ViewPagerAdapter(priceList, imagesList)
        view_pager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(view_pager2)
    }

    private fun addToList(price: String, image: Int) {
        priceList.add(price)
        imagesList.add(image)
    }


    /** -------------------------------- */
    private fun postToList() {

        for (i in 1..10) {
            addToList("$i", R.mipmap.ic_launcher_round)

        }
    }


    private  fun Con(){
        val ref = FirebaseDatabase.getInstance().getReference("Products/0")
        ref.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (productSnapshot in dataSnapshot.children) {
                val product = productSnapshot.getValue(Product::class.java)
                products.add(product!!)
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException();
            }

        })
    }


}

