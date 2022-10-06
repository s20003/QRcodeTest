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

    private var num: Int = 0
    private var viewProduct: String = ""
    // private var incomplete: String = ""

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

    // QRコード読み込み部分
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // ↓ QRコード結果受取
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        // ↓ にgetRawBytesメソッドをつかう？
        resultQR = result.contents.toString()
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            search()
        }
    }

    // 薬名検索部分
    private fun search() {
        var startPoint: Int
        var namePosition = -1

        for (i in 1 until 10) {
            startPoint = namePosition + 1
            namePosition = resultQR.indexOf("201,", startPoint)
            if (namePosition == -1) {
                break
            }
            num = i
            val extractView = resultQR.substring(namePosition + 6)
            val commaPosition = extractView.indexOf(",", 6)
            if (commaPosition != -1) {
                viewProduct = extractView.substring(0, commaPosition)
                textInput()
            } else if (commaPosition == -1) {
                viewProduct = extractView.substring(0)
                textInput()
                // Toast.makeText(this, "次のQRコードを読み取ってください", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "その他", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun textInput() {
        when (num) {
            1 -> {
                binding.textView1.text = viewProduct
            }
            2 -> {
                binding.textView2.text = viewProduct
            }
            3 -> {
                binding.textView3.text = viewProduct
            }
        }
    }
}