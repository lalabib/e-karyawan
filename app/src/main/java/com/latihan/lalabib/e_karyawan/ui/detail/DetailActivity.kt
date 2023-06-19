package com.latihan.lalabib.e_karyawan.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.databinding.ActivityDetailBinding
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var selectedEmployee: EmployeeEntities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupData()
        setupAction()
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_detail)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        detailViewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    //setup data by get data from employee activity by intent
    private fun setupData() {
        val employeeId = intent.getIntExtra(employee_id, 0)

        detailViewModel.setId(employeeId)
        detailViewModel.employee.observe(this) { employee ->
            if (employee != null) {
                selectedEmployee = employee

                binding.edtId.setText(employee.id.toString())
                binding.edtName.setText(employee.nama)
                binding.edtAddress.setText(employee.alamat)
                binding.edtSalary.setText(employee.gajiPokok.toString())

                //dropdown jenis kelamin
                val gender = resources.getStringArray(R.array.jenis_kelamin)
                val arrayAdapterGender = ArrayAdapter(this, R.layout.dropdown_item, gender)
                binding.edtGender.setText(employee.jenisKelamin)
                binding.edtGender.setAdapter(arrayAdapterGender)

                //dropdown jabatan
                val position = resources.getStringArray(R.array.jabatan)
                val arrayAdapterPosition = ArrayAdapter(this, R.layout.dropdown_item, position)
                binding.edtPosition.setText(employee.jabatan)
                binding.edtPosition.setAdapter(arrayAdapterPosition)

                //dropdown lama jabatan
                val lengthWork = resources.getStringArray(R.array.lama_jabatan)
                val arrayAdapterLengthWork = ArrayAdapter(this, R.layout.dropdown_item, lengthWork)
                binding.edtLengthWork.setText(employee.lamaBekerja)
                binding.edtLengthWork.setAdapter(arrayAdapterLengthWork)


                //func to change number format to rupiah
                //val formatter = NumberFormat.getCurrencyInstance(Locale("in", "id"))
                //val hasilFormat = formatter.format(employee.gajiPokok.toString().toDouble())
                //binding.edtSalary.setText(hasilFormat.substringBefore(","))
            }
        }
    }

    private fun setupAction() {
        binding.btnDelete.setOnClickListener { deleteEmployee() }
        binding.btnUpdate.setOnClickListener { updateEmployee() }
    }

    private fun deleteEmployee() {
        AlertDialog.Builder(this@DetailActivity).apply {
            setTitle(getString(R.string.confirm))
            setMessage(getString(R.string.delete_msg))
            setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                detailViewModel.deleteEmployee()
                Toast.makeText(this@DetailActivity, R.string.deleted, Toast.LENGTH_SHORT).show()
                finish()
                dialog.cancel()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            create()
            show()
        }
    }

    private fun updateEmployee() {
        val id = binding.edtId.text.toString()
        val name = binding.edtName.text.toString()
        val address = binding.edtAddress.text.toString()
        val gender = binding.edtGender.text.toString()
        val position = binding.edtPosition.text.toString()
        val lengthWork = binding.edtLengthWork.text.toString()
        val salary = binding.edtSalary.text.toString()


        AlertDialog.Builder(this@DetailActivity).apply {
            setTitle(getString(R.string.confirm))
            setMessage(getString(R.string.edit_msg))
            setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                val employee = EmployeeEntities(id.toInt(), name, address, gender, position, lengthWork, salary.toInt())
                detailViewModel.updateEmployee(employee)
                Toast.makeText(this@DetailActivity, R.string.edited, Toast.LENGTH_SHORT).show()
                dialog.cancel()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
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
        const val employee_id = "id"
    }
}