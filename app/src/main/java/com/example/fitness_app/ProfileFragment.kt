package com.example.fitness_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fitness_app.Data.SQLiteHelper
import com.example.fitness_app.Data.User

class ProfileFragment : Fragment() {

    private lateinit var dbHelper: SQLiteHelper
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbHelper = SQLiteHelper(requireContext())

        val etName = view.findViewById<EditText>(R.id.etName)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etAge = view.findViewById<EditText>(R.id.etAge)
        val etWeight = view.findViewById<EditText>(R.id.etWeight)
        val etHeight = view.findViewById<EditText>(R.id.etHeight)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)
        val btnSave = view.findViewById<Button>(R.id.btnSave)

        fun setEditable(editable: Boolean) {
            etName.isEnabled = editable
            etEmail.isEnabled = editable
            etPassword.isEnabled = editable
            etAge.isEnabled = editable
            etWeight.isEnabled = editable
            etHeight.isEnabled = editable
        }

        dbHelper.getUser()?.let { user ->
            currentUser = user
            etName.setText(user.name)
            etEmail.setText(user.email)
            etPassword.setText(user.password)
            etAge.setText(user.age.toString())
            etWeight.setText(user.weight.toString())
            etHeight.setText(user.height.toString())
        }

        setEditable(false)

        btnEdit.setOnClickListener {
            setEditable(true)
            btnSave.isEnabled = true
        }

        btnSave.setOnClickListener {
            currentUser.apply {
                name = etName.text.toString()
                email = etEmail.text.toString()
                password = etPassword.text.toString()
                age = etAge.text.toString().toIntOrNull() ?: 0
                weight = etWeight.text.toString().toFloatOrNull() ?: 0f
                height = etHeight.text.toString().toFloatOrNull() ?: 0f
            }

            val success = dbHelper.updateUser(currentUser)
            if (success) {
                Toast.makeText(requireContext(), "تم حفظ التعديلات", Toast.LENGTH_SHORT).show()
                setEditable(false)
                btnSave.isEnabled = false
            } else {
                Toast.makeText(requireContext(), "فشل في الحفظ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
