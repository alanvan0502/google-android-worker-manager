package com.example.background.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File

class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
    override fun doWork(): Result {
        makeStatusNotification("Cleaning up old temporary files", applicationContext)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            outputDirectory.listFiles()?.let { entries ->
                entries.forEach {
                    if (it.name.endsWith(".png")) {
                        it.delete()
                    }
                }
            }
            Result.success()
        } catch (e: Throwable) {
            Result.failure()
        }
    }
}