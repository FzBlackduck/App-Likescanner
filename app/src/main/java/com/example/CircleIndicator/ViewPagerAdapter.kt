package com.example.CircleIndicator

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


class ViewPagerAdapter(private val productList: ArrayList<Product>): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>()
{
    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(product: Product) {
            val itemimage: ImageView = itemView.findViewById((R.id.image_list_detail))
            //var productImage2 = itemView.findViewById<View>(R.id.image_list_detail) as ImageView
            //val productName = itemView.findViewById(R.id.name) as TextView
            val productPrice  = itemView.findViewById(R.id.price_list_detail) as TextView
            //val productQuantity = itemView.findViewById(R.id.quantity) as TextView
            //val productStatus  = itemView.findViewById(R.id.status) as TextView
            //val productCategory  = itemView.findViewById(R.id.category) as TextView

            productPrice.text = product.price
            Picasso.get()
                .load("" + product.image)
                .into(itemimage)
        }



        val itemimage: ImageView = itemView.findViewById((R.id.image_list_detail))
        init{
            itemimage.setOnClickListener{v: View ->
                val position = adapterPosition
                Toast.makeText(itemView.context, "Tou click on item ${position + 1}",Toast.LENGTH_SHORT).show()

            }
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