package com.example.fitness_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        Log.d("SessionCheck", "HomeActivity > isLoggedIn = $isLoggedIn")

        // التحقق من الجلسة
        if (!isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavigation)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        setupBottomNavigation()
    }
    // إعداد شريط التنقل السفلي
    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            // عند الضغط على أي زر في شريط التنقل السفلي، سيتم تحميل الفراجمنت المناسب
            val fragment = when (item.itemId) {
                R.id.bottom_home -> HomeFragment()  // تحميل فراجمنت الصفحة الرئيسية
                R.id.bottom_trinnig -> TrainingFragment()  // تحميل فراجمنت التمارين
                R.id.bottom_profile -> ProfileFragment()  // تحميل فراجمنت الملف الشخصي
                R.id.bottom_report -> ReportFragment()  // تحميل فراجمنت التقارير
                else -> null
            }
            // التأكد من أن الفراجمنت غير فارغ، ثم تحميله
            fragment?.let {
                loadFragment(it)  // تحميل الفراجمنت المحدد
                return@setOnItemSelectedListener true
            }
            false
        }
    }
    // دالة لتحميل الفراجمنت في الحاوية
    private fun loadFragment(fragment: Fragment) {
        // نقوم بعمل transaction لاستبدال الفراجمنت الحالي بالفراجمنت الجديد
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)  // هنا نضع ID الحاوية في XML
            .addToBackStack(null)  // إذا أردت إضافة الفراجمنت إلى الBackStack ليكون قابلاً للرجوع
            .commit()
    }
}
