package com.example.workshop1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.google.firebase.database.*
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import kotlinx.android.synthetic.main.activity_product_recyclerview.*
import java.util.*
import kotlin.collections.ArrayList


class Showproduct : AppCompatActivity(), BarcodeAdapter.OnBarcodeClickListner {

    var getbarcode: ArrayList<String> = ArrayList()

    var num: Int? = null
    var nameDB: String = ""
    var priceDB: String = ""
    var quantityDB: String = ""
    var statusDB: String = ""
    var imageDB:String = ""
    var categoryDB:String = ""
    var idDB:String = ""

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

       Connectfirebase()


        /**---------------------------------------------------------------------------------------------------*/
        val  bottomnavigationView: ChipNavigationBar = findViewById(R.id.tabbar)

        //bottomnavigationView.selectedItemId = R.id.home
        bottomnavigationView.setItemSelected(R.id.list,true);
        bottomnavigationView.setOnItemSelectedListener(object:
            ChipNavigationBar.OnItemSelectedListener{
            override fun onItemSelected(id: Int) {
                if (id == R.id.home){
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    if (bundle != null) {
                        intent.putExtra("barcodemain", getbarcode)
                    }
                    startActivity(intent)
                    //startActivity(Intent(applicationContext, MainActivity::class.java))
//
                }
                if (id == R.id.star){
                    val i = Intent(this@Showproduct, StarList::class.java)
                    if (bundle != null) {
                        i.putExtra("barcodestar", getbarcode)
                    }
                    startActivity(i)
                    //startActivity(Intent(applicationContext, StarList::class.java))
//
                }
                if (id == R.id.scanbarcode){
                    val i = Intent(this@Showproduct, StillImageActivity::class.java)
                    if (bundle != null) {
                        i.putExtra("barcodescan", getbarcode)
                    }
                    startActivity(i)
                   // startActivity(Intent(applicationContext, StillImageActivity::class.java))
//
                }
                else{
                    bottomnavigationView.setItemSelected(R.id.list,true);
                }
            }
        })


        if (savedInstanceState != null) {

            getbarcode = savedInstanceState.getStringArrayList("savebarcode") as ArrayList<String>
            Log.v(
                    VisionProcessorBase.MANUAL_TESTING_LOG,
                    "////////////[[[[USE]]]]]]////////////// ${getbarcode},"

            )

        }







    }

       private fun Connectfirebase() {
           var refUsers: DatabaseReference? = null
           refUsers = FirebaseDatabase.getInstance().reference.child("Product").child("barcode")
           refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
               override fun onDataChange(dataSnapshot: DataSnapshot) {
                   //val num = dataSnapshot.childrenCount
                   for (datas in dataSnapshot.children) {
                       idDB = datas.child("id").value.toString()

                       var filterbarcodeid = getbarcode.any { it == idDB }

                       Log.v(
                               VisionProcessorBase.MANUAL_TESTING_LOG,
                               "////////////[[[[barcode_Connect]]]]]]////////////// ${getbarcode},"

                       )
                       if (filterbarcodeid.equals(true)) {
                           nameDB = datas.child("name").value.toString()
                           priceDB = datas.child("price").value.toString()
                           quantityDB = datas.child("quantity").value.toString()
                           statusDB = datas.child("status").value.toString()
                           imageDB = datas.child("image").value.toString()
                           categoryDB = datas.child("category").value.toString()


                           users.add(
                                   User(
                                           "" + nameDB,
                                           "" + priceDB,
                                           "$quantityDB",
                                           "" + statusDB,
                                           "" + imageDB,
                                           "" + categoryDB


                                   )
                           )

                           recyclerView.adapter = adapter
                       }
                       Log.v(
                               VisionProcessorBase.MANUAL_TESTING_LOG,
                               "////////////[[[[filter1]]]]]]////////////// ${filterbarcodeid},"

                       )


//DB #1 :: pepsi , id=> 890

//SCAN :: array of [1234567890, 1234567892, 1234567893, 1234567891, 1234567896, 1234567895]

//                       val filter = getbarcode.filter {
//                           it == nameDB
                   }
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("savebarcode",getbarcode)


    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        //get value
        savedInstanceState.getStringArrayList("savebarcode")
        super.onRestoreInstanceState(savedInstanceState)

    }


}





