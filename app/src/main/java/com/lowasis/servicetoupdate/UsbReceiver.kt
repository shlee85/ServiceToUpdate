package com.lowasis.servicetoupdate

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import android.util.Log

class UsbReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "onReceive!!")

        when (intent?.action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                Log.i(TAG, "USB 연결됨: 서비스 시작")
                val serviceIntent = Intent(context, UpdateService::class.java)
                context?.startForegroundService(serviceIntent)
            }

            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                Log.i(TAG, "USB 해제됨")
                // 필요시 서비스 정지 로직 추가 가능
            }
        }
    }


    companion object {
        private val TAG = UsbReceiver::class.java.simpleName
    }
}