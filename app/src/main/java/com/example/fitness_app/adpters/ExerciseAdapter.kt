package com.example.fitness_app.adpters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_app.R
import com.example.fitness_app.model.ExerciseModel

class ExerciseAdapter(
    private val exercises: List<ExerciseModel>,
    private val onItemClick: (ExerciseModel) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        val exerciseImage: ImageView = itemView.findViewById(R.id.exerciseImage)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(exercises[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.name
        holder.exerciseImage.setImageResource(exercise.imageResId)  // لاحظ استخدمنا imageResId
    }

    override fun getItemCount(): Int = exercises.size
}
