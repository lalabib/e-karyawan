package com.latihan.lalabib.e_karyawan.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.local.EmployeeDatabase
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class StartingEmployee(private val context: Context): RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            fillWithStartingDataEmployee(context)
        }
    }

    //Filling database with the data from JSON
    private fun fillWithStartingDataEmployee(context: Context) {
        //obtaining instance of data access object
        val dao = EmployeeDatabase.getInstance(context).employeeDao()
        val jsonArray = loadJsonArrayEmployee(context)

        // using try catch to load the necessary data
        try {
            if (jsonArray != null) {
                for (i in 0 until jsonArray.length()) {
                    //variable to obtain the JSON object
                    val item = jsonArray.getJSONObject(i)
                    //Using the JSON object to assign data, loaded to entity and insert to db
                    dao.insertAllEmployee(
                        EmployeeEntities(
                            item.getInt("id"),
                            item.getString("nama"),
                            item.getString("alamat"),
                            item.getString("jenisKelamin"),
                            item.getString("jabatan"),
                            item.getString("lamaBekerja"),
                            item.getInt("gajiPokok")
                        )
                    )
                }
            }
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
    }

    //read json array and load it to tb_karyawan
    private fun loadJsonArrayEmployee(context: Context): JSONArray? {
        val builder = StringBuilder()
        val `in` = context.resources.openRawResource(R.raw.karyawan)
        //using Buffered reader to read the input stream byte
        val reader = BufferedReader(InputStreamReader(`in`))
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                builder.append(line)
            }
            val json = JSONObject(builder.toString())
            return json.getJSONArray("tb_karyawan")
        } catch (exception: IOException) {
            exception.printStackTrace()
        } catch (exception: JSONException) {
            exception.printStackTrace()
        }
        return null
    }
}