package com.latihan.lalabib.e_karyawan.ui.employee

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository

class EmployeeViewModel(private val repository: EmployeeRepository): ViewModel() {

    fun getEmployee(): LiveData<List<EmployeeEntities>> = repository.getEmployees()
}