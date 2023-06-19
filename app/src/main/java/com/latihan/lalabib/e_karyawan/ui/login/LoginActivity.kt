package com.latihan.lalabib.e_karyawan.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.databinding.ActivityLoginBinding
import com.latihan.lalabib.e_karyawan.ui.home.HomeActivity
import com.latihan.lalabib.e_karyawan.ui.home.HomeActivity.Companion.extra_data
import com.latihan.lalabib.e_karyawan.ui.home.UserViewModel
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    private fun setupLogin() {
        binding.apply {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            loginViewModel.getUser().observe(this@LoginActivity) { users ->
                for (user in users) {
                    if (username == user.nama && password == user.password) {
                        Toast.makeText(
                            this@LoginActivity,
                            R.string.login_success,
                            Toast.LENGTH_SHORT
                        ).show()
                        Intent(this@LoginActivity, HomeActivity::class.java).apply {
                            putExtra(extra_data, user.nama)
                            startActivity(this)
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener { setupLogin() }
    }
}