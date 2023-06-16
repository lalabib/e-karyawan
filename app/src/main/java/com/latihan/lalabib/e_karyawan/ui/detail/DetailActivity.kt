package com.latihan.lalabib.e_karyawan.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.EmployeeEntities
import com.latihan.lalabib.e_karyawan.databinding.ActivityDetailBinding
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory
import java.text.NumberFormat
import java.util.Locale

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

    private fun setupData() {
        val employeeId = intent.getIntExtra(employee_id, 0)

        detailViewModel.setId(employeeId)
        detailViewModel.employee.observe(this) { employee ->
            if (employee != null) {
                selectedEmployee = employee

                binding.edtId.setText(employee.id.toString())
                binding.edtName.setText(employee.nama)
                binding.edtAddress.setText(employee.alamat)
                binding.edtGender.setText(employee.jenisKelamin)
                binding.edtPosition.setText(employee.jabatan)
                binding.edtLengthWork.setText(employee.lamaBekerja)
                binding.edtSalary.setText(employee.gajiPokok.toString())

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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val employee_id = "id"
    }
}