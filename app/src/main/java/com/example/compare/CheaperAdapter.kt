package com.example.compare

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.starproduct.Star
import com.example.starproduct.StarproductAdapter
import com.example.workshop1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_showdetail.*

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
            val name = itemView.findViewById(R.id.name_id_c) as TextView
            val namelist = itemView.findViewById(R.id.namelist_id_c) as TextView
            val price = itemView.findViewById(R.id.price2_c) as TextView
            val price2 = itemView.findViewById(R.id.price_c) as TextView
            val result = itemView.findViewById(R.id.result_c) as TextView
            var image = itemView.findViewById<View>(R.id.image_c) as ImageView
            val star = itemView.findViewById<LikeButton>(R.id.star_cheaper)

            //action.onClick(cheaper, adapterPosition)
            var firebaseUser: FirebaseUser? = null
            var refUsers: DatabaseReference? = null

            /**-------------check like-----------*/

            firebaseUser = FirebaseAuth.getInstance().currentUser
            refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                    .child(firebaseUser!!.uid)
                    .child("starlist")
                    .child(""+cheaper.namelist)
                    .child("${cheaper.nameproduct}")
            refUsers.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val star2 = dataSnapshot.child("star").value.toString()
                    if (star2 == "Show") {
                        star.isLiked = true
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {

                }
            })





            star.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    likeButton.isLiked = true
                    //Toast.makeText(itemView.context, "Liked!", Toast.LENGTH_SHORT).show()
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                            .child(firebaseUser!!.uid)
                            .child("starlist")
                            .child("" + cheaper.namelist)
                            .child("${cheaper.nameproduct}")
                    val userHashMap = HashMap<String, Any>()
                    //userHashMap["uid"]= firebaseUserID
                    userHashMap["star"] = "Show"
                    refUsers!!.updateChildren(userHashMap)

                }

                override fun unLiked(likeButton: LikeButton) {
                    //action.onClick(cheaper, adapterPosition)
                    //Toast.makeText(itemView.context, "unLiked!", Toast.LENGTH_SHORT).show()
                    firebaseUser = FirebaseAuth.getInstance().currentUser
                    refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                            .child(firebaseUser!!.uid)
                            .child("starlist")
                            .child("" + cheaper.namelist)
                            .child("${cheaper.nameproduct}")

                    refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.ref.removeValue()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                }
            })




            nameproduct.text = cheaper.nameproduct
            name.text = cheaper.name
            namelist.text = cheaper.namelist
            price.text = cheaper.price
            price2.text = cheaper.price2
            result.text = cheaper.result

            Picasso.get()
                    .load("" + cheaper.image)
                    .into(image)


        }

    }
}


