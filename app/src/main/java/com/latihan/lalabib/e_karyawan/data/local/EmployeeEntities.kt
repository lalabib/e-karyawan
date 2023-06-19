package com.latihan.lalabib.e_karyawan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_karyawan")
data class EmployeeEntities(
    @PrimaryKey
    val id: Int,

    val nama: String,

    val alamat: String,

    val jenisKelamin: String,

    val jabatan: String,

    val lamaBekerja: String,

    val gajiPokok: Int
)

@Entity(tableName = "tb_user")
data class UserEntities(
    @PrimaryKey
    val id: Int,

    val nama: String,

    val password: String,
)