package com.example.fitness_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fitness_app.Data.SQLiteHelper
import com.example.fitness_app.Data.User
import com.example.fitness_app.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var dbHelper: SQLiteHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        val binding = FragmentRegisterBinding.inflate(inflater, container, false)

        // تهيئة قاعدة البيانات
        dbHelper = SQLiteHelper(requireContext())

        // ربط الحقول من XML باستخدام binding
        val nameEditText = binding.etName
        val emailEditText = binding.txtEmail
        val passwordEditText = binding.txtPassword
        val ageEditText = binding.txtAge
        val weightEditText = binding.txtWeight
        val heightEditText = binding.txtHight
        val registerButton = binding.btnRegister

        // الاستماع لزر إنشاء الحساب
        registerButton.setOnClickListener {
            // استخراج البيانات المدخلة من المستخدم
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val age = ageEditText.text.toString().toInt()
            val weight = weightEditText.text.toString().toFloat()
            val height = heightEditText.text.toString().toFloat()

            // إنشاء كائن User
            val user = User(0, name, email, password, age, weight, height, 0)

            // إضافة المستخدم إلى قاعدة البيانات
            val userId = dbHelper.addUser(user)

            // تأكيد النجاح
            if (userId > 0) {
                openWelcomFragment()
                Toast.makeText(requireContext(), "تم إنشاء الحساب بنجاح", Toast.LENGTH_SHORT).show()
                // الانتقال إلى صفحة أخرى مثل صفحة تسجيل الدخول
            } else {
                Toast.makeText(requireContext(), "حدث خطأ، حاول مجددًا", Toast.LENGTH_SHORT).show()

            }
        }
        return binding.root
    }

    // استبدال الـ Fragment الحالي بـ WelcomeFragment بعد تسجيل الدخول
    private fun openWelcomFragment() {
        Log.d("TestApplication", "This is a test message openWelcomFragment")  // مستوى Debug
        val fragment = FragmentHi()  // التأكد من الاسم الصحيح لـ FragmentWelcome
        val transaction = parentFragmentManager.beginTransaction()  // استخدام parentFragmentManager بدلاً من supportFragmentManager
        transaction.replace(R.id.fragment_container, fragment) // حاوية الـ Fragment
        transaction.addToBackStack(null)
        onLoginSuccess()// يضيفه إلى Back Stack ليسمح بالرجوع للخلف
        transaction.commit()
    }

    private fun onLoginSuccess() {
        val sharedPreferences = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_logged_in", true).apply()

        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
}
}
