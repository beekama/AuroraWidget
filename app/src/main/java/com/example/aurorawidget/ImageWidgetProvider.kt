package com.example.aurorawidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import java.io.File


class ImageWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        appWidgetIds.forEach { appWidgetId ->
            val imageFile = File(context.filesDir, "aurora_forecast.png")
            if (imageFile.exists()){
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

                val views = RemoteViews(context.packageName, R.layout.image_widget)
                views.setImageViewBitmap(R.id.widget_imageview, bitmap)

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }

    }
}