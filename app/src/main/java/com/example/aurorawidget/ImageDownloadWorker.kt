package com.example.aurorawidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
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

            // AKTUALISIERE WIDGET
            val intent = Intent(applicationContext, ImageWidgetProvider::class.java)
            intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE

            // Hole alle Widget-IDs
            val ids = AppWidgetManager.getInstance(applicationContext)
                .getAppWidgetIds(ComponentName(applicationContext, ImageWidgetProvider::class.java))
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)

            // Sende die Broadcast-Nachricht, um das Widget zu aktualisieren
            applicationContext.sendBroadcast(intent)

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()

        }        } }