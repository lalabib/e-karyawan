package com.latihan.lalabib.e_karyawan.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.local.UserEntities
import com.latihan.lalabib.e_karyawan.databinding.ActivityHomeBinding
import com.latihan.lalabib.e_karyawan.ui.add.AddEmployeeActivity
import com.latihan.lalabib.e_karyawan.ui.employee.EmployeeActivity
import com.latihan.lalabib.e_karyawan.ui.login.LoginActivity
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        moveToEmployee()
        moveToAddEmployee()
        // move to hitungGaji()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        val username = intent.getStringExtra(extra_data)
        if (username != null) {
            userViewModel.setUsername(username)
            userViewModel.username.observe(this) { users ->
                if (users.nama == getString(R.string.admin)) {
                    admin()
                } else if (users.nama == getString(R.string.hrd)) {
                    hrd()
                }
            }
        }
    }

    //login as admin
    private fun admin() {
        binding.menu.llTambahKaryawan.visibility = View.VISIBLE
        binding.menu.llGaji.visibility = View.GONE
    }

    //login as hrd
    private fun hrd() {
        binding.menu.llTambahKaryawan.visibility = View.GONE
        binding.menu.llGaji.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun moveToEmployee() {
        binding.menu.llKaryawan.setOnClickListener {
            startActivity(Intent(this@HomeActivity, EmployeeActivity::class.java))
        }
    }

    private fun moveToAddEmployee() {
        binding.menu.llTambahKaryawan.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AddEmployeeActivity::class.java))
        }
    }

    private fun logout() {
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()

    }

    companion object {
        const val extra_data = "user"
    }
}