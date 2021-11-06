package com.tatyanashkolnik.servicestest

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class MyIntentService2 : IntentService(NAME) { // для комбинирования с JobScheduler
    override fun onCreate() { // не делаем foreground потому что будем использовать его до версии 26
        super.onCreate()  // а после 26 версии будем запускать уже JobScheduler
        log("onCreate()")
        setIntentRedelivery(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy()")
    }

    override fun onHandleIntent(intent: Intent?) {

        val page = intent?.getIntExtra(PAGE, 0) ?: 0

        log("onHandleIntent()")
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
        // после выполнения этого метода сервис будет остановлен автоматически
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyIntentService2: $message")
    }

    companion object {

        const val NAME = "intent_service"
        const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentService2::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}