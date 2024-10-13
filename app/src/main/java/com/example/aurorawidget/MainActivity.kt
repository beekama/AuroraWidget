package com.example.aurorawidget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
 

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        // WorkManager - download image in background every 3 hours (also when app is closed)
        startImageDownloadWorker()
    }

    private fun startImageDownloadWorker() {
        val downloadRequest = PeriodicWorkRequestBuilder<ImageDownloadWorker>(3, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueue(downloadRequest)
    }
}