package com.example.fitness_app

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitness_app.Data.SQLiteHelper
import kotlin.or

class HomeFragment : Fragment(R.layout.fragment_home), SensorEventListener {

    private lateinit var dbHelper: SQLiteHelper
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var totalSteps = 0
    private lateinit var stepCountTextView: TextView
    private var currentUserId = 1 // ثابت مؤقتًا
    private lateinit var dbHelperHoure: SQLiteHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = SQLiteHelper(requireContext())
        stepCountTextView = view.findViewById(R.id.step_count)
        val storedSteps = dbHelper.getUserSteps(currentUserId)
        totalSteps = storedSteps
        stepCountTextView.text = "$storedSteps خطوات"

        // جلب معلومات المستخدم
        val user = dbHelper.getUserBasicInfo()
        view.findViewById<TextView>(R.id.tvhight).text = "${user?.height ?: 0} CM"
        view.findViewById<TextView>(R.id.tvwight).text = "${user?.weight ?: 0} KG"
        view.findViewById<TextView>(R.id.tvage).text = "${user?.age ?: 0} سنة"
        view.findViewById<TextView>(R.id.tvExerciseTime).text = "${user?.width ?: 0} CM"

        dbHelperHoure = SQLiteHelper(requireContext())
         val tvHours = view.findViewById<TextView>(R.id.tvExerciseTime)

       val (hours, minutes) = dbHelperHoure.getTotalExerciseTime()
       tvHours.text = String.format("%02d h : %02d m", hours, minutes)


        val treadmillIcon = view.findViewById<ImageView>(R.id.ivTreadmill)

        treadmillIcon.setOnClickListener {
            // تأكيد تسجيل الخروج (اختياري)
            showLogoutDialog()
        }

        // أزرار التنقل
        view.findViewById<Button>(R.id.btnWater).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddWaterFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(R.id.btnAddFood).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, AddFoodsFragment())
                .addToBackStack(null)
                .commit()
        }

        // الحساس
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    override fun onStart() {
        super.onStart()
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }



    private fun showLogoutDialog() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("تسجيل الخروج")
        builder.setMessage("هل تريد تسجيل الخروج؟")

        builder.setPositiveButton("نعم") { _, _ ->
            // حذف الجلسة
            val sharedPreferences = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()

            // الانتقال إلى شاشة تسجيل الدخول
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        builder.setNegativeButton("إلغاء", null)
        builder.show()
    }


    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
            totalSteps += event.values[0].toInt()
            stepCountTextView.text = "$totalSteps خطوات"
            dbHelper.updateUserSteps(currentUserId, totalSteps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // لا حاجة هنا
    }
}
