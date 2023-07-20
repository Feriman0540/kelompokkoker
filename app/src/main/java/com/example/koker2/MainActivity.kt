package com.example.koker2

import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import android.widget.TextView
import android.net.Uri
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.view.Menu
import android.view.MenuItem







class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null
    private var outputTextView: TextView? = null
    private var isPingRunning: Boolean = false
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        outputTextView = findViewById(R.id.outputTextView)

        button1!!.setOnClickListener(this)
        button2!!.setOnClickListener(this)
        button3!!.setOnClickListener(this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item -> {
                showAlertDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button1 ->

                if (Build.VERSION.SDK_INT < 29) {
                    openMenu("com.android.settings", "com.android.settings.RadioInfo")
                } else {
                    openMenu("com.android.phone", "com.android.phone.settings.RadioInfo")
                }


            R.id.button2 -> {
                if (!isPingRunning) {
                    isPingRunning = true
                    button2?.text = "Stop Ping"
                    val ipAddress = "1.1.1.1" // Ganti dengan alamat IP yang ingin Anda ping
                    startPing(ipAddress)
                } else {
                    isPingRunning = false
                    button2?.text = "Start Ping"
                    job?.cancel()
                }

            }

            R.id.button3 -> {
                performSpeedTest()
            }

        }

    }


    private fun openMenu(packageName: String, ClassName: String) {
        val menu = Intent("android.intent.action.MAIN")
        menu.setClassName(packageName, ClassName)
        startActivity(menu)
        finish()


    }

    private fun startPing(ipAddress: String) {
        job = coroutineScope.launch {
            while (isPingRunning) {
                val output = pingIpAddress(ipAddress)
                withContext(Dispatchers.Main) {
                    outputTextView?.append(output + "\n")
                }
                delay(1000) // Jeda 1 detik sebelum melakukan ping berikutnya
            }
        }
    }

    private fun pingIpAddress(ipAddress: String): String {
        return try {
            val address = InetAddress.getByName(ipAddress)
            if (address.isReachable(3000)) {
                "ping sukses, semoga ping stabil buat ML :)"
            } else {
                "ping sukses, semoga ping stabil buat ML :)"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        }
    }
    private fun performSpeedTest() {
        val url = "https://openspeedtest.com/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Tutor")
        builder.setMessage("Tutor Paksa 4G:\n1. Cari Opsi Set Preferred Network Type\n2. Pilih LTE Only")

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}



