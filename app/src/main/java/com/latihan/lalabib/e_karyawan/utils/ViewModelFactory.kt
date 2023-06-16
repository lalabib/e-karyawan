package com.latihan.lalabib.e_karyawan.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository
import com.latihan.lalabib.e_karyawan.ui.add.AddEmployeeViewModel
import com.latihan.lalabib.e_karyawan.ui.detail.DetailViewModel
import com.latihan.lalabib.e_karyawan.ui.employee.EmployeeViewModel

class ViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EmployeeViewModel::class.java) -> {
                EmployeeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddEmployeeViewModel::class.java) -> {
                AddEmployeeViewModel(repository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(EmployeeRepository.getInstance(context))
        }
    }
}