package com.example.fitness_app

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.fitness_app.Data.SQLiteHelper
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var dbHelperp: SQLiteHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val dbHelper = SQLiteHelper(requireContext())
            val listView = view.findViewById<ListView>(R.id.list_items_Completed)

            // ✅ التاريخ الحالي بصيغة yyyy-MM-dd
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT name FROM meals WHERE date = ?", arrayOf(today))

            val mealNames = mutableListOf<String>()

            if (cursor.moveToFirst()) {
                do {
                    val mealName = cursor.getString(0)
                    mealNames.add(mealName)
                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()
           dbHelperp = SQLiteHelper(requireContext())
            pieChart = view.findViewById(R.id.pieChart)
            barChart = view.findViewById(R.id.barChart)

            setupPieChart()
            setupBarChart()
            val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, mealNames) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = super.getView(position, convertView, parent) as TextView
                    view.typeface = ResourcesCompat.getFont(requireContext(), R.font.cairo_medium)
                    return view
                }
            }
            listView.adapter = adapter

        }


    private fun setupPieChart() {
        val db = dbHelperp.readableDatabase
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val cursor = db.rawQuery("SELECT SUM(protein), SUM(fat), SUM(carbs) FROM meals WHERE date = ?", arrayOf(today))

        if (cursor.moveToFirst()) {
            val protein = cursor.getDouble(0)
            val fat = cursor.getDouble(1)
            val carbs = cursor.getDouble(2)
            val total = protein + fat + carbs

            val entries = ArrayList<PieEntry>()

            if (total > 0) {
                if (protein > 0) entries.add(PieEntry((protein / total * 100).toFloat(), "بروتين"))
                if (fat > 0) entries.add(PieEntry((fat / total * 100).toFloat(), "دهون"))
                if (carbs > 0) entries.add(PieEntry((carbs / total * 100).toFloat(), "كربوهيدرات"))
            }

            val dataSet = PieDataSet(entries, "")
            dataSet.setColors(*com.github.mikephil.charting.utils.ColorTemplate.MATERIAL_COLORS)
            dataSet.valueTextColor = Color.WHITE
            dataSet.valueTextSize = 14f
            dataSet.valueTypeface = Typeface.DEFAULT_BOLD // ✅ خط عريض

            val pieData = PieData(dataSet)

            // ✅ إظهار النسب مع علامة %
            pieData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return String.format(Locale.getDefault(), "%.1f%%", value)
                }
            })
            val cairoFont = ResourcesCompat.getFont(requireContext(), R.font.cairo_medium)
            dataSet.valueTypeface = cairoFont


            pieChart.data = pieData
            pieChart.setUsePercentValues(true) // ✅ ضروري لإظهار النسب
            pieChart.description.isEnabled = false
            pieChart.centerText = "توزيع المغذيات"
            pieChart.setEntryLabelTypeface(cairoFont)// ✅ تسميات الفئات bold أيضاً
            pieChart.animateY(1000)
            pieChart.invalidate()
        }

        cursor.close()
        db.close()
    }

    private fun setupBarChart() {
        val db = dbHelperp.readableDatabase
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK, -6)

        for (i in 0..6) {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            val label = SimpleDateFormat("EEE", Locale("ar")).format(calendar.time)

            val cursor = db.rawQuery("SELECT SUM(calories) FROM meals WHERE date = ?", arrayOf(date))
            var calories = 0
            if (cursor.moveToFirst()) {
                calories = cursor.getInt(0)
            }
            entries.add(BarEntry(i.toFloat(), calories.toFloat()))
            labels.add(label)
            cursor.close()
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }

        val dataSet = BarDataSet(entries, "السعرات اليومية")
        dataSet.color = Color.parseColor("#6D5BA6")
        dataSet.valueTextSize = 12f

        // إعداد خط الأرقام (السعرات الحرارية) إلى Times New Roman
        val timesNewRoman = Typeface.create("times_new_roman", Typeface.NORMAL)
        dataSet.valueTypeface = timesNewRoman

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.animateY(1000)

        // إعداد محور X (أيام الأسبوع)
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        // تنسيق الخط لعرض الأيام
        xAxis.textSize = 10f // حجم صغير
        xAxis.typeface = Typeface.DEFAULT_BOLD // خط عريض
        xAxis.textColor = Color.BLACK

        barChart.axisRight.isEnabled = false
        barChart.invalidate()
        db.close()
    }

}
