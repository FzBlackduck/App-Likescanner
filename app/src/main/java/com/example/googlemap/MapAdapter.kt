package com.example.googlemap

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.compare.CompareActivity
import com.example.workshop1.R

class MapAdapter(private val mapList: List<MapList>, private val ct: Context, var clickListner:  MapClicklistner) : RecyclerView.Adapter<MapAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.activity_maplist, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindItems(mapList[position],clickListner)

        }

        override fun getItemCount(): Int {
            return mapList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(map: MapList, action2:MapClicklistner) {

                var imageView = itemView.findViewById(R.id.imagestore) as ImageView
                val titlename = itemView.findViewById(R.id.namestore) as TextView
                val distance = itemView.findViewById(R.id.distancestore) as TextView
                val pid = itemView.findViewById(R.id.storeid) as TextView

               // imageView.setImageDrawable(ct.resources.getDrawable(map.imagestore))
                titlename.text = map.namestore
                distance.text = map.distance
                pid.text = map.storeid
                imageView.setImageDrawable(ct.resources.getDrawable(map.imagestore))

                itemView.setOnClickListener{
                    action2.onClick(map,adapterPosition)
                }
            }
        }
        interface MapClicklistner {
            fun onClick(map: MapList, position: Int) {

            }
        }
    }


