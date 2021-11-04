package com.tatyanashkolnik.servicestest

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.*

class MyJobService : JobService() {

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate()")
    }


    // возвращаемый тип - работа сервиса уже завершена или ещё нет?
    override fun onStartJob(params: JobParameters?): Boolean { // выполняется на главном потоке


        // если запускали сервис через enqueue
        scope.launch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent?.getIntExtra(PAGE, 0)


                    log("onStartJob()")
                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer $i $page")
                    }
                    params?.completeWork(workItem) // завершаем не всю работу, а только текущего workItem
                    workItem = params?.dequeueWork() // достаем новый workItem из очереди

                }
                jobFinished(params, false) // сами завершаем сервис
            }
        }

//
//        // при запуске через .schedule(jobInfo)
//        val page = params?.extras?.getInt(PAGE) ?: 0 // хотим продолжить с момента остановки
//
//        scope.launch {
//            log("onStartJob()")
//            for (i in 0 until 5) {
//                delay(1000)
//                log("Timer $i $page")
//            }
//
//            jobFinished(params, true) // сами завершаем сервис
//        }


        return true // сервис ещё выполняется (тк будет выполняться корутина)
        // и мы завершим его работу сами
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob()")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        log("onDestroy()")
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "MyJobService: $message")
    }

    companion object {
        const val JOB_ID = 1
        private const val PAGE = "page"
        fun newPersistableBundle(page: Int): PersistableBundle { // для передачи параметров в сервис
            return PersistableBundle().apply {
                putInt(PAGE, page)
            }
        }

        fun newIntent(page: Int): Intent { // для передачи параметров в сервис
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}