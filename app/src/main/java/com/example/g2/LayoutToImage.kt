package com.example.g2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LayoutToImage {
    companion object{
        public fun saveBitMap(context: Context, drawView: View): File? {
            val pictureFileDir =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Handcare")
            if (!pictureFileDir.exists()) {
                val isDirectoryCreated = pictureFileDir.mkdirs()
                if (!isDirectoryCreated) {
                    //Not created
                }
                return null
            }
            val fileName = pictureFileDir.path + File.separator + System.currentTimeMillis() + ".jpg";
            val pictureFile = File(fileName)
            val bitmap: Bitmap = getBitmapFromView(drawView)
            try {
                pictureFile.createNewFile()
                val oStream: FileOutputStream = FileOutputStream(pictureFile)
                bitmap.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    oStream
                )
                oStream.flush()
                oStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            scanGallery(context, pictureFile.absolutePath)
            return pictureFile
        }
        public fun scanGallery(context: Context, absolutePath: String) {
            try {
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf<String>(absolutePath),
                    null,
                    object : MediaScannerConnection.OnScanCompletedListener {
                        override fun onScanCompleted(p0: String?, p1: Uri?) {
                        }
                    }
                )
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        public fun getBitmapFromView(drawView: View): Bitmap {
            val returnedBitmap = Bitmap.createBitmap(drawView.width, drawView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(returnedBitmap)
            val bgDrawable = drawView.background
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            drawView.draw(canvas)
            return returnedBitmap
        }
    }


}