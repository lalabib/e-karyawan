package com.latihan.lalabib.e_karyawan.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.latihan.lalabib.e_karyawan.data.local.EmployeeDao
import com.latihan.lalabib.e_karyawan.data.local.EmployeeDatabase
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.local.SalaryEntity
import com.latihan.lalabib.e_karyawan.data.local.UserEntities

class EmployeeRepository(private val employeeDao: EmployeeDao) {

    fun getEmployees(): LiveData<List<EmployeeEntities>> = employeeDao.getEmployees()

    fun getEmployeeById(employeeId: Int): LiveData<EmployeeEntities> =
        employeeDao.getEmployeeById(employeeId)

    fun getAllEmployeeByName(): LiveData<List<String>> =
        employeeDao.getAllEmployeeByName()

    fun getEmployeeByName(employeeName: String): LiveData<EmployeeEntities> =
        employeeDao.getEmployeeByName(employeeName)

    suspend fun insertEmployee(newEmployee: EmployeeEntities) {
        employeeDao.insertEmployee(newEmployee)
    }

    suspend fun deleteEmployee(employee: EmployeeEntities) {
        employeeDao.deleteEmployee(employee)
    }

    suspend fun updatedEmployee(employee: EmployeeEntities) {
        employeeDao.updateEmployee(employee)
    }

    fun getUser(): LiveData<List<UserEntities>> = employeeDao.getUser()

    fun getUserByName(username: String): LiveData<UserEntities> =
        employeeDao.getUserByName(username)

    suspend fun insertSalary(salary: SalaryEntity) = employeeDao.insertSalary(salary)

    companion object {
        @Volatile
        private var instance: EmployeeRepository? = null

        fun getInstance(context: Context): EmployeeRepository = instance ?: synchronized(this) {
            if (instance == null) {
                val database = EmployeeDatabase.getInstance(context)
                instance = EmployeeRepository(database.employeeDao())
            }
            return instance as EmployeeRepository
        }
    }
}
