package com.example.CircleIndicator


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Bitmap
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.User
import com.example.workshop1.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_list_detailproduct.view.*
import kotlinx.android.synthetic.main.activity_product_dialog.view.*
import kotlinx.android.synthetic.main.activity_product_list.view.*


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
            itemimage.setOnClickListener { v: View ->
                val position = adapterPosition
                Toast.makeText(itemView.context, "Tou click on item ${position + 1}", Toast.LENGTH_SHORT).show()
                dialog(productList[position])
            }

        }

        fun dialog(product: Product) {
            val mDialogView = LayoutInflater.from(itemView.context).inflate(R.layout.activity_product_dialog, null)
            //AlertDialogBuilder
            /**Dialog*/
            val di_price: TextView = mDialogView.findViewById(R.id.dialog_price)
            val di_name: TextView = mDialogView.findViewById(R.id.dialog_name)
            val di_quantity: TextView = mDialogView.findViewById(R.id.dialog_quantity)
            val di_status: TextView = mDialogView.findViewById(R.id.dialog_status)
            val di_category: TextView = mDialogView.findViewById(R.id.dialog_category)
            var di_image: ImageView = mDialogView.findViewById(R.id.dialog_image)

            /**ListDetail*/
//            val di_price2: TextView = itemView.findViewById(R.id.price_list_detail)
//            val di_name2: TextView = itemView.findViewById(R.id.name_list_detail)
//            val di_quantity2: TextView = itemView.findViewById(R.id.quantity_list_detail)
//            val di_status2: TextView = itemView.findViewById(R.id.status_list_detail)
//            val di_category2: TextView = itemView.findViewById(R.id.category_list_detail)
//            var di_image2: ImageView = itemView.findViewById(R.id.image_list_detail)
//            di_price.text = di_price2.text
//            di_name.text = di_name2.text
//            di_status.text = di_status2.text
//            di_quantity.text = di_quantity2.text
//            di_category.text = di_category2.text
            //di_image = di_image2
              di_price.text = product.price
              di_name.text = product.name
              di_quantity.text = product.quantity
              di_status.text = product.status
              di_category.text = product.category
            Picasso.get()
                    .load("" + product.image)
                    .into(di_image)


            val mBuilder = AlertDialog.Builder(itemView.context)
                    .setView(mDialogView)


            //show dialog
            mBuilder.show()
            //
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