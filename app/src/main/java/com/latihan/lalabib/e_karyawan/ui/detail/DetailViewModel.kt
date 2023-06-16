package com.latihan.lalabib.e_karyawan.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.latihan.lalabib.e_karyawan.data.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EmployeeRepository): ViewModel() {

    private val _employeeId = MutableLiveData<Int>()

    private val _employee = _employeeId.switchMap { id ->
        repository.getEmployeeById(id)
    }
    val employee: LiveData<EmployeeEntities> = _employee

    fun setId(employeeId: Int) {
        if (employeeId == _employeeId.value) {
            return
        }
        _employeeId.value = employeeId
    }

    fun deleteEmployee() {
        viewModelScope.launch {
            _employee.value?.let { repository.deleteEmployee(it) }
        }
    }

    fun updateEmployee(employee: EmployeeEntities) = repository.updatedEmployee(employee)
}