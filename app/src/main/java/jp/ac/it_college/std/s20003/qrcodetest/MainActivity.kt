@file:Suppress("DEPRECATION")

package jp.ac.it_college.std.s20003.qrcodetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import jp.ac.it_college.std.s20003.qrcodetest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var resultQR: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ボタンを押した際にQRコード画面に遷移する
        binding.qrButton.setOnClickListener {
            IntentIntegrator(this).apply {
                setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                setBeepEnabled(false)
                setPrompt("QR Code Scan")
                setTorchEnabled(true)
            }.initiateScan()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        resultQR = result.contents.toString()
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            search()
        }
    }

    private fun search() {
        /*
        var startPoint: Int
        var namePosition = 0
         */

        val namePosition1 = resultQR.indexOf("201,")
        if (namePosition1 == -1) {
            Toast.makeText(this, "指定のQRコードを読み取ってください", Toast.LENGTH_LONG).show()
        } else {
            val viewTest1 = resultQR.substring(namePosition1 + 6)
            val commaPosition1 = viewTest1.indexOf(",", 6)
            val viewProduction1 = viewTest1.substring(0, commaPosition1)

            val namePosition2 = resultQR.lastIndexOf("201,")
            val viewTest2 = resultQR.substring(namePosition2 + 6)
            val commaPosition2 = viewTest2.indexOf(",", 6)
            val viewProduction2 = viewTest2.substring(0, commaPosition2)

            if (viewProduction1 != viewProduction2) {
                // Toast.makeText(this, "Scanned: ${viewProduction1},${viewProduction2}", Toast.LENGTH_LONG).show()
                binding.textView1.text = viewProduction1
                binding.textView2.text = viewProduction2
            } else {
                // Toast.makeText(this, "Scanned: $viewProduction1", Toast.LENGTH_LONG).show()
                binding.textView1.text = viewProduction1
            }
        }

        /*
        for (i in 0 until 10) {
            startPoint = namePosition + 1
            namePosition = resultQR.indexOf("201,", startPoint)
            if (namePosition == -1) {
                break
            }
            val viewTest = resultQR.substring(namePosition + 6)
            val commaPosition = viewTest.indexOf(",", 6)
            val viewProduct = resultQR.substring(0, commaPosition)
        }

         */

    }
}