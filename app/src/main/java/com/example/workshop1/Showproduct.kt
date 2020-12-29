package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CircleIndicator.MainDatail
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_recyclerview.*


class Showproduct : AppCompatActivity(), BarcodeAdapter.OnBarcodeClickListner {

    var getbarcode: ArrayList<String> = ArrayList()
    var num: Int? = null
    var nameDB: String = ""
    var priceDB: String = ""
    var quantityDB: String = ""
    var statusDB: String = ""
    var imageDB:String = ""
    var categoryDB:String = ""

    ////
    val users = ArrayList<User>()
    val adapter = BarcodeAdapter(users,this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_recyclerview)

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //recyclerView.adapter = BarcodeAdapter(arrayListOf<User>(User("xx", "", "", ""), User("yy", "", "", "")))
        //recyclerView.adapter!!.notifyDataSetChanged()

        val bundle = intent.extras
        if (bundle != null) {
           getbarcode = bundle.getStringArrayList("barcode")!!

        }

        for ((index, value) in getbarcode.withIndex()) {
               num = index
               Connectfirebase(num)

           }

    }

       private fun Connectfirebase(num: Int?) {
        var refUsers: DatabaseReference? = null

        refUsers = FirebaseDatabase.getInstance().reference.child("Product")
        refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (datas in dataSnapshot.children) {
                        nameDB = datas.child("${getbarcode[num!!]}/name").value.toString()
                        priceDB = datas.child("${getbarcode[num!!]}/price").value.toString()
                        quantityDB = datas.child("${getbarcode[num]!!}/quantity").value.toString()
                        statusDB = datas.child("${getbarcode[num!!]}/status").value.toString()
                        imageDB = datas.child("${getbarcode[num!!]}/image").value.toString()
                        categoryDB = datas.child("${getbarcode[num!!]}/category").value.toString()

                    users.add(
                        User(
                            "" + nameDB,
                            "" + priceDB,
                            "" + quantityDB,
                            "" + statusDB,
                            "" + imageDB,
                                ""+categoryDB


                        )
                    )


                }

//                when (num) {
//
//                     in 0..6 -> {
//                        users.add(User(""+nameDB,""+ priceDB,""+quantityDB,""+statusDB,""+imageDB))
//
//                   }
//
//      }
                recyclerView.adapter = adapter

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
   }
/** -----------------  click list on recycler view ------------------- */
    override fun onClick(userList: User, position: Int) {
        //Toast.makeText(this, userList.name ,Toast.LENGTH_SHORT).show()

        val intent = Intent(this, DetailProduct::class.java)
        intent.putExtra("name_detail",userList.name)
        intent.putExtra("price_detail",userList.price)
        intent.putExtra("quantity_detail",userList.quantity)
         intent.putExtra("status_detail",userList.status)
        intent.putExtra("image_detail",userList.image)
       intent.putExtra("category_detail",userList.category)
        startActivity(intent)






    }


}





