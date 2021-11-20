package com.example.test_eklavya.mentee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.eklavyafrontend.R
import com.example.eklavyafrontend.mentee.MentorsInfo


class MenteeMainAdapter(private val listener: MentorClicked): RecyclerView.Adapter<MentorsViewHolder>(){
    private val items: ArrayList<MentorsInfo> = ArrayList()
    //private lateinit var btnConnect:Button
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorsViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mentor, parent, false)
        val viewHolder = MentorsViewHolder(view)
        /*btnConnect = view.findViewById(R.id.btnConnect)

        btnConnect.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }*/
        return viewHolder
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MentorsViewHolder, position: Int) {
        val currentItem = items[position]
        holder.nameView.text = "${currentItem.name}"
        holder.companydesignationView.text = "${currentItem.designation} at ${currentItem.company} "
        holder.fieldView.text = "Field:${currentItem.field}"
        holder.btnView.setOnClickListener {
            listener.onItemClicked(currentItem)
            holder.btnView.visibility = View.GONE
        }
    }
    fun updateMentors(updatedMentors: ArrayList<MentorsInfo>) {
        items.clear()
        items.addAll(updatedMentors)
        notifyDataSetChanged()
    }

}
class MentorsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameView:TextView = itemView.findViewById(R.id.tvName)
    val companydesignationView:TextView = itemView.findViewById(R.id.tvCompanyDesignation)
    val fieldView:TextView = itemView.findViewById(R.id.tvField)
    val btnView:Button = itemView.findViewById(R.id.btnConnect)
}

interface MentorClicked {
    fun onItemClicked(item:MentorsInfo)
}