package com.latihan.lalabib.e_karyawan.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.latihan.lalabib.e_karyawan.databinding.ActivityHomeBinding
import com.latihan.lalabib.e_karyawan.ui.add.AddEmployeeActivity
import com.latihan.lalabib.e_karyawan.ui.employee.EmployeeActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moveToEmployee()
        moveToAddEmployee()
        // move to hitungGaji()
    }

    private fun moveToEmployee() {
        binding.menu.llKaryawan.setOnClickListener {
            startActivity(Intent(this@HomeActivity, EmployeeActivity::class.java))
        }
    }

    private fun moveToAddEmployee() {
        binding.menu.llTambahKaryawan.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AddEmployeeActivity::class.java))
        }
    }
}