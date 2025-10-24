package com.example.fitness_app.dailogs
import com.example.fitness_app.Data.SQLiteHelper

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.fitness_app.R

class ExerciseTimerDialogFragment(
    private val exerciseName: String,
    private val exerciseImageRes: Int
) : DialogFragment() {
    private lateinit var dbHelper: SQLiteHelper

    private lateinit var tvTimer: TextView
    private lateinit var etNote: EditText
    private lateinit var btnStart: Button
    private lateinit var btnFinish: Button
    private lateinit var exerciseImage: ImageView
    private lateinit var exerciseTitle: TextView

    private var timer: CountDownTimer? = null
    private var totalTimeInMillis = 30 * 1000L  // 30 ثانية مثلاً مؤقت مبدئي
    private var isTimerRunning = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_exercise_timer, null)
        dialog.setContentView(view)
        dbHelper = SQLiteHelper(requireContext())

        tvTimer = view.findViewById(R.id.tvTimer)
        etNote = view.findViewById(R.id.etNote)
        btnStart = view.findViewById(R.id.btnStart)
        btnFinish = view.findViewById(R.id.btnFinish)
        exerciseImage = view.findViewById(R.id.exerciseImage)
       exerciseTitle = view.findViewById(R.id.exerciseTitle)

        // تعيين بيانات التمرين
        exerciseTitle.text = exerciseName
        exerciseImage.setImageResource(exerciseImageRes)

        updateTimerText(totalTimeInMillis)

        btnStart.setOnClickListener {
            if (!isTimerRunning) {
                startTimer()
            }
        }

        btnFinish.setOnClickListener {
            stopTimer()
            dismiss()  // يغلق الديالوج
        }

        return dialog
    }

    private fun startTimer() {
        timer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                totalTimeInMillis = millisUntilFinished
                updateTimerText(millisUntilFinished)
            }

            override fun onFinish() {
                isTimerRunning = false
                saveExerciseToDatabase()
                dismiss()

            }
        }.start()
        isTimerRunning = true
    }

    private fun stopTimer() {
        timer?.cancel()
        isTimerRunning = false
    }
    private fun saveExerciseToDatabase() {
        val db = dbHelper.writableDatabase
        val values = android.content.ContentValues().apply {
            put("name", exerciseName)
            put("duration", 30) // عدد الثواني 30 لأن التايمر كان 30 ثانية
            put("note", etNote.text.toString())
            put("date", getCurrentDate())
        }
        db.insert("completed_exercises", null, values)
        db.close()
    }
    private fun getCurrentDate(): String {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return dateFormat.format(java.util.Date())
    }

    private fun updateTimerText(millisUntilFinished: Long) {
        val seconds = millisUntilFinished / 1000
        tvTimer.text = seconds.toString()
    }
}
