package com.binarybeats.petconnect


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageAdapter(private val context: Context, private val list: List<MessageModel>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val senderImage: ImageView = itemView.findViewById(R.id.senderImage)
        val image: ImageView = itemView.findViewById(R.id.imagesend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.layout_reciever_message, parent, false)
        return MessageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = list[position]

        if (!message.imageUrl.isNullOrEmpty()) {
            // If it's an image message
            holder.messageText.visibility = View.GONE // Hide messageText TextView
            holder.image.visibility = View.VISIBLE // Show image ImageView
            // Load the image using Glide
            Glide.with(context)
                .load(message.imageUrl)
                .into(holder.image)
        } else {
            // If it's a text message
            holder.messageText.visibility = View.VISIBLE // Show messageText TextView
            holder.messageText.text = message.message // Set text message
            holder.image.visibility = View.GONE // Hide image ImageView
        }

        val senderId = message.senderId ?: ""
        loadSenderImage(senderId, holder.senderImage)
        holder.itemView.setOnClickListener(){

        }
    }


    private fun loadSenderImage(senderId: String, imageView: ImageView) {
        FirebaseDatabase.getInstance().getReference("users").child(senderId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userData = snapshot.getValue(UserData::class.java)
                        userData?.let {
                            Glide.with(context)
                                .load(it.image)
                                .placeholder(R.drawable.ahmadpicmine)
                                .into(imageView)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }

    fun addMessage(message: MessageModel) {
        val newList = ArrayList(list)
        newList.add(message)
        updateMessages(newList)
    }

    fun updateMessages(messages: List<MessageModel>) {
        (list as ArrayList).clear()
        (list as ArrayList).addAll(messages)
        notifyDataSetChanged()
    }
}