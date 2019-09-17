package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import java.text.SimpleDateFormat
import java.util.*

class SaveImageToFileWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val TAG by lazy {
        SaveImageToFileWorker::class.java.simpleName
    }
    private val Title = "Blurred Image"
    private val dateFormatter = SimpleDateFormat(
            "yyy.MM.dd 'at' HH:mm:ss z", Locale.getDefault()
    )

    override fun doWork(): Result {
        makeStatusNotification("Saving Image", applicationContext)
        sleep()

        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            val bitmap = BitmapFactory.decodeStream(applicationContext.contentResolver
                    .openInputStream(Uri.parse(resourceUri)))
            val imageUrl = MediaStore.Images.Media.insertImage(
                    applicationContext.contentResolver, bitmap, Title, dateFormatter.format(Date())
            )
            if (!imageUrl.isNullOrEmpty()) {
                val output = workDataOf(KEY_IMAGE_URI to imageUrl)
                Result.success(output)
            } else {
                Result.failure()
            }
        } catch (e: Throwable) {
            Result.failure()
        }

    }

}