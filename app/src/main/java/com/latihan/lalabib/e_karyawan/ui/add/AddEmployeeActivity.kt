package com.latihan.lalabib.e_karyawan.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.EmployeeEntities
import com.latihan.lalabib.e_karyawan.databinding.ActivityAddEmployeeBinding
import com.latihan.lalabib.e_karyawan.ui.employee.EmployeeActivity
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEmployeeBinding
    private lateinit var addEmployeeViewModel: AddEmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_add)
            setDisplayHomeAsUpEnabled(true)
        }

        //dropdown jenis kelamin
        val gender = resources.getStringArray(R.array.jenis_kelamin)
        val arrayAdapterGender = ArrayAdapter(this, R.layout.dropdown_item, gender)
        binding.edtGender.setAdapter(arrayAdapterGender)

        //dropdown jabatan
        val position = resources.getStringArray(R.array.jabatan)
        val arrayAdapterPosition = ArrayAdapter(this, R.layout.dropdown_item, position)
        binding.edtPosition.setAdapter(arrayAdapterPosition)

        //dropdown lama jabatan
        val lengthWork = resources.getStringArray(R.array.lama_jabatan)
        val arrayAdapterLengthWork = ArrayAdapter(this, R.layout.dropdown_item, lengthWork)
        binding.edtLengthWork.setAdapter(arrayAdapterLengthWork)
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        addEmployeeViewModel = ViewModelProvider(this, factory)[AddEmployeeViewModel::class.java]
    }

    private fun saveData() {
        val id = binding.edtId.text.toString()
        val name = binding.edtName.text.toString()
        val address = binding.edtAddress.text.toString()
        val gender = binding.edtGender.text.toString()
        val position = binding.edtPosition.text.toString()
        val lengthWork = binding.edtLengthWork.text.toString()
        val salary = binding.edtSalary.text.toString()

        if (id.isNotEmpty() && name.isNotEmpty() && address.isNotEmpty() && gender.isNotEmpty() &&
            position.isNotEmpty() && lengthWork.isNotEmpty() && salary.isNotEmpty()
        ) {
            val employee = EmployeeEntities(id.toInt(), name, address, gender, position, lengthWork, salary.toInt())
            addEmployeeViewModel.insertEmployee(employee)
            Toast.makeText(this, R.string.succes, Toast.LENGTH_SHORT).show()
            moveToKaryawan()
        } else {
            Toast.makeText(this, R.string.cant_empty, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAction() {
        binding.btnInsert.setOnClickListener { saveData() }
    }

    private fun moveToKaryawan() {
        val moveActivity = Intent(this@AddEmployeeActivity, EmployeeActivity::class.java)
        moveActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(moveActivity)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}