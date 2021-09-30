package com.tatyanashkolnik.servicestest

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()
        log("onCreate()")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand()")
        for(i in 0 until 100){
            Thread.sleep(1000)
            log("Timer $i")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy()")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun log(message: String){
        Log.d("SERVICE_TAG", "MyService: $message")
    }

    companion object{
        fun newIntent(context: Context): Intent {
            return Intent(context, MyService::class.java)
        }
    }
}