package com.latihan.lalabib.e_karyawan.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupAction()
        permission()
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


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    // on below line we are calling on request permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this@LoginActivity, R.string.permission_cancel, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun permission() {
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener { setupLogin() }
    }

    companion object {
        // on below line we are creating a constant code for runtime permissions.
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS
        )
        private const val REQUEST_CODE_PERMISSIONS = 110
    }
}