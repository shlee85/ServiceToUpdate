package com.lowasis.servicetoupdate

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class UpdateService: Service() {
    override fun onBind(p0: Intent?): IBinder? {
        Log.i(TAG, "onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate()")

        startForegroundServiceWithNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        return START_STICKY
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundServiceWithNotification() {
        val channelId = "update_service_channel"
        val channelName = "Update Service"

        val chan = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(chan)

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("서비스 실행 중")
            .setContentText("UpdateService가 백그라운드에서 실행되고 있습니다.")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .build()

        try {
            startForeground(1, notification)
        } catch (e: SecurityException) {
            Log.i(TAG, "Foreground권한 없음!! ${e.message}")
        }
    }

    companion object {
        private val TAG = UpdateService::class.java.simpleName
    }
}