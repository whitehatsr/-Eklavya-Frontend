package com.example.eklavyafrontend.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eklavyafrontend.R
class ChatRVAdapter(var userId: String) : RecyclerView.Adapter<ChatViewHolder>() {
    private val items: ArrayList<ChatInfo> = ArrayList()

    /*fun checkCurr(): Int {
        if (items[pos].from == userId) {
            return 1
        }
        return 0
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        /*if (checkCurr() == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.sent_message_container, parent, false)
            val viewHolder = ChatViewHolder(view)
            return viewHolder
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.received_message_container, parent, false)
            val viewHolder = ChatViewHolder(view)
            return viewHolder
        }*/
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.sent_message_container, parent, false)
        val viewHolder = ChatViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentItem = items[position]
        //pos = position


        if (currentItem.from == userId) {
            holder.receivedMessageView.visibility = View.GONE
            holder.sentMessageView.text = currentItem.message
            holder.sentMessageView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            //holder.messageView.textColors = Color.parseColor("#00FF00")
//            holder.sentMessageView.setTextColor(Color.parseColor("#00FF00"))
        }
        else {
            holder.sentMessageView.visibility = View.GONE
            holder.receivedMessageView.text = currentItem.message
            holder.receivedMessageView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
//            holder.receivedMessageView.setTextColor(Color.parseColor("#FF6600"))
        }
        if (currentItem.to == userId) {
            holder.sentMessageView.visibility = View.GONE
            holder.receivedMessageView.text = currentItem.message
            holder.receivedMessageView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
//            holder.receivedMessageView.setTextColor(Color.parseColor("#FF6600"))
        }
        else {
            holder.receivedMessageView.visibility = View.GONE
            holder.sentMessageView.text = currentItem.message
            holder.sentMessageView.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            //holder.messageView.textColors = Color.parseColor("#00FF00")
//            holder.sentMessageView.setTextColor(Color.parseColor("#00FF00"))
        }

        /*if (currentItem.from == userId) {
            holder.messageView.setTextColor(Color.parseColor("#00FF00"))
        }
        else {
            holder.messageView.setTextColor(Color.parseColor("#FF6600"))
        }
        if (currentItem.to == userId) {
            holder.messageView.setTextColor(Color.parseColor("#FF6600"))
        }
        else {
            holder.messageView.setTextColor(Color.parseColor("#00FF00"))
        }*/

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateChat(updatedChats: ArrayList<ChatInfo>) {
        items.clear()
        items.addAll(updatedChats)
        notifyDataSetChanged()
    }

    fun addChat(addChat: ChatInfo) {
        items.add(addChat)
        notifyDataSetChanged()
    }
}

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val sentMessageView: TextView = itemView.findViewById(R.id.textMessageSent)
    val receivedMessageView: TextView = itemView.findViewById(R.id.textMessageReceived)
}