package com.example.barcodescanner


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso



class BarcodeAdapter(private val userList: ArrayList<User>, var clickListner: OnBarcodeClickListner,var clickdelete: ClickdeleteListner) : RecyclerView.Adapter<BarcodeAdapter.ViewHolder>() {



        //this method is returning the view for each item in the list
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarcodeAdapter.ViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(
                    R.layout.activity_product_list,
                    parent,
                    false
            )
            return ViewHolder(v)
        }

        //this method is binding the data on the list
        override fun onBindViewHolder(holder: BarcodeAdapter.ViewHolder, position: Int) {
            holder.bindItems(userList[position],clickListner,clickdelete)
            //holder.initialize(userList[position],clickListner)

        }

        //this method is giving the size of the list
        override fun getItemCount(): Int {
            return userList.size
        }




    //the class is hodling the list view
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @SuppressLint("WrongViewCast")

           // var barcode_name = itemView.name
           // var barcode_price = itemView.price
           // var barcode_status = itemView.status
           // var barcode_quanutity = itemView.quantity
           // var barcode_image = itemView.imageView1


            fun bindItems(user: User, action:OnBarcodeClickListner, action2: ClickdeleteListner) {
                val textViewName = itemView.findViewById(R.id.name) as TextView
                val textViewPrice  = itemView.findViewById(R.id.price) as TextView
               // val textViewQuantity = itemView.findViewById(R.id.quantity) as TextView
               // val textViewStatus  = itemView.findViewById(R.id.status) as TextView
                var setimageview = itemView.findViewById<View>(R.id.imageView1) as ImageView
               // val textViewCategory  = itemView.findViewById(R.id.category) as TextView

                textViewName.text = user.name
                textViewPrice.text = user.price
                //textViewQuantity.text = user.quantity
                //textViewStatus.text = user.status
                //textViewCategory.text = user.category


                Picasso.get()
                    .load("" + user.image)
                    .into(setimageview)

                itemView.setOnClickListener{
                    action.onClick(user,adapterPosition)
                }

                val del = itemView.findViewById(R.id.Delete) as Button
                del.setOnClickListener{
                    action2.onClickdelete(user,adapterPosition)

                }
            }

             /*fun initialize(userList: User, action:OnBarcodeClickListner){
                 barcode_name.text = userList.name
                 barcode_price.text = userList.price
                 barcode_quanutity.text = userList.quantity
                 barcode_status.text = userList.status
                 //barcode_image.setImageResource(userList.image.toInt())

                 itemView.setOnClickListener{
                     action.onClick(userList,adapterPosition)
                 }
             }*/

        }

        interface OnBarcodeClickListner {
        fun onClick(userList: User,position: Int) {

        }
        }
    interface ClickdeleteListner {
        fun onClickdelete(userList: User,position: Int) {

        }
        }

    }


