package com.latihan.lalabib.e_karyawan.ui.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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
        val uname = intent.getStringExtra(extra_username)

        val employeeAdapter = EmployeeAdapter {
           //check role admin or hrd
            if (uname != null) {
                if (uname == getString(R.string.admin)) {
                    //if admin move to detail
                    moveToDetail(it)
                } else if (uname == getString(R.string.hrd)) {
                    //if hrd cant move to detail
                    alertDialog()
                }
            }
        }

        //observe employee data using live data through view model and show it by list
        employeeViewModel.getEmployee().observe(this) {
            employeeAdapter.submitList(it)
        }

        //setup recyclerview to adapter and layout manager to handle employee data
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

    private fun alertDialog() {
        AlertDialog.Builder(this@EmployeeActivity).apply {
            setMessage(getString(R.string.uname_check_msg))
            setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.cancel()
            }
            create()
            show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val extra_username = "username"
    }
}