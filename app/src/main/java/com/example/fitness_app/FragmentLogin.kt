package com.example.fitness_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.fitness_app.Data.SQLiteHelper

class FragmentLogin : Fragment() {

    private lateinit var dbHelper: SQLiteHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // تحميل تصميم واجهة تسجيل الدخول
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // تهيئة قاعدة البيانات
        dbHelper = SQLiteHelper(requireContext())

        // عناصر الشاشة
        val emailEditText = view.findViewById<EditText>(R.id.txt_username)
        val passwordEditText = view.findViewById<EditText>(R.id.txtLogin_Password)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)
        val tvRegister = view.findViewById<TextView>(R.id.tvRegister)

        // زر تسجيل الدخول
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "يرجى إدخال البريد الإلكتروني وكلمة المرور!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // التحقق من بيانات المستخدم في قاعدة البيانات
            if (dbHelper.checkUser(email, password)) {
                Toast.makeText(requireContext(), "تم تسجيل الدخول بنجاح!", Toast.LENGTH_SHORT).show()
                saveSessionAndOpenHome()  // ✅ بعد النجاح
            } else {
                Toast.makeText(requireContext(), "البريد الإلكتروني أو كلمة المرور غير صحيحة!", Toast.LENGTH_SHORT).show()
            }
        }

        // عند الضغط على "إنشاء حساب" الانتقال إلى RegisterFragment
        tvRegister.setOnClickListener {
            openRegisterFragment()
        }

        return view
    }

    // ✅ فتح RegisterFragment
    private fun openRegisterFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RegisterFragment())
            .addToBackStack(null)
            .commit()
    }

    // ✅ حفظ الجلسة والانتقال إلى HomeActivity
    private fun saveSessionAndOpenHome() {
        val sharedPreferences = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()

        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
