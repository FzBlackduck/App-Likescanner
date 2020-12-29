package com.example.CircleIndicator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.barcodescanner.BarcodeAdapter
import com.example.barcodescanner.User
import com.example.workshop1.R
////////////////////////////////
class ViewPagerAdapter(private var price: List<String>,
                       private var image: List<Int>): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>()
{
    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val itemimage: ImageView = itemView.findViewById((R.id.image_list_detail))
        val itemprice: TextView = itemView.findViewById((R.id.price_list_detail))



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
        return price.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.itemprice.text = price[position]
        holder.itemimage.setImageResource(image[position])
    }

}