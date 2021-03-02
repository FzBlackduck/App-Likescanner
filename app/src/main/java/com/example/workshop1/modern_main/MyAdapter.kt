package com.example.workshop1.modern_main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R


class MyAdapter(private val myListList: List<MyList>, private val ct: Context, var clickListner:  MainClickListner) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.activirt_modurn_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val myList = myListList[position]
        //holder.imageView.setImageDrawable(ct.resources.getDrawable(myList.image))
        holder.bindItems(myListList[position],clickListner)

    }

    override fun getItemCount(): Int {
        return myListList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(myList: MyList, action2:MainClickListner) {

            var imageView = itemView.findViewById(R.id.myimage) as ImageView
            val titlename = itemView.findViewById(R.id.titlename) as TextView

            imageView.setImageDrawable(ct.resources.getDrawable(myList.image))
            titlename.text = myList.titlename

            itemView.setOnClickListener{
                action2.onClick(myList,adapterPosition)
            }
        }
    }
    interface MainClickListner {
        fun onClick(myList: MyList, position: Int) {

        }
    }
}