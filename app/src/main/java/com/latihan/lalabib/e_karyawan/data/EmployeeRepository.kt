package com.latihan.lalabib.e_karyawan.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.latihan.lalabib.e_karyawan.data.local.EmployeeDao
import com.latihan.lalabib.e_karyawan.data.local.EmployeeDatabase
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.local.UserEntities
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EmployeeRepository(
    private val employeeDao: EmployeeDao,
    private val executor: ExecutorService
) {

    fun getEmployees(): LiveData<List<EmployeeEntities>> = employeeDao.getEmployees()

    fun getEmployeeById(employeeId: Int): LiveData<EmployeeEntities> =
        employeeDao.getEmployeeById(employeeId)

    fun insertEmployee(newEmployee: EmployeeEntities): Long {
        val data = Callable { employeeDao.insertEmployee(newEmployee) }
        val executor = executor.submit(data)
        return executor.get()
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

    companion object {
        @Volatile
        private var instance: EmployeeRepository? = null

        fun getInstance(context: Context): EmployeeRepository = instance ?: synchronized(this) {
            if (instance == null) {
                val database = EmployeeDatabase.getInstance(context)
                instance = EmployeeRepository(
                    database.employeeDao(),
                    Executors.newSingleThreadExecutor()
                )
            }
            return instance as EmployeeRepository
        }
    }
}
