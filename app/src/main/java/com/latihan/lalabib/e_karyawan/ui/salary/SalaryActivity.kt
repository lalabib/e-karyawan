package com.latihan.lalabib.e_karyawan.ui.salary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.local.SalaryEntity
import com.latihan.lalabib.e_karyawan.databinding.ActivitySalaryBinding
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory
import java.util.UUID
import kotlin.math.roundToInt

class SalaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalaryBinding
    private lateinit var salaryViewModel: SalaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewMode()
        setupData()
        setupAction()
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_salary)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewMode() {
        val factory = ViewModelFactory.getInstance(this)
        salaryViewModel = ViewModelProvider(this, factory)[SalaryViewModel::class.java]
    }

    private fun setupData() {
        //dropdown name by employee name
        val employeeNameList = ArrayList<String>()

        salaryViewModel.getEmployeeName().observe(this) { employeeName ->
            employeeNameList.addAll(employeeName)
            val arrayAdapterName = ArrayAdapter(this, R.layout.dropdown_item, employeeNameList)
            binding.edtName.setAdapter(arrayAdapterName)

            binding.edtName.setOnItemClickListener { _, _, position, _ ->
                val selectedName = employeeNameList[position]
                salaryViewModel.setName(selectedName)
            }
        }

        salaryViewModel.employee.observe(this) { employee ->
            if (employee != null) {
                binding.edtAddress.setText(employee.alamat)
                binding.edtGender.setText(employee.jenisKelamin)
                binding.edtPosition.setText(employee.jabatan)
                binding.edtSalary.setText(employee.gajiPokok.toString())

                sumSalaries()
            }
        }

        //dropdown bulan
        val month = resources.getStringArray(R.array.month)
        val arrayAdapterMonth = ArrayAdapter(this, R.layout.dropdown_item, month)
        binding.edtMonth.setAdapter(arrayAdapterMonth)
    }

    private fun sumSalaries() {
        //sum salaries by position
        val position = binding.edtPosition.text.toString()
        val salary = binding.edtSalary.text.toString().toInt()
        val bonus = binding.edtBonus
        val pphSalary = binding.edtPph
        val sumSalaries = binding.edtTotalGaji

        val bonusManager = (salary * managerBonus).roundToInt()
        val bonusSpv = (salary * spvBonus).roundToInt()
        val bonusStaff = (salary * staffBonus).roundToInt()
        val tax = (salary * pph).roundToInt()

        when (position) {
            getString(R.string.manager) -> {
                bonus.setText(bonusManager.toString())
                pphSalary.setText(tax.toString())
                val sum = salary + bonusManager - tax
                sumSalaries.setText(sum.toString())
            }

            getString(R.string.spv) -> {
                bonus.setText(bonusSpv.toString())
                pphSalary.setText(tax.toString())
                val sum = salary + bonusSpv - tax
                sumSalaries.setText(sum.toString())
            }

            getString(R.string.staff) -> {
                bonus.setText(bonusStaff.toString())
                pphSalary.setText(tax.toString())
                val sum = salary + bonusStaff - tax
                sumSalaries.setText(sum.toString())
            }
        }
    }

    private fun saveSalaries() {
        val name = binding.edtName.text.toString()
        val month = binding.edtMonth.text.toString()
        val salary = binding.edtSalary.text.toString()
        val bonus = binding.edtBonus.text.toString()
        val pph = binding.edtPph.text.toString()
        val sumSalary = binding.edtTotalGaji.text.toString()

        if (name.isNotEmpty() && month.isNotEmpty() && bonus.isNotEmpty() && pph.isNotEmpty()
            && salary.isNotEmpty() && sumSalary.isNotEmpty()
        ) {
            val salaries = SalaryEntity(
                UUID.randomUUID().toString(),
                name,
                month,
                salary.toInt(),
                bonus.toInt(),
                pph.toInt(),
                sumSalary.toInt()
            )
            salaryViewModel.insertSalaries(salaries)
            Toast.makeText(this@SalaryActivity, R.string.saved, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@SalaryActivity, R.string.cant_empty, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAction() {
        binding.btnSum.setOnClickListener { saveSalaries() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val pph = 0.05
        const val managerBonus = 0.5
        const val spvBonus = 0.4
        const val staffBonus = 0.3
    }
}