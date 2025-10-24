package com.example.fitness_app

import MealAdapter
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fitness_app.Data.SQLiteHelper

import com.example.fitness_app.adpters.MealModel
import java.text.SimpleDateFormat
import java.util.*

class AddFoodsFragment : Fragment() {

    private lateinit var dbHelper: SQLiteHelper
    private lateinit var foodNameInput: EditText
    private lateinit var etCalories: EditText
    private lateinit var etProtein: EditText
    private lateinit var etFat: EditText
    private lateinit var etCarbs: EditText
    private lateinit var addMealButton: Button
    private lateinit var mealsListView: ListView
    private lateinit var mealsAdapter: MealAdapter
    private val mealsList = mutableListOf<MealModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_foods, container, false)

        // ربط العناصر من الواجهة
        foodNameInput = view.findViewById(R.id.foodNameInput)
        etCalories = view.findViewById(R.id.etCalories)
        etProtein = view.findViewById(R.id.etProtein)
        etFat = view.findViewById(R.id.etFat)
        etCarbs = view.findViewById(R.id.etCarbs)
        addMealButton = view.findViewById(R.id.addMealButton)
        mealsListView = view.findViewById(R.id.dailyLogList)

        // تهيئة قاعدة البيانات
        dbHelper = SQLiteHelper(requireContext())

        // ربط المحول بالقائمة
        mealsAdapter = MealAdapter(requireContext(), mealsList, dbHelper)
        mealsListView.adapter = mealsAdapter

        // تحميل الوجبات
        loadTodayMeals()

        // زر الإضافة
        addMealButton.setOnClickListener {
            saveMeal()
        }

        return view
    }

    private fun loadTodayMeals() {
        val db = dbHelper.readableDatabase
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val cursor = db.rawQuery(
            "SELECT name, calories, protein, fat, carbs, date FROM meals WHERE date = ?",
            arrayOf(today)
        )

        mealsList.clear()

        if (cursor.moveToFirst()) {
            do {
                val meal = MealModel(
                    name = cursor.getString(0),
                    calories = cursor.getInt(1),
                    protein = cursor.getDouble(2),
                    fat = cursor.getDouble(3),
                    carbs = cursor.getDouble(4),
                    date = cursor.getString(5)
                )
                mealsList.add(meal)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        mealsAdapter.notifyDataSetChanged()
    }

    private fun saveMeal() {
        val name = foodNameInput.text.toString().trim()
        val caloriesStr = etCalories.text.toString().trim()
        val proteinStr = etProtein.text.toString().trim()
        val fatStr = etFat.text.toString().trim()
        val carbsStr = etCarbs.text.toString().trim()

        if (name.isEmpty() || caloriesStr.isEmpty() || proteinStr.isEmpty() || fatStr.isEmpty() || carbsStr.isEmpty()) {
            Toast.makeText(requireContext(), "يرجى تعبئة جميع الحقول", Toast.LENGTH_SHORT).show()
            return
        }

        val calories = caloriesStr.toIntOrNull()
        val protein = proteinStr.toDoubleOrNull()
        val fat = fatStr.toDoubleOrNull()
        val carbs = carbsStr.toDoubleOrNull()

        if (calories == null || protein == null || fat == null || carbs == null) {
            Toast.makeText(requireContext(), "الرجاء إدخال قيم رقمية صحيحة", Toast.LENGTH_SHORT).show()
            return
        }

        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("calories", calories)
            put("protein", protein)
            put("fat", fat)
            put("carbs", carbs)
            put("date", date)
        }

        val result = db.insert("meals", null, values)
        db.close()

        if (result != -1L) {
            Toast.makeText(requireContext(), "✅ تم إضافة الوجبة بنجاح", Toast.LENGTH_SHORT).show()

            val newMeal = MealModel(name, calories, protein, fat, carbs, date)
            mealsList.add(newMeal)
            mealsAdapter.notifyDataSetChanged()

            foodNameInput.text.clear()
            etCalories.text.clear()
            etProtein.text.clear()
            etFat.text.clear()
            etCarbs.text.clear()
        } else {
            Toast.makeText(requireContext(), "❌ فشل في إضافة الوجبة", Toast.LENGTH_SHORT).show()
        }
    }
}
