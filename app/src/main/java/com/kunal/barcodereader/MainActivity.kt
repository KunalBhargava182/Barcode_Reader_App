package com.kunal.barcodereader

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import com.journeyapps.barcodescanner.CompoundBarcodeView

class MainActivity : AppCompatActivity() {
    private lateinit var barcodeView: CompoundBarcodeView
    private lateinit var scanButton: Button
    private lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        barcodeView = findViewById(R.id.barcode_scanner)
        scanButton = findViewById(R.id.scan_button)
        resultTextView = findViewById(R.id.result_text_view)

        // Request camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startScanning()
        }

        // Set click listener for the scan button
        scanButton.setOnClickListener {
            barcodeView.decodeSingle { result ->
                result?.let {
                    resultTextView.text = it.text
                    // Process the scanned result here
                }
            }
        }
    }

    private fun startScanning() {
        barcodeView.resume()
        barcodeView.decodeContinuous { result ->
            result?.let {
                resultTextView.text = it.text
                // Process the scanned result here
            }
        }
    }

    override fun onResume() {
        super.onResume()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
                // Handle denied permission
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
