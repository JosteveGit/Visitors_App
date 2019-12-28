package com.example.g2

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.print.PrintHelper
import com.example.g2.database.Visitors
import com.example.g2.database.VisitorsViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.android.synthetic.main.activity_image_capture.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class ImageCaptureActivity : AppCompatActivity() {

    override fun onBackPressed() {
        startActivity(Intent(this,ChangeActivity::class.java))
        finish()
    }

    private val CAMERA_REQUEST = 0
    private var thumbnail: Bitmap? = null
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

        setContentView(R.layout.activity_image_capture)

        val intent: Intent = intent
        val name: String = intent.getStringExtra("name")!!
        val address: String = intent.getStringExtra("address")!!
        val phoneNumber: String = intent.getStringExtra("phoneNumber")!!
        val nationality: String = intent.getStringExtra("nationality")!!
        val purpose: String = intent.getStringExtra("purpose")!!
        val allData = "{\n Name: $name,\n Gender: $nationality,\n Phone: $phoneNumber,\n Address: $address,\n Nationality: $nationality,\n Purpose: $purpose}"

        val bitmap = textToImageEncode(MyCipher.encrypt(allData, cipherOf = 100))
        qr.setImageBitmap(bitmap)

        name2.text = "Name: $name"
        address2.text ="Address: $address"
        phone2.text = "Phone: $phoneNumber"
        nationality2.text ="Nationality: $nationality"
        purpose2.text = "Purpose: $purpose"



        nextToImageCaptureButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        retakeButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        fun toggle(){
            if(takePicture.visibility==View.VISIBLE){
                takePicture.visibility = View.GONE
                idCardGenerator.visibility = View.VISIBLE
            }else{
                takePicture.visibility = View.VISIBLE
                idCardGenerator.visibility = View.GONE
            }
        }

        cancel_button.setOnClickListener {
//            toggle()
            startActivity(Intent(this,ChangeActivity::class.java))
            finish()
        }


        generateIdButton.setOnClickListener {
            toggle()
        }

        fun printImage(imageToPrint: Bitmap) {
            PrintHelper(this).apply {
                scaleMode = PrintHelper.SCALE_MODE_FIT
            }.also {
                it.printBitmap("idcard.jpg - test print", imageToPrint)
            }
        }
        printButton2.setOnClickListener {
            val imageToPrint = LayoutToImage.getBitmapFromView(idCard2)
            val viewModel = ViewModelProvider(this).get(VisitorsViewModel::class.java)
            val date = LocalDate.now()
            val time = SimpleDateFormat("hh:mm a").format(Calendar.getInstance().time)
            val dateAsString = "${date.dayOfMonth} ${getMonth(date.monthValue)} ${date.year}"
            viewModel.insert(Visitors(
                name = name,
                phoneNumber = phoneNumber,
                gender = null,
                address = address,
                stateOfOrigin = null,
                nationality = nationality,
                arrivingFrom = null,
                purposeOfvisit = purpose,
                accompanyWith = null,
                date = dateAsString,
                time = time,
                occupation = null
            ))
            printImage(imageToPrint)
        }
    }


    val getMonth = fun(value: Int): String = when (value-1) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Nov"
        11 -> "Dec"
        else -> "Error"
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, CAMERA_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            thumbnail = data!!.extras!!.get("data") as Bitmap
            peronImage.setImageBitmap(thumbnail)
            person2.setImageBitmap(thumbnail)
            misc.visibility = View.VISIBLE
            nextToImageCaptureButton.visibility = View.GONE
        }
    }

    @Throws(WriterException::class)
    private fun textToImageEncode(value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                value,
                BarcodeFormat.QR_CODE,
                QRcodeWidth, QRcodeWidth, null
            )

        } catch (Illegalargumentexception: IllegalArgumentException) {

            return null
        }

        val bitMatrixWidth = bitMatrix.getWidth()

        val bitMatrixHeight = bitMatrix.getHeight()

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    resources.getColor(R.color.black)
                else
                    resources.getColor(R.color.white)
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }

    companion object {
        val QRcodeWidth = 500
    }


}
