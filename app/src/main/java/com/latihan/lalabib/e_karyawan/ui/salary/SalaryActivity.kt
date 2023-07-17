package com.latihan.lalabib.e_karyawan.ui.salary

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.latihan.lalabib.e_karyawan.R
import com.latihan.lalabib.e_karyawan.data.local.EmployeeEntities
import com.latihan.lalabib.e_karyawan.data.local.SalaryEntity
import com.latihan.lalabib.e_karyawan.databinding.ActivitySalaryBinding
import com.latihan.lalabib.e_karyawan.utils.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt

class SalaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySalaryBinding
    private lateinit var salaryViewModel: SalaryViewModel
    private lateinit var employeeEntities: EmployeeEntities
    private lateinit var salaryEntity: SalaryEntity

    // creating a bitmap variable
    // for storing our images
    private lateinit var bmp: Bitmap
    private lateinit var scaledBmp: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // on below line we are initializing our bitmap and scaled bitmap.
        bmp = BitmapFactory.decodeResource(resources, R.drawable.gedung)
        scaledBmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        setupView()
        setupViewMode()
        permission()
        setupData()
        setupAction()
    }

    private fun setupView() {
        supportActionBar?.apply {
            title = getString(R.string.title_salary)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setupViewMode() {
        val factory = ViewModelFactory.getInstance(this)
        salaryViewModel = ViewModelProvider(this, factory)[SalaryViewModel::class.java]
    }

    private fun setupData() {
        //dropdown name by employee name
        val employeeNameList = ArrayList<String>()

        salaryViewModel.getEmployeeName().observe(this) { employeeName ->
            employeeNameList.addAll(employeeName)
            val arrayAdapterName = ArrayAdapter(this, R.layout.dropdown_item, employeeNameList)
            binding.edtName.setAdapter(arrayAdapterName)

            binding.edtName.setOnItemClickListener { _, _, position, _ ->
                val selectedName = employeeNameList[position]
                salaryViewModel.setName(selectedName)
            }
        }

        salaryViewModel.employee.observe(this) { employee ->
            if (employee != null) {
                employeeEntities = employee

                binding.edtAddress.setText(employee.alamat)
                binding.edtGender.setText(employee.jenisKelamin)
                binding.edtPosition.setText(employee.jabatan)
                binding.edtSalary.setText(employee.gajiPokok.toString())

                sumSalaries()
            }
        }

        //dropdown bulan
        val month = resources.getStringArray(R.array.month)
        val arrayAdapterMonth = ArrayAdapter(this, R.layout.dropdown_item, month)
        binding.edtMonth.setAdapter(arrayAdapterMonth)
    }

    private fun sumSalaries() {
        //sum salaries by position
        val position = binding.edtPosition.text.toString()
        val salary = binding.edtSalary.text.toString().toInt()
        val bonus = binding.edtBonus
        val pphSalary = binding.edtPph
        val sumSalaries = binding.edtTotalGaji

        val bonusManager = (salary * managerBonus).roundToInt()
        val bonusSpv = (salary * spvBonus).roundToInt()
        val bonusStaff = (salary * staffBonus).roundToInt()
        val tax = (salary * pph).roundToInt()

        when (position) {
            getString(R.string.manager) -> {
                bonus.setText(bonusManager.toString())
                pphSalary.setText(tax.toString())
                val sum = salary + bonusManager - tax
                sumSalaries.setText(sum.toString())
            }

            getString(R.string.spv) -> {
                bonus.setText(bonusSpv.toString())
                pphSalary.setText(tax.toString())
                val sum = salary + bonusSpv - tax
                sumSalaries.setText(sum.toString())
            }

            getString(R.string.staff) -> {
                bonus.setText(bonusStaff.toString())
                pphSalary.setText(tax.toString())
                val sum = salary + bonusStaff - tax
                sumSalaries.setText(sum.toString())
            }
        }
    }

    private fun saveSalaries() {
        val name = binding.edtName.text.toString()
        val month = binding.edtMonth.text.toString()
        val salary = binding.edtSalary.text.toString()
        val bonus = binding.edtBonus.text.toString()
        val pph = binding.edtPph.text.toString()
        val sumSalary = binding.edtTotalGaji.text.toString()

        if (name.isNotEmpty() && month.isNotEmpty() && bonus.isNotEmpty() && pph.isNotEmpty()
            && salary.isNotEmpty() && sumSalary.isNotEmpty()
        ) {
            val salaries = SalaryEntity(
                UUID.randomUUID().toString(),
                name,
                month,
                salary.toInt(),
                bonus.toInt(),
                pph.toInt(),
                sumSalary.toInt()
            )
            salaryViewModel.insertSalaries(salaries)
            salaryEntity = salaries
            generatePdf()
            Toast.makeText(this@SalaryActivity, R.string.saved, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@SalaryActivity, R.string.cant_empty, Toast.LENGTH_SHORT).show()
        }
    }


    // on below line we are creating a generate PDF method
    // which is use to generate our PDF file.
    private fun generatePdf() {
        // creating an object variable
        // for our PDF document.
        val pdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val paint = Paint()
        val title = Paint()

        // format to convert number to currency
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "id"))

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        val myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        val myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledBmp, 56F, 40F, paint)

        // below line is used for adding typeface for our text which we will be adding in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        title.textSize = 40F
        canvas.drawText(getString(R.string.pt_name), 210F, 150F, title)

        // write text in our pdf
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.textSize = 20F
        canvas.drawText(getString(R.string.title_pdf), 20F, 260F, title)

        // write name of employee
        canvas.drawText(getString(R.string.name_title), 20F, 300F, title)
        canvas.drawText(employeeEntities.nama, 170F, 300F, title)

        // write gender
        canvas.drawText(getString(R.string.gender_title), 20F, 340F, title)
        canvas.drawText(employeeEntities.jenisKelamin, 170F, 340F, title)

        // write address
        canvas.drawText(getString(R.string.address_title), 20F, 380F, title)
        canvas.drawText(employeeEntities.alamat, 170F, 380F, title)

        // write position
        canvas.drawText(getString(R.string.position_title), 20F, 420F, title)
        canvas.drawText(employeeEntities.jabatan, 170F, 420F, title)

        // write month
        canvas.drawText(getString(R.string.month_title), 20F, 460F, title)
        canvas.drawText(salaryEntity.bulan, 170F, 460F, title)

        // write salary and convert to currency
        canvas.drawText(getString(R.string.salary_title), 20F, 500F, title)
        val hasilFormatGaji = formatter.format(salaryEntity.gajiPokok.toString().toDouble())
        canvas.drawText(hasilFormatGaji.substringBefore(","), 170F, 500F, title)

        // write bonus and convert to currency
        canvas.drawText(getString(R.string.bonus_title), 20F, 540F, title)
        val hasilFormatBonus = formatter.format(salaryEntity.bonus.toString().toDouble())
        canvas.drawText(hasilFormatBonus.substringBefore(","), 170F, 540F, title)

        // write pph and convert to currency
        canvas.drawText(getString(R.string.pph_title), 20F, 580F, title)
        val hasilFormatPph = formatter.format(salaryEntity.pph.toString().toDouble())
        canvas.drawText(hasilFormatPph.substringBefore(","), 170F, 580F, title)

        // write sum salary and convert to currency
        canvas.drawText(getString(R.string.sum_salary_title), 20F, 620F, title)
        val hasilFormatTotalGaji = formatter.format(salaryEntity.totalGaji.toString().toDouble())
        canvas.drawText(hasilFormatTotalGaji.substringBefore(","), 170F, 620F, title)

        // after adding all attributes to our PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of our PDF file and its path.
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Gaji ${employeeEntities.nama + " " + salaryEntity.bulan}.pdf"
        )

        try {
            // after creating a file name we will write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(this@SalaryActivity, R.string.pdf_geenerated, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used to handle error
            e.printStackTrace()

            // on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(this@SalaryActivity, R.string.pdf_geenerated_fail, Toast.LENGTH_SHORT)
                .show()
        }
        // after storing our pdf to that location we are closing our PDF file.
        pdfDocument.close()

        val uri = FileProvider.getUriForFile(
            this@SalaryActivity,
            "com.your.package.name.fileprovider",
            file
        )

        // Create a PendingIntent for opening the PDF file
        val openPdfIntent = Intent(Intent.ACTION_VIEW)
        openPdfIntent.setDataAndType(uri, "application/pdf")
        openPdfIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            this@SalaryActivity,
            0,
            openPdfIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a notification channel if running on Android Oreo or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.notif_id)
            val channelName = getString(R.string.ch_name)
            val channelDescription = getString(R.string.desc_ch)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            // Register the notification channel with the system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            // Create a notification using NotificationCompat.Builder
            val notificationBuilder = NotificationCompat.Builder(this@SalaryActivity, getString(R.string.notif_id))
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_msg))
                .setSmallIcon(R.drawable.ic_download)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            // Show the notification
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

    // on below line we are calling on request permission result.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionGranted()) {
                Toast.makeText(this@SalaryActivity, R.string.permission_cancel, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun permission() {
        if (!allPermissionGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupAction() {
        binding.btnSum.setOnClickListener { saveSalaries() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val pph = 0.05
        const val managerBonus = 0.5
        const val spvBonus = 0.4
        const val staffBonus = 0.3

        // declaring width and height
        // for our PDF file.
        var pageHeight = 1120
        var pageWidth = 792

        // on below line we are creating a constant code for runtime permissions.
        private val REQUIRED_PERMISSIONS = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}