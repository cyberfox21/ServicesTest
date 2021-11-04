package com.tatyanashkolnik.servicestest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tatyanashkolnik.servicestest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

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
    }

    private fun showNotification() {
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // если SDK 26 или больше
            val notificationChannel = NotificationChannel(    // создаем channel
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // В классе NotificationCompat уже сделана проверка
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "channel_name"
    }
}