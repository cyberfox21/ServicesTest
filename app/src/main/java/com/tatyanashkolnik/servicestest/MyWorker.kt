package com.tatyanashkolnik.servicestest

import android.content.Context
import android.util.Log
import androidx.work.*

class MyWorker(context: Context, private val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result { // выполняется в другом потоке
        val page = workerParameters.inputData.getInt(PAGE, 0)

        log("doWork()")
        for (i in 0 until 5) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }

        return Result.success()
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyWorker: $message")
    }

    companion object {

        private const val PAGE = "page"
        const val NAME = "work name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>()
                // to создает объект Pair с ключём PAGE и значением page
                .setInputData(workDataOf(PAGE to page))
                // workDataOf создает объект Data, который хранит объекты ключ-значение
                .setConstraints(makeConstraints()) // установим необходимые ограничения
                .build()
        }

        // создадим необходимые ограничения
        private fun makeConstraints() = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
    }
}