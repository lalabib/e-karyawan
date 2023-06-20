package com.latihan.lalabib.e_karyawan.ui.salary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.local.SalaryEntity
import kotlinx.coroutines.launch

class SalaryViewModel(private val repository: EmployeeRepository): ViewModel() {

    fun getEmployeeName(): LiveData<List<String>> = repository.getAllEmployeeByName()

    private val _employeeName = MutableLiveData<String>()

    private val _employee = _employeeName.switchMap { name ->
        repository.getEmployeeByName(name)
    }
    val employee: LiveData<EmployeeEntities> = _employee

    fun setName(employeeName: String) {
        if (employeeName == _employeeName.value) {
            return
        }
        _employeeName.value = employeeName
    }

    fun insertSalaries(salary: SalaryEntity) {
        viewModelScope.launch {
            repository.insertSalary(salary)
        }
    }
}