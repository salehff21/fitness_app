package com.example.fitness_app.adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.fitness_app.R
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_app.Data.UserBasicInfo

class UserCardAdapter(private val data: List<UserBasicInfo>) : RecyclerView.Adapter<UserCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWeight: TextView = itemView.findViewById(R.id.tvwight)
        val tvHeight: TextView = itemView.findViewById(R.id.tvhight)
        val tvAge: TextView = itemView.findViewById(R.id.tvage)
        val tvWidth: TextView = itemView.findViewById(R.id.tvExerciseTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fitness_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = data[position]
        holder.tvWeight.text = "${user.weight} KG"
        holder.tvHeight.text = "${user.height} CM"
        holder.tvAge.text = "${user.age} سنة"
        holder.tvWidth.text = "${user.width} CM"
    }
}
