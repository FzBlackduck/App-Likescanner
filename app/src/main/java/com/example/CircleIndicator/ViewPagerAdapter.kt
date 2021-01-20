package com.example.CircleIndicator


import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R
import com.google.firebase.database.*
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_dialog2.view.*
import kotlin.collections.ArrayList
import com.like.LikeButton as LikeButton


class ViewPagerAdapter(private val productList: ArrayList<Product>): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>()
{

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(product: Product) {
            val itemimage: ImageView = itemView.findViewById((R.id.image_list_detail))
            //var productImage2 = itemView.findViewById<View>(R.id.image_list_detail) as ImageView
            val productName = itemView.findViewById(R.id.name_list_detail) as TextView
            val productPrice = itemView.findViewById(R.id.price_list_detail) as TextView
            val productQuantity = itemView.findViewById(R.id.quantity_list_detail) as TextView
            val productStatus = itemView.findViewById(R.id.status_list_detail) as TextView
            val productCategory = itemView.findViewById(R.id.category_list_detail) as TextView

            productCategory.text = product.category
            productStatus.text = product.status
            productQuantity.text = product.quantity
            productName.text = product.name
            productPrice.text = product.price
            Picasso.get()
                    .load("" + product.image)
                    .into(itemimage)
        }


        //val di_price: String = itemView.findViewById((R.id.dialog_price))
        val itemimage: ImageView = itemView.findViewById((R.id.image_list_detail))

        init {
            itemimage.setOnClickListener { v: View->
                val position = adapterPosition
                // Toast.makeText(itemView.context, "Tou click on item ${position + 1}", Toast.LENGTH_SHORT).show()
                dialog(productList[position])
            }

        }

        fun dialog(product: Product) {
            val mDialogView = LayoutInflater.from(itemView.context).inflate(R.layout.activity_product_dialog2, null)
            //AlertDialogBuilder
            /**Dialog*/
            val di_price: TextView = mDialogView.findViewById(R.id.dialog_price1)
            val di_name: TextView = mDialogView.findViewById(R.id.dialog_name1)
            val di_quantity: TextView = mDialogView.findViewById(R.id.dialog_quantity1)
            val di_status: TextView = mDialogView.findViewById(R.id.dialog_status1)
            //val di_category: TextView = mDialogView.findViewById(R.id.dialog_category)
            var di_image: ImageView = mDialogView.findViewById(R.id.dialog_image1)

            /**ListDetail*/
            di_price.text = product.price
            di_name.text = product.name
            di_quantity.text = product.quantity
            di_status.text = product.status
            // di_category.text = product.category
            Picasso.get()
                    .load("" + product.image)
                    .into(di_image)



            var refUsers: DatabaseReference? = null
            refUsers = FirebaseDatabase.getInstance().reference.child("Product")
            refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (datas in dataSnapshot.children) {
                        val star = datas.child("${di_name.text}/star").value.toString()
                        if (star == "showstar") {
                            mDialogView.star_button.isLiked = true
                        }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }

            })
            /**----------------------------------------------------------------------------------------------------------------------*/

            val mBuilder = AlertDialog.Builder(itemView.context, R.style.DialogAnimation)
                    .setView(mDialogView)


            //show dialog

            val mAlertDialog = mBuilder.show()



            mDialogView.cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }






            mDialogView.star_button.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    Toast.makeText(itemView.context, "Liked!", Toast.LENGTH_SHORT).show()
                    likeButton.isLiked = true
                    var map = mutableMapOf<String, Any>()
                    map["star"] = "showstar"
                    var refupdate: DatabaseReference? = null
                    refupdate = FirebaseDatabase.getInstance().reference
                            .child("Product")
                            .child("subproduct")
                            .child("${di_name.text}")
                    refupdate.updateChildren(map)

                }
                override fun unLiked(likeButton: LikeButton) {
                    Toast.makeText(itemView.context, "UnLiked!", Toast.LENGTH_SHORT).show()
                    var map2 = mutableMapOf<String, Any>()
                    map2["star"] = "unshowstar"
                    var refupdate2: DatabaseReference? = null
                    refupdate2 = FirebaseDatabase.getInstance().reference
                            .child("Product")
                            .child("subproduct")
                            .child("${di_name.text}")
                    refupdate2.updateChildren(map2)




                        }

                    })

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.Pager2ViewHolder {
        return  Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_list_detailproduct, parent, false))


    }

    override fun getItemCount(): Int {
        //return price.size
        return productList.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        //holder.itemprice.text = price[position]
        //holder.itemimage.setImageResource(image[position])
        holder.bindItems(productList[position])
    }

}