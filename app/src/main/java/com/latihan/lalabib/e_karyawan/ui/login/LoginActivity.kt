package com.latihan.lalabib.e_karyawan.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.local.UserEntities
import com.latihan.lalabib.e_karyawan.databinding.ActivityLoginBinding
import com.latihan.lalabib.e_karyawan.ui.home.HomeActivity
import com.latihan.lalabib.e_karyawan.ui.home.UserViewModel
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: UserViewModel
    private lateinit var user: UserEntities

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

       loginViewModel.getUser().observe(this) {users ->
           this.user = users
      }
    }

    private fun setupLogin() {
        binding.apply {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            when {
                username.isEmpty() -> {
                    tfUsername.error = getString(R.string.cant_empty)
                }

                password.isEmpty() -> {
                    tfPassword.error = getString(R.string.cant_empty)
                }

                username != user.nama -> {
                    tfUsername.error = getString(R.string.username_invalid)
                }

                password != user.password -> {
                    tfPassword.error = getString(R.string.pass_invalid)
                }

                else -> {
                    val newIsLogin = true
                    loginViewModel.checkLogin(newIsLogin, user.nama)
                    Toast.makeText(
                        this@LoginActivity,
                        R.string.login_success,
                        Toast.LENGTH_SHORT
                    ).show()
                    moveToHome()
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener { setupLogin() }
    }

    private fun moveToHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}