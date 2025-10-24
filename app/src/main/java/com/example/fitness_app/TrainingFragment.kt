package com.example.fitness_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_app.adpters.ExerciseAdapter
import com.example.fitness_app.dailogs.ExerciseTimerDialogFragment
import com.example.fitness_app.model.ExerciseModel

class TrainingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerExercises)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val exerciseList = getExerciseList()

        adapter = ExerciseAdapter(exerciseList) { exercise ->
            openExerciseDialog(exercise)
        }

        recyclerView.adapter = adapter
    }

    private fun getExerciseList(): List<ExerciseModel> {
        return listOf(
            ExerciseModel("تمرين شد عضلات البطن", R.drawable.exercise_bicycle),
            ExerciseModel("تمرين القرفصاء", R.drawable.exercise_squat),
            ExerciseModel("تمرين الضغط", R.drawable.exercise_pushup),
            ExerciseModel("تمرين البلانك", R.drawable.exercise_plank),
            ExerciseModel("تمرين رفع الساقين", R.drawable.exercise_leg_raise),
            ExerciseModel("تمرين الانحناء الجانبي", R.drawable.exercise_side_bend),
            ExerciseModel("تمرين الجسر", R.drawable.exercise_bridge),
            ExerciseModel("تمرين الدراجة", R.drawable.exercise_bicycle),
            ExerciseModel("تمرين الطعنات", R.drawable.exercise_lunges),
            ExerciseModel("تمرين العقلة", R.drawable.exercise_pullup)
        )
    }

    private fun openExerciseDialog(exercise: ExerciseModel) {
        val dialog = ExerciseTimerDialogFragment(
            exerciseName = exercise.name,
            exerciseImageRes = exercise.imageResId
        )
        dialog.show(parentFragmentManager, "ExerciseTimerDialog")
    }
}
