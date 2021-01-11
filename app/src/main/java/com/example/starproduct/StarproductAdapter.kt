package com.example.starproduct

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.User
import com.example.workshop1.R
import com.squareup.picasso.Picasso

class StarAdapter(private val starList: ArrayList<Star> ) : RecyclerView.Adapter<StarAdapter.ViewHolder>() {



    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_product_list,
            parent,
            false
        )
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: StarAdapter.ViewHolder, position: Int) {
        holder.bindItems(starList[position])


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return starList.size
    }



    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("WrongViewCast")


        fun bindItems(star: Star) {
            val textViewName = itemView.findViewById(R.id.namestar) as TextView
            val textViewPrice  = itemView.findViewById(R.id.pricestar) as TextView
            var setimageview = itemView.findViewById<View>(R.id.imageView1star) as ImageView


            textViewName.text = star.name
            textViewPrice.text = star.price


            Picasso.get()
                .load("" + star.image)
                .into(setimageview)


        }


    }


}