package com.latihan.lalabib.e_karyawan.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface EmployeeDao {

    @Query("Select * from tb_karyawan")
    fun getEmployees(): LiveData<List<EmployeeEntities>>

    @Query("Select * from tb_karyawan where id = :employeeId")
    fun getEmployeeById(employeeId: Int): LiveData<EmployeeEntities>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEmployee(employee: EmployeeEntities): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg employees: EmployeeEntities)

    @Delete
    suspend fun deleteEmployee(employee: EmployeeEntities)

    @Update
    fun updateEmployee(employee: EmployeeEntities)
}