package com.example.aurorawidget

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL


class ImageDownloadWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        return try {
            val imageUrl = URL("https://cdn.polarlicht-vorhersage.de/kp_mid.png")
            val connection = imageUrl.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            // Disable caching (headers)
            connection.useCaches = false
            connection.defaultUseCaches = false
            connection.addRequestProperty("Cache-Control", "no-cache")
            connection.addRequestProperty("Pragma", "no-cache")

            // save in internal Storage
            val imgFile = File(applicationContext.filesDir, "aurora_forecast.png")
            val inputStream = connection.inputStream
            val outputStream = FileOutputStream(imgFile)

            // download and save image
            inputStream.use { input ->
                outputStream.use { output ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (input.read(buffer).also { length = it } > 0) {
                        output.write(buffer, 0, length)
                    }
                }
            }
            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()

        }        } }