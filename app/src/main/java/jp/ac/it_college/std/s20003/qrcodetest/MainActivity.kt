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

    private lateinit var completeQR: String   // 完成形をここに入れる

    private var num1: Int = 0
    private var num3: Int = 0
    private var productView: String = ""

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
                            completeQR = code1 + code2
                            nameSearch()
                            daysSearch()
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
                            completeQR = code1 + code2 + code3
                            nameSearch()
                            daysSearch()
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
                            completeQR = code1 + code2 + code3 + code4
                            nameSearch()
                            daysSearch()
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
                            completeQR = code1 + code2 + code3 + code4 + code5
                            nameSearch()
                            daysSearch()
                        }
                        Toast.makeText(this, "５つ目のQRコードを読み取りました", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            if (resultQR.substring(0, 4) != "JAHIS") {
                Toast.makeText(this, "指定のQRコードを読み取ってください", Toast.LENGTH_LONG).show()
            } else {
                nameSearch()
                daysSearch()
            }
        }
    }

    // 薬名検索部分
    private fun nameSearch() {
        var startPoint: Int
        var namePosition = -1

        // 指定のQRコード以外を読み取ったときの処理を書く
        for (i in 1 until 10) {
            startPoint = namePosition + 1
            namePosition = completeQR.indexOf("201,", startPoint)
            if (namePosition == -1) {
                break
            }
            num1 = i
            val extractView = completeQR.substring(namePosition + 6)
            val commaPosition = extractView.indexOf(",", 6)
            if (commaPosition != -1) {
                productView = extractView.substring(0, commaPosition)
                textInput()
            } else {
                productView = extractView.substring(0)
                textInput()
            }
        }
    }

    private fun daysSearch() {
        var startPoint: Int
        var namePosition = -1
        for (i in 1 until 10) {
            startPoint = namePosition + 1
            namePosition = completeQR.indexOf("301,", startPoint)
            if (namePosition == -1) {
                break
            }
            num3 = i
            val extractView = completeQR.substring(namePosition + 6)
            val commaPosition1 = extractView.indexOf(",")
            val viewProduct = extractView.substring(0, commaPosition1 + 10)
            val daysPosition = viewProduct.indexOf("日分")
            if (daysPosition == -1) {
                break
            }
            val daysProduct = viewProduct.substring(daysPosition - 3)
            val commaPosition2 = daysProduct.indexOf(",")
            val dateView = daysProduct.substring(0, commaPosition2)
            productView = dateView
            daysInput()
        }
    }

    private fun textInput() {
        when (num1) {
            1 -> {
                binding.textView1.text = productView
            }
            2 -> {
                binding.textView2.text = productView
            }
            3 -> {
                binding.textView3.text = productView
            }
            4 -> {
                binding.textView4.text = productView
            }
            5 -> {
                binding.textView5.text = productView
            }
        }
    }

    private fun daysInput() {
        val day = if (productView != "") {
            "日分"
        } else {
            ""
        }
        when (num3) {
            1 -> {
                binding.daysView1.text = productView + day
            }
            2 -> {
                binding.daysView1.text = productView + day
            }
            3 -> {
                binding.daysView1.text = productView + day
            }
            4 -> {
                binding.daysView1.text = productView + day
            }
            5 -> {
                binding.daysView1.text = productView + day
            }
        }
    }
}