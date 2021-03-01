package com.example.CircleIndicator

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_productlist.*


class ProductAdapter(private val productList: ArrayList<Product>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var firebaseUser: FirebaseUser? = null
    val promotion = 5
    var store : String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.activity_list_showdetail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(productList[position])

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        fun bindItems(product: Product) {
            val itemimage: ImageView = itemView.findViewById((R.id.image_list_detail2))
            val productName = itemView.findViewById(R.id.name_list_detail2) as TextView
            val productPrice = itemView.findViewById(R.id.price_list_detail2) as TextView
            var discount = itemView.findViewById<TextView>(R.id.discount_detail)


            store = product.storeid
            discount.text = "฿"+product.price
            productName.text = product.name
            productPrice.text = "฿"+(product.price.toInt()-promotion)
            Picasso.get()
                    .load("" + product.image)
                    .into(itemimage)
        }

        val itemimage: ImageView = itemView.findViewById((R.id.image_list_detail2))

        init {
            itemimage.setOnClickListener { v: View ->
                val position = adapterPosition
                // Toast.makeText(itemView.context, "Tou click on item ${position + 1}", Toast.LENGTH_SHORT).show()
                dialog(productList[position])

            }
        }

            fun dialog(product: Product) {
                //val mDialogView = LayoutInflater.from(itemView.context).inflate(R.layout.new_activity_productlist, null)
                //AlertDialogBuilder
                val dialog = Dialog(itemView.context)
                dialog.setContentView(R.layout.activity_productlist)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                /**Dialog*/
                val di_price: TextView = dialog.findViewById(R.id.price)
                val di_name: TextView = dialog.findViewById(R.id.name)
                var di_image: ImageView = dialog.findViewById(R.id.image)
                var di_discont: TextView = dialog.findViewById(R.id.discount)
                var di_payment: TextView = dialog.findViewById(R.id.payment)

                /**ListDetail*/

                di_price.text = "฿"+product.price
                di_name.text = product.name
                Picasso.get()
                        .load("" + product.image)
                        .into(di_image)
                di_discont.text = "฿$promotion"
                di_payment.text = "฿${product.price.toInt()-promotion}"

                dialog.close.setOnClickListener {
                    dialog.dismiss()
                }
                /**-------------check like-----------*/
                var refUsers: DatabaseReference? = null
                firebaseUser = FirebaseAuth.getInstance().currentUser
                refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                        .child(firebaseUser!!.uid)
                        .child("starlist")
                        .child(""+store)
                        .child("${di_name.text}")
                refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        //for (datas in dataSnapshot.children) {
                        val star = dataSnapshot.child("star").value.toString()
                        if (star == "Show") {
                            dialog.star_button2.isLiked = true
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
                /**----------------------------------------------------------------------------------------------------------------------*/
//                val mBuilder = AlertDialog.Builder(itemView.context, R.style.DialogAnimation)
//                        .setView(mDialogView)
//                val mAlertDialog = mBuilder.show()
//                mDialogView.cancel.setOnClickListener {
//                    mAlertDialog.dismiss()
//                }


                dialog.star_button2.setOnLikeListener(object : OnLikeListener {
                    override fun liked(likeButton: LikeButton) {
                        Toast.makeText(itemView.context, "Liked!", Toast.LENGTH_SHORT).show()
                        likeButton.isLiked = true

                        firebaseUser = FirebaseAuth.getInstance().currentUser
                        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                                .child(firebaseUser!!.uid)
                                .child("starlist")
                                .child(""+store)
                                .child("${di_name.text}")
                        val userHashMap = HashMap<String, Any>()
                        //userHashMap["uid"]= firebaseUserID
                        userHashMap["star"] = "Show"
                        refUsers!!.updateChildren(userHashMap)


                    }

                    override fun unLiked(likeButton: LikeButton) {
                        Toast.makeText(itemView.context, "UnLiked!", Toast.LENGTH_SHORT).show()
                        firebaseUser = FirebaseAuth.getInstance().currentUser
                        refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                                .child(firebaseUser!!.uid)
                                .child("starlist")
                                .child(""+store)
                                .child("${di_name.text}")
                        val userHashMap = HashMap<String, Any>()
                        //userHashMap["uid"]= firebaseUserID
                        userHashMap["star"] = "unShow"
                        refUsers!!.updateChildren(userHashMap)


                    }

                })
                dialog.show()
            }


        }


}



