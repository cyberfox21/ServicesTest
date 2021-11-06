package com.tatyanashkolnik.servicestest

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tatyanashkolnik.servicestest.MyJobService.Companion.JOB_ID
import com.tatyanashkolnik.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.service.setOnClickListener {
            stopService(MyForegroundService.newIntent(this))
            startService(MyService.newIntent(this, 25))
        }
        binding.foregroundService.setOnClickListener {
//            showNotification()
            ContextCompat.startForegroundService( // В классе ContextCompat уже сделана проверка
                this,                // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                MyForegroundService.newIntent(this)
            )

            // startForegroundService вызывает у MyForegroundService метод startForeground()
            // который как бы обещает, что в течение 5 секунд покажжет уведомление пользователю
            // которое невозможно смахнуть
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(
                this,
                MyIntentService.newIntent(this)
            )
        }
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)

            val jobInfo = JobInfo.Builder(JOB_ID, componentName)
//                .setExtras(MyJobService.newBundle(page++)) для вызова метода .schedule()
//                .setRequiresCharging(true) // чтобы работал только на заряжающемся устройстве
//                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) // только по WIFI
                //ТОЛЬКО КОГДА БЕЗ ОЧЕРЕДИ
//                .setPersisted(true) // выполнится даже после таго как телефон выключили и включили
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

//            jobScheduler.schedule(jobInfo) // не складывает сервисы в очередь,
            // просто отменяет предыдущий и запукает новый

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(MyIntentService2.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }
        binding.workManager.setOnClickListener {
            val workManager =
                WorkManager.getInstance(applicationContext) // контекст приложения а то memory leaks
            workManager.enqueueUniqueWork(
                MyWorker.NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }
    }
}