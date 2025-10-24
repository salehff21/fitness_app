package com.example.fitness_app.adpters

data class MealModel(
    val name: String,
    val calories: Int,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val date: String // ضروري للتحديد عند الحذف
)
