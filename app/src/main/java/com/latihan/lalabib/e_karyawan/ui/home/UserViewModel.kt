package com.latihan.lalabib.e_karyawan.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.latihan.lalabib.e_karyawan.data.EmployeeRepository
import com.latihan.lalabib.e_karyawan.data.local.UserEntities

class UserViewModel(private val repository: EmployeeRepository) : ViewModel() {

    fun getUser(): LiveData<List<UserEntities>> = repository.getUser()

    private val _userName = MutableLiveData<String>()

    private val _username = _userName.switchMap { username ->
        repository.getUserByName(username)
    }
    val username: LiveData<UserEntities> = _username

    fun setUsername(username: String) {
        if (username == _userName.value) {
            return
        }
        _userName.value = username
    }
}