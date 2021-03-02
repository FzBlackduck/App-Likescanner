package com.example.compare

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R
import com.squareup.picasso.Picasso

class CheaperAdapter(private val cheaperList: ArrayList<Cheaper>) : RecyclerView.Adapter<CheaperAdapter.ViewHolder>() {



    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheaperAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.activity_compare_list_cheaper,
                parent,
                false
        )
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: CheaperAdapter.ViewHolder, position: Int) {
        holder.bindItems(cheaperList[position])


    }


    override fun getItemCount(): Int {
        return cheaperList.size
    }





    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("WrongViewCast")


        fun bindItems(cheaper: Cheaper) {
            val nameproduct = itemView.findViewById(R.id.name_product_c) as TextView
            val name  = itemView.findViewById(R.id.name_id_c) as TextView
            val name2  = itemView.findViewById(R.id.name2_id_c) as TextView
            val price  = itemView.findViewById(R.id.price_c) as TextView
            val price2  = itemView.findViewById(R.id.price2_c) as TextView
            val result  = itemView.findViewById(R.id.result_c) as TextView
            var image = itemView.findViewById<View>(R.id.image_c) as ImageView


            nameproduct.text = cheaper.nameproduct
            name.text = cheaper.name
            name2.text = cheaper.name2
            price.text = cheaper.price
            price2.text = cheaper.price2
            result.text = cheaper.result

            Picasso.get()
                    .load("" + cheaper.image)
                    .into(image)


        }

    }



}
