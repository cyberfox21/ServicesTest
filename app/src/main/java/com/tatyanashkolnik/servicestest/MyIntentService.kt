package com.tatyanashkolnik.servicestest

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class MyIntentService : IntentService(NAME) {
    override fun onCreate() {
        super.onCreate()
        log("onCreate()")
        setIntentRedelivery(true)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }
    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy()")
    }
    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent()")
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer $i")
        }
        // после выполнения этого метода сервис будет остановлен автоматически
    }
    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService: $message")
    }
    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentText("Foreground text")
        .setContentTitle("Foreground title")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .build()
    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    companion object {

        const val NAME = "intent_service"
        const val CHANNEL_ID = "foreground_channel_id"
        const val CHANNEL_NAME = "foreground_channel_name"
        const val NOTIFICATION_ID = 3 // низя 0

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}