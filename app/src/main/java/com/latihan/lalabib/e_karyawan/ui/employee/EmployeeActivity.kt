package com.latihan.lalabib.e_karyawan.ui.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.databinding.ActivityEmployeeBinding
import com.latihan.lalabib.e_karyawan.ui.detail.DetailActivity
import com.latihan.lalabib.e_karyawan.ui.detail.DetailActivity.Companion.employee_id
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory

class EmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmployeeBinding
    private lateinit var employeeViewModel: EmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //code here
        setupView()
        setupViewModel()
        setupData()
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_employee)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        employeeViewModel = ViewModelProvider(this, factory)[EmployeeViewModel::class.java]
    }

    private fun setupData() {
        val employeeAdapter = EmployeeAdapter {
           // create if to check role manager or hrd
            moveToDetail(it)
        }

        employeeViewModel.getEmployee().observe(this) {
            employeeAdapter.submitList(it)
        }

        binding.apply {
            rvEmployee.layoutManager = LinearLayoutManager(this@EmployeeActivity)
            rvEmployee.adapter = employeeAdapter
        }
    }

    private fun moveToDetail(it: EmployeeEntities) {
        val intent = Intent(this@EmployeeActivity, DetailActivity::class.java)
        intent.putExtra(employee_id, it.id)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}