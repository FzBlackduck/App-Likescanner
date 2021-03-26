package com.example.compare

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workshop1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.like.LikeButton
import com.like.OnLikeListener
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
            val namelist  = itemView.findViewById(R.id.namelist_id_s) as TextView
            val price  = itemView.findViewById(R.id.price2_s) as TextView
            val price2  = itemView.findViewById(R.id.price_s) as TextView
            val result  = itemView.findViewById(R.id.result_s) as TextView
            var image = itemView.findViewById<View>(R.id.image_s) as ImageView
            val star = itemView.findViewById<LikeButton>(R.id.star_expensive)
            /**-------------check like-----------*/
            var firebaseUser: FirebaseUser? = null
            var refUsers: DatabaseReference? = null
            firebaseUser = FirebaseAuth.getInstance().currentUser
            refUsers = FirebaseDatabase.getInstance().reference.child("Account")
                    .child(firebaseUser!!.uid)
                    .child("starlist")
                    .child(""+expensive.namelist)
                    .child("${expensive.nameproduct}")
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
                            .child("" + expensive.namelist)
                            .child("${expensive.nameproduct}")
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
                            .child("" + expensive.namelist)
                            .child("${expensive.nameproduct}")

                    refUsers!!.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.ref.removeValue()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })

                }
            })
            nameproduct.text = expensive.nameproduct
            name.text = expensive.name
            namelist.text = expensive.namelist
            price.text = expensive.price
            price2.text = expensive.price2
            result.text = expensive.result

            Picasso.get()
                    .load("" + expensive.image)
                    .into(image)


        }

    }



}
