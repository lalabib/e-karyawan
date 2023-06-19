package com.latihan.lalabib.e_karyawan.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
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
    private lateinit var user: UserEntities

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

        userViewModel.getUser().observe(this) { users ->
            this.user = users
            if (users.isLogin) {
                if (users.nama == getString(R.string.admin)) {
                    admin()
                } else if (users.nama == getString(R.string.hrd)) {
                    hrd()
                }
            } else {
                moveToLogin()
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

    private fun logout() {
        val builder = AlertDialog.Builder(this)
        val dialog = builder.setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.logout_msg))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                val newIsLogin = false
                userViewModel.checkLogin(newIsLogin, user.nama)
                finish()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()

            }
            .create()
        dialog.show()
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

    private fun moveToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    companion object {
        const val extra_data = "user"
    }
}