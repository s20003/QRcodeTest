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

    private lateinit var hex1: String
    private lateinit var hex2: String

    private lateinit var perfectQR: String   // 完成形をここに入れる

    private var num: Int = 0
    private var viewProduct: String = ""

    private var code1 = ""
    private var code2 = ""
    private var code3 = ""
    private var code4 = ""
    private var code5 = ""

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
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        // QRコードのバイナリ読み取り
        val rawQRCode = result.rawBytes
        hex1 = String.format("%x", rawQRCode[0])
        hex2 = String.format("%x", rawQRCode[1])

        resultQR = result.contents.toString()
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            binaryReading()
        }
    }

    // Binaryデータ読み取り・データ連結部分
    private fun binaryReading() {
        if (hex1.startsWith("3")) {
            when(hex1.substring(1, 2)) {
                "0" -> {
                    code1 = resultQR
                    Toast.makeText(this, "１つ目のQRコードを読み取りました", Toast.LENGTH_LONG).show()
                }
                "1" -> {
                    if (code1 == "") {
                        Toast.makeText(this, "順番通りに読み取ってください", Toast.LENGTH_LONG).show()
                    } else {
                        code2 = resultQR
                        if (hex2.startsWith("1")) {
                            perfectQR = code1 + code2
                            search()
                        }
                        Toast.makeText(this, "２つ目のQRコードを読み取りました", Toast.LENGTH_LONG).show()
                    }
                }
                "2" -> {
                    if (code1 == "" || code2 == "") {
                        Toast.makeText(this, "順番通りに読み取ってください", Toast.LENGTH_LONG).show()
                    } else {
                        code3 = resultQR
                        if (hex2.startsWith("2")) {
                            perfectQR = code1 + code2 + code3
                            search()
                        }
                        Toast.makeText(this, "３つ目のQRコードを読み取りました", Toast.LENGTH_LONG).show()
                    }
                }
                "3" -> {
                    if (code1 == "" || code2 == "" || code3 == "") {
                        Toast.makeText(this, "順番通りに読み取ってください", Toast.LENGTH_LONG).show()
                    } else {
                        code4 = resultQR
                        if (hex2.startsWith("3")) {
                            perfectQR = code1 + code2 + code3 + code4
                            search()
                        }
                        Toast.makeText(this, "４つ目のQRコードを読み取りました", Toast.LENGTH_LONG).show()
                    }
                }
                "4" -> {
                    if (code1 == "" || code2 == "" || code3 == "" || code4 == "") {
                        Toast.makeText(this, "順番通りに読み取ってください", Toast.LENGTH_LONG).show()
                    } else {
                        code5 = resultQR
                        if (hex2.startsWith("4")) {
                            perfectQR = code1 + code2 + code3 + code4 + code5
                            search()
                        }
                        Toast.makeText(this, "５つ目のQRコードを読み取りました", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            if (resultQR.substring(0, 4) != "JAHIS") {
                Toast.makeText(this, "指定のQRコードを読み取ってください", Toast.LENGTH_LONG).show()
            } else {
                search()
            }
        }
    }

    // 薬名検索部分
    private fun search() {
        var startPoint: Int
        var namePosition = -1

        // 指定のQRコード以外を読み取ったときの処理を書く
        for (i in 1 until 10) {
            startPoint = namePosition + 1
            namePosition = perfectQR.indexOf("201,", startPoint)
            if (namePosition == -1) {
                break
            }
            num = i
            val extractView = perfectQR.substring(namePosition + 6)
            val commaPosition = extractView.indexOf(",", 6)
            if (commaPosition != -1) {
                viewProduct = extractView.substring(0, commaPosition)
                textInput()
            } else {
                viewProduct = extractView.substring(0)
                textInput()
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
            4 -> {
                binding.textView4.text = viewProduct
            }
            5 -> {
                binding.textView5.text = viewProduct
            }
        }
    }
}