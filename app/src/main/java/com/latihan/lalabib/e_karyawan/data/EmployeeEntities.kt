package com.latihan.lalabib.e_karyawan.data

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