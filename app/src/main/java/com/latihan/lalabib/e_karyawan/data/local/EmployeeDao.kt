package com.latihan.lalabib.e_karyawan.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface EmployeeDao {

    @Query("Select * from tb_karyawan")
    fun getEmployees(): LiveData<List<EmployeeEntities>>

    @Query("Select * from tb_karyawan where id = :employeeId")
    fun getEmployeeById(employeeId: Int): LiveData<EmployeeEntities>

    @Query("Select nama from tb_karyawan")
    fun getAllEmployeeByName(): LiveData<List<String>>

    @Query("Select * from tb_karyawan where nama = :employeeName")
    fun getEmployeeByName(employeeName: String): LiveData<EmployeeEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeeEntities)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEmployee(vararg employees: EmployeeEntities)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntities)

    @Update
    suspend fun updateEmployee(employee: EmployeeEntities)

    @Query("Select * from tb_user")
    fun getUser(): LiveData<List<UserEntities>>

    @Query("Select * from tb_user where nama = :username")
    fun getUserByName(username: String): LiveData<UserEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUser(vararg user: UserEntities)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSalary(salary: SalaryEntity)
}