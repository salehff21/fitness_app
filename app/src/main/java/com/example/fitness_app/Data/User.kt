package com.example.fitness_app.Data

data class User(
    val id: Int,
    var name: String,
    var email: String,
    var password: String,
    var age: Int,
    var weight: Float,
    var height: Float,
    var  steps: Int,
)
