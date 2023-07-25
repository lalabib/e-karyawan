package com.latihan.lalabib.e_karyawan.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.databinding.ActivityHomeBinding
import com.latihan.lalabib.e_karyawan.ui.add.AddEmployeeActivity
import com.latihan.lalabib.e_karyawan.ui.employee.EmployeeActivity
import com.latihan.lalabib.e_karyawan.ui.employee.EmployeeActivity.Companion.extra_username
import com.latihan.lalabib.e_karyawan.ui.login.LoginActivity
import com.latihan.lalabib.e_karyawan.ui.salary.SalaryActivity
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        moveToAddEmployee()
        moveToSalary()
        setupAction()
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

        //setup greeting text
        binding.tvGreeting.text = getString(R.string.greeting, username)

        //move to employee activity with username data
        binding.menu.llKaryawan.setOnClickListener {
            Intent(this@HomeActivity, EmployeeActivity::class.java).apply {
                putExtra(extra_username, username)
                startActivity(this)
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

    private fun moveToAddEmployee() {
        binding.menu.llTambahKaryawan.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AddEmployeeActivity::class.java))
        }
    }

    private fun moveToSalary() {
        binding.menu.llGaji.setOnClickListener {
            startActivity(Intent(this@HomeActivity, SalaryActivity::class.java))
        }
    }

    private fun logout() {
        AlertDialog.Builder(this@HomeActivity).apply {
            setTitle(getString(R.string.confirm))
            setMessage(getString(R.string.logout_msg))
            setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
                dialog.cancel()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
            create()
            show()
        }
    }

    private fun setupAction() {
        binding.icLogout.setOnClickListener { logout() }
    }

    companion object {
        const val extra_data = "user"
    }
}