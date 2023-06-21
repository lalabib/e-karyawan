package com.latihan.lalabib.e_karyawan.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository
import kotlinx.coroutines.launch

class AddEmployeeViewModel(private val repository: EmployeeRepository): ViewModel() {

    fun insertEmployee(employee: EmployeeEntities) {
        viewModelScope.launch {
            repository.insertEmployee(employee)
        }
    }
}