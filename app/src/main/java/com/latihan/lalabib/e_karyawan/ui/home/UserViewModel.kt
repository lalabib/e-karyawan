package com.latihan.lalabib.e_karyawan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository
import com.latihan.lalabib.e_karyawan.data.local.UserEntities
import kotlinx.coroutines.launch

class UserViewModel(private val repository: EmployeeRepository): ViewModel() {

    fun getUser(): LiveData<UserEntities> = repository.getUser()

//    fun getAdmin(): LiveData<UserEntities> = repository.getUserByUsername(admin)

//    fun getHrd(): LiveData<UserEntities> = repository.getUserByUsername(hrd)

    fun checkLogin(isLogin: Boolean, username: String) {
        viewModelScope.launch {
            repository.checkLogin(isLogin, username)
        }
    }

    companion object {
        const val admin = "admin"
        const val hrd = "hrd"
    }
}