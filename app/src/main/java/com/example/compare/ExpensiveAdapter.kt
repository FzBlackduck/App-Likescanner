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

class ExpensiveAdapter(private val expensiveList: ArrayList<Expensive>) : RecyclerView.Adapter<ExpensiveAdapter.ViewHolder>() {



    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensiveAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
                R.layout.activity_compare_list_expensive,
                parent,
                false
        )
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ExpensiveAdapter.ViewHolder, position: Int) {
        holder.bindItems(expensiveList[position])


    }


    override fun getItemCount(): Int {
        return expensiveList.size
    }





    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("WrongViewCast")


        fun bindItems(expensive: Expensive) {
            val nameproduct = itemView.findViewById(R.id.name_product_s) as TextView
            val name  = itemView.findViewById(R.id.name_id_s) as TextView
            val name2  = itemView.findViewById(R.id.name2_id_s) as TextView
            val price  = itemView.findViewById(R.id.price_s) as TextView
            val price2  = itemView.findViewById(R.id.price2_s) as TextView
            val result  = itemView.findViewById(R.id.result_s) as TextView
            var image = itemView.findViewById<View>(R.id.image_s) as ImageView


            nameproduct.text = expensive.nameproduct
            name.text = expensive.name
            name2.text = expensive.name2
            price.text = expensive.price
            price2.text = expensive.price2
            result.text = expensive.result

            Picasso.get()
                    .load("" + expensive.image)
                    .into(image)


        }

    }



}
