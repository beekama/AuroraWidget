package com.example.aurorawidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.io.File
import java.util.concurrent.TimeUnit
 

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val updateButton: Button = findViewById(R.id.updateButton)
        val imageView: ImageView = findViewById(R.id.imageView)

        loadImageFromStorage(imageView)

        updateButton.setOnClickListener {
            triggerImageDownload()
            loadImageFromStorage(imageView)
        }

        // WorkManager - download image in background every 3 hours (also when app is closed)
        startImageDownloadWorker()
    }

    private fun startImageDownloadWorker() {
        val downloadRequest = PeriodicWorkRequestBuilder<ImageDownloadWorker>(3, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueue(downloadRequest)
    }

    private fun triggerImageDownload(){
        val downloadRequest: WorkRequest = OneTimeWorkRequest.Builder(ImageDownloadWorker::class.java).build()
        WorkManager.getInstance(this).enqueue(downloadRequest)
    }

    private fun loadImageFromStorage(imageView: ImageView){
        val imgFile = File(filesDir, "aurora_forecast.png")
        if (imgFile.exists()){
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

            imageView.setImageBitmap(bitmap)

        }
    }
}