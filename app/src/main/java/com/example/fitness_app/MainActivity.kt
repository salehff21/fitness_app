package com.example.fitness_app

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // الحصول على الجلسة من SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        Log.d("SessionCheck", "هل المستخدم مسجل دخول؟ = $isLoggedIn")

        if (isLoggedIn) {
            // ✅ المستخدم مسجل دخول: الانتقال إلى الصفحة الرئيسية
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // إنهاء هذا النشاط حتى لا يعود المستخدم بالرجوع
        } else {
            // ❌ المستخدم غير مسجل: عرض واجهة تسجيل الدخول
            setContentView(R.layout.activity_main)

            if (savedInstanceState == null) {
                val loginFragment = FragmentLogin()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
                    .commit()
            }
        }
    }
}
