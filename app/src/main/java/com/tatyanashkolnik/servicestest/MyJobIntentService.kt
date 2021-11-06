package com.tatyanashkolnik.servicestest

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {
    override fun onCreate() {
        super.onCreate()
        log("onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy()")
    }

    override fun onHandleWork(intent: Intent) {
        val page = intent.getIntExtra(PAGE, 0)

        log("onHandleWork()")
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
        // после выполнения этого метода сервис будет остановлен автоматически
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobIntentService: $message")
    }

    companion object {

        private const val PAGE = "page"
        private const val JOB_ID = 111

        fun enqueue(context: Context, page: Int){
            JobIntentService.enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}