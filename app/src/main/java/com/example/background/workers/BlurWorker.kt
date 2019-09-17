package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI
import timber.log.Timber

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        val appContext = applicationContext
        makeStatusNotification("Blurring image", appContext)
        return try {
            if (TextUtils.isEmpty(resourceUri)) {
                throw IllegalArgumentException("Invalid input Uri")
            }

            val resolver = appContext.contentResolver
            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))

            val saveUri = writeBitmapToFile(appContext, blurBitmap(picture, appContext))
            val builder = Data.Builder()
            builder.putString(KEY_IMAGE_URI, saveUri.toString())

            makeStatusNotification("Output is $saveUri", appContext)
            Result.success(builder.build())
        } catch (e: Throwable) {
            Timber.e(e)
            Result.failure()
        }
    }
}