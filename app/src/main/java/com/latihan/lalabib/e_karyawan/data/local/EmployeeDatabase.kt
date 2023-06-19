package com.latihan.lalabib.e_karyawan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.latihan.lalabib.e_karyawan.data.StartingEmployee
import com.latihan.lalabib.e_karyawan.data.StartingUser

@Database(
    entities = [EmployeeEntities::class, UserEntities::class],
    version = 3,
    exportSchema = false
)
abstract class EmployeeDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao

    companion object {
        @Volatile
        private var instance: EmployeeDatabase? = null

        fun getInstance(context: Context): EmployeeDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                EmployeeDatabase::class.java,
                "karyawan_db"
            )
                .addCallback(StartingEmployee(context))
                .addCallback(StartingUser(context))
                .fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}