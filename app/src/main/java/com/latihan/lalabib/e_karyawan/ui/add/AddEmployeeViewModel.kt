package com.latihan.lalabib.e_karyawan.ui.add

import androidx.lifecycle.ViewModel
import com.latihan.lalabib.e_karyawan.data.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository

class AddEmployeeViewModel(private val repository: EmployeeRepository): ViewModel() {

    fun insertEmployee(employee: EmployeeEntities) = repository.insertEmployee(employee)
}