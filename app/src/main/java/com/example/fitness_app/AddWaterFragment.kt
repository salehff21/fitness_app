package com.example.fitness_app

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.fitness_app.Data.SQLiteHelper
import java.text.SimpleDateFormat
import java.util.*

class AddWaterFragment : Fragment() {

    private lateinit var dbHelper: SQLiteHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_water, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = SQLiteHelper(requireContext())

        val etCups = view.findViewById<EditText>(R.id.etCups)
        val spinnerCupSize = view.findViewById<Spinner>(R.id.spinnerCupSize)
        val btnAddWater = view.findViewById<Button>(R.id.btnAddWater)
        val tvWaterTotal = view.findViewById<TextView>(R.id.tvWaterTotal)
        val progressWater = view.findViewById<ProgressBar>(R.id.progressWater)

        // أول مرة يحدث التقدم
        updateWaterTotal(tvWaterTotal, progressWater)

        btnAddWater.setOnClickListener {
            val cupCountText = etCups.text.toString().trim()
            val cupSizeText = spinnerCupSize.selectedItem.toString().replace("مل", "").trim()

            if (cupCountText.isEmpty()) {
                Toast.makeText(requireContext(), "يرجى إدخال عدد الأكواب", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cupCount = cupCountText.toIntOrNull() ?: 0
            val cupSizeMl = cupSizeText.toIntOrNull() ?: 0

            val totalMl = cupCount * cupSizeMl
            val totalLiters = totalMl / 1000.0
            val unit = "L"
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())

            val englishDigitsDate = date.map {
                if (it.isDigit()) '0' + (it.code - '0'.code) else it
            }.joinToString("")

            val success = dbHelper.insertWater(totalLiters, unit, englishDigitsDate)

            if (success) {
                Toast.makeText(
                    requireContext(),
                    "✅ تم تسجيل $cupCount كوب (${String.format("%.2f", totalLiters)}L)",
                    Toast.LENGTH_SHORT
                ).show()
                etCups.text.clear()
                updateWaterTotal(tvWaterTotal, progressWater)
            } else {
                Toast.makeText(requireContext(), "❌ فشل في الإضافة", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ دالة لتحديث مجموع الماء والتقدم
    private fun updateWaterTotal(tv: TextView, progressBar: ProgressBar) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val totalLiters = dbHelper.getDailyWaterTotal(date)
        val targetLiters = 10.0

        val progressPercent = ((totalLiters / targetLiters) * 100).toInt().coerceAtMost(100)

        // تحديث النص
        tv.text = "الماء المستهلك اليوم: ${String.format("%.2f", totalLiters)} L"

        // تحديث شريط التقدم
        progressBar.progress = progressPercent

        // ✅ تغيير لون الشريط عند الاقتراب من الهدف
        if (progressPercent >= 80) {
            progressBar.progressTintList = android.content.res.ColorStateList.valueOf(Color.GREEN)
        } else {
            progressBar.progressTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#00BCD4"))
        }
    }
}
