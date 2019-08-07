package com.example.camerasamplecamerakit

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    private var REQUEST_CODE_WRITE_TO_EXTERNAL_STORAGE = 1002
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionWork()

        videoRecordButton.setOnClickListener {
            //            cameraKitView.startVideo()
            cameraKitView.captureImage { p0, p1 ->
                notifyUserWithLog("inside captureImage")
                createPhotoFromByteArray(p1)

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permissionWork() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_TO_EXTERNAL_STORAGE
                );
            }
        }
    }

    private fun createPhotoFromByteArray(capturedImage: ByteArray?) {

        val savedPhoto = File(providePhotosPath(), "photo_${System.currentTimeMillis()}.jpg")
        notifyUserWithLog("savedPhoto path= ${savedPhoto.absolutePath} capturedImage=" + capturedImage?.size)

        try {
            val outputStream = FileOutputStream(savedPhoto.absolutePath)
            outputStream.write(capturedImage)
            outputStream.close()

            scanFile(savedPhoto)

        } catch (e: java.io.IOException) {
            e.printStackTrace()
        }

    }

    private fun scanFile(file: File) {
        notifyUserWithLog("starting media scan... ${file.absolutePath}")

        MediaScannerConnection.scanFile(
            applicationContext,
            arrayOf(file.absolutePath),
            null
        ) { path, uri ->
            if (file.exists()) {
                notifyUserWithLog("inside savedPhoto.exists() savedPhoto path= ${path}")
                var myBitmap: Bitmap = BitmapFactory.decodeFile(path)
                imageThumbnailView.setImageBitmap(myBitmap)
            }
            notifyUserWithLog("media scan completed")

        }
    }

    override fun onStart() {
        super.onStart()
        cameraKitView.onStart()
    }


    override fun onResume() {
        super.onResume()
        cameraKitView.onResume()
    }

    override fun onPause() {
        cameraKitView.onStop();
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        cameraKitView.onStop()
    }


}
