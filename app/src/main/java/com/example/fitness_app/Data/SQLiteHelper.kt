package com.example.fitness_app.Data

import android.R.attr.duration
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fitnessApp.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_HEIGHT = "height"
        private const val COLUMN_STEPS = "steps"

        private const val TABLE_MEALS = "meals"
        private const val COLUMN_MEAL_ID = "id"
        private const val COLUMN_MEAL_NAME = "name"
        private const val COLUMN_MEAL_CALORIES = "calories"
        private const val COLUMN_MEAL_PROTEIN = "protein"
        private const val COLUMN_MEAL_FAT = "fat"
        private const val COLUMN_MEAL_CARBS = "carbs"
        private const val COLUMN_MEAL_DATE = "date"

        // جدول استهلاك الماء
        private const val TABLE_WATER = "water_intake"
        private const val COLUMN_WATER_ID = "id"
        private const val COLUMN_WATER_AMOUNT = "amount"
        private const val COLUMN_WATER_UNIT = "unit"
        private const val COLUMN_WATER_DATE = "date"

        // ✅ جدول التمارين المنفذة
        private const val TABLE_COMPLETED_EXERCISES = "completed_exercises"
        private const val COLUMN_EXERCISE_ID = "id"
        private const val COLUMN_EXERCISE_NAME = "name"
        private const val COLUMN_EXERCISE_DURATION = "duration"
        private const val COLUMN_EXERCISE_NOTE = "note"
        private const val COLUMN_EXERCISE_DATE = "date"

    }
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_PASSWORD TEXT, "
                + "$COLUMN_AGE INTEGER, "
                + "$COLUMN_WEIGHT REAL, "
                + "$COLUMN_HEIGHT REAL, "
                + "$COLUMN_STEPS INTEGER DEFAULT 0)" // إضافة عمود الخطوات
                )
        db.execSQL(CREATE_USERS_TABLE)
        val CREATE_MEALS_TABLE = ("CREATE TABLE $TABLE_MEALS ("
                + "$COLUMN_MEAL_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_MEAL_NAME TEXT NOT NULL, "
                + "$COLUMN_MEAL_CALORIES INTEGER, "
                + "$COLUMN_MEAL_PROTEIN REAL, "
                + "$COLUMN_MEAL_FAT REAL, "
                + "$COLUMN_MEAL_CARBS REAL, "
                + "$COLUMN_MEAL_DATE TEXT)")
        db.execSQL(CREATE_MEALS_TABLE)

        // جدول استهلاك الماء
        val CREATE_WATER_TABLE = ("CREATE TABLE $TABLE_WATER ("
                + "$COLUMN_WATER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_WATER_AMOUNT REAL NOT NULL, "
                + "$COLUMN_WATER_UNIT TEXT, "
                + "$COLUMN_WATER_DATE TEXT)")
        db.execSQL(CREATE_WATER_TABLE)

        val CREATE_COMPLETED_EXERCISES_TABLE =(" CREATE TABLE $TABLE_COMPLETED_EXERCISES ("
                + " $COLUMN_EXERCISE_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " $COLUMN_EXERCISE_NAME TEXT NOT NULL,"
                + "$COLUMN_EXERCISE_DURATION INTEGER,"
                + "$COLUMN_EXERCISE_NOTE TEXT,"
                +"$COLUMN_EXERCISE_DATE TEXT)")
        db.execSQL(CREATE_COMPLETED_EXERCISES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MEALS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WATER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COMPLETED_EXERCISES")

        onCreate(db)
    }
    // دالة لإدخال المستخدم الجديد
    fun addUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, user.name)
        values.put(COLUMN_EMAIL, user.email)
        values.put(COLUMN_PASSWORD, user.password)
        values.put(COLUMN_AGE, user.age)
        values.put(COLUMN_WEIGHT, user.weight)
        values.put(COLUMN_HEIGHT, user.height)
        values.put(COLUMN_STEPS, user.steps)

        // إدخال البيانات في الجدول
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    // دالة إدخال كوب ماء
    fun insertWater(amount: Double, unit: String, date: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_WATER_AMOUNT, amount)
            put(COLUMN_WATER_UNIT, unit)
            put(COLUMN_WATER_DATE, date)
        }

        val result = db.insert(TABLE_WATER, null, values)
        db.close()
        return result != -1L
    }
    // دالة حساب مجموع الماء المستهلك ليوم معيّن (بـ Liters مثلاً)
    fun getDailyWaterTotal(date: String): Double {
        val db = readableDatabase
        val query = "SELECT SUM($COLUMN_WATER_AMOUNT) FROM $TABLE_WATER WHERE $COLUMN_WATER_DATE = ?"
        val cursor = db.rawQuery(query, arrayOf(date))

        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }

        cursor.close()
        db.close()
        return total
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    @SuppressLint("Range")
    fun getUserByEmail(email: String): User? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE))
            val weight = cursor.getFloat(cursor.getColumnIndex(COLUMN_WEIGHT))
            val height = cursor.getFloat(cursor.getColumnIndex(COLUMN_HEIGHT))
            val steps  = cursor.getInt(cursor.getColumnIndex(COLUMN_STEPS))

            cursor.close()
            db.close()

            return User(id, name, userEmail, password, age, weight, height, steps)
        }
        cursor.close()
        db.close()
        return null
    }
    fun updateUserSteps(userId: Int, steps: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("steps", steps)
        }
        db.update("users", values, "id = ?", arrayOf(userId.toString()))
        db.close()
    }


    fun getUser(): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Users LIMIT 1", null)
        return if (cursor.moveToFirst()) {
            User(id = cursor.getInt(0), name = cursor.getString(1),
                email = cursor.getString(2), password = cursor.getString(3),
                age = cursor.getInt(4), weight = cursor.getFloat(5),
                height = cursor.getFloat(6), steps =cursor.getInt(7)
            )
        } else null.also { cursor.close() }
    }
    fun getUserBasicInfo(): UserBasicInfo? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT weight, height, age FROM Users LIMIT 1", null)

        return if (cursor.moveToFirst()) {
            //val  = cursor.getFloat(cursor.getColumnIndexOrThrow("weidht"))
            val weight = cursor.getFloat(cursor.getColumnIndexOrThrow("weight"))
            val height = cursor.getFloat(cursor.getColumnIndexOrThrow("height"))
            val age = cursor.getInt(cursor.getColumnIndexOrThrow("age"))

            cursor.close()
            UserBasicInfo(weight, height, age, 0f) // ضع 0 مؤقتًا للعرض (width)
        } else {
            cursor.close()
            null
        }
    }
    fun getUserSteps(userId: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_STEPS FROM $TABLE_USERS WHERE $COLUMN_ID = ?", arrayOf(userId.toString()))
        var steps = 0
        if (cursor.moveToFirst()) {
            steps = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return steps
    }
    fun updateUser(user: User): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("age", user.age)
            put("weight", user.weight)
            put("height", user.height)
        }
        return db.update("Users", values, "id=?", arrayOf(user.id.toString())) > 0
    }
    fun getTotalExerciseTime(): Pair<Int, Int> {
        val db = this.readableDatabase
        var totalSeconds = 0
        val cursor = db.rawQuery("SELECT SUM(duration) FROM completed_exercises", null)

        if (cursor.moveToFirst()) {
            totalSeconds = cursor.getInt(0)  // يتم جلب المدة الإجمالية بالثواني
        }

        cursor.close()
        db.close()

        val totalMinutes = totalSeconds / 60
        val hours = totalMinutes / 60
        val minutes = totalMinutes % 60

        return Pair(hours, minutes)
    }

}
