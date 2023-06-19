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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployee(employee: EmployeeEntities): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllEmployee(vararg employees: EmployeeEntities)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntities)

    @Update
    suspend fun updateEmployee(employee: EmployeeEntities)

    @Query("Select * from tb_user")
    fun getUser(): LiveData<UserEntities>

    @Query("Select * from tb_user where nama = :username")
    fun getUserByUsername(username: String): LiveData<UserEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUser(vararg user: UserEntities)

    @Query("Update tb_user set isLogin = :isLogin where nama = :username")
    suspend fun checkLogin(isLogin: Boolean, username: String)
}