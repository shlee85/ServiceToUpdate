package com.lowasis.servicetoupdate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class BootBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.i(TAG, "onReceive!!")

        if(p1?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i(TAG, "ACTION BOOT COMPLETED")
            val serviceIntent = Intent(p0, UpdateService::class.java)
            p0?.startForegroundService(serviceIntent)
        }
    }

    companion object {
        private val TAG = BootBroadcastReceiver::class.java.simpleName
    }
}