package com.lowasis.usbreadtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.STORAGE_SERVICE
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.storage.StorageManager
import android.provider.Settings
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.File
import kotlin.math.PI

class UsbReceiver(): BroadcastReceiver() {
    private var mUsbHandler: Handler?=null
    val appContext = MyApp.instance.applicationContext

    init {


/*
        mUsbHandler = Handler(Looper.getMainLooper()) { msg->
            when(msg.what) {
                1 -> {
                    Log.i(TAG, "USB Mount!!!")

                    val volumes =
                        (appContext.getSystemService(STORAGE_SERVICE) as StorageManager).storageVolumes

                    Log.i("UsbReceiver", "volumes.size = ${volumes.size}, ${volumes.get(1)}")
                    Log.i(TAG, "=============================================================")
                    for(volume in volumes) {
                        if (volume.isEmulated || volume.uuid == null) continue
                        Log.i("UsbReceiver", File("/storage/" + volume.uuid + File.separator + "factory.txt").readText())
                        Log.i("UsbReceiver", File("/storage/" + volume.uuid + File.separator + "README.txt").readText())
                    }

                    Log.i(TAG, "=============================================================")
                }
            }

            true
        }

 */
    }

    private var lastActionTime: Long = 0
    private var lastAction: String? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "onReceive!!")

        val now = System.currentTimeMillis()
        val action = intent?.action ?: return

        if (action == lastAction && (now - lastActionTime < 1000)) {
            Log.w("UsbReceiver", "⚠️ 중복 호출 차단됨: $action")
            return
        }

        lastAction = action
        lastActionTime = now


        when (intent?.action) {
            UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                Log.i(TAG, "USB 연결됨: 서비스 시작!!!!!!")

                //mUsbHandler?.sendEmptyMessageDelayed(1, 1000)

                if (Environment.isExternalStorageManager()) {
                    Log.i(TAG, "✅ 외부저장소 접근 가능")
                } else {
                    Log.e(TAG, "❌ 외부저장소 권한 없음")
                }
            }

            Intent.ACTION_MEDIA_MOUNTED -> {
                val volumes =
                    (appContext.getSystemService(STORAGE_SERVICE) as StorageManager).storageVolumes

                Log.i(TAG, "=============================================================")
                for(volume in volumes) {
                    if (volume.isEmulated || volume.uuid == null) continue
                    Log.i("UsbReceiver1", File("/storage/" + volume.uuid + File.separator + "factory.txt").readText())
                    Log.i("UsbReceiver1", File("/storage/" + volume.uuid + File.separator + "README.txt").readText())
                }
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