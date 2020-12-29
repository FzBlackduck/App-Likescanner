package com.example.CircleIndicator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R
import com.squareup.picasso.Picasso

class DataAdapter(val dataModelList: List<DataModel>) : RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.activity_list_detailproduct,p0,false))
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val dataModel = dataModelList[p1]
        p0.textprice.text = dataModel.price

        Picasso.get()
                .load(dataModel.image)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(p0.imageView)
    }

    override fun getItemCount(): Int {
        return dataModelList.size
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textprice: TextView = itemView.findViewById(R.id.price_list_detail)
    var imageView: ImageView

    init {
        imageView = itemView.findViewById(R.id.image_list_detail)
    }
}