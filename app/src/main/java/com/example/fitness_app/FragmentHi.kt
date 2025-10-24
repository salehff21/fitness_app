package com.example.fitness_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fitness_app.Data.SQLiteHelper
class FragmentHi : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_hi, container, false)
        // زر استمرار للانتقال إلى FragmentHome
        val btnContinue = rootView.findViewById<Button>(R.id.btnContinue)
        btnContinue.setOnClickListener {
            openHomeFragment()
        }


        return rootView
    }
    private fun openHomeFragment() {

        Log.d("FragmentDebug", "انتقلنا إلى HomeActivity")
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)


    }

}
