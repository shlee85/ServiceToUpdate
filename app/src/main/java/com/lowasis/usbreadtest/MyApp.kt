package com.lowasis.usbreadtest

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager

class MyApp: Application() {
    // USB 마운트 등 브로드캐스트를 수신할 Receiver 인스턴스
    lateinit var mountReceiver: UsbReceiver

    override fun onCreate() {
        super.onCreate()

        //앱 전체에서 사용할 수 있는 전역 application 인스턴스 저장.
        instance = this


        mountReceiver = UsbReceiver()

        val filter = IntentFilter(Intent.ACTION_MEDIA_MOUNTED)
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
        filter.addDataScheme("file")
        registerReceiver(mountReceiver, filter)
    }

    companion object {
        lateinit var instance: MyApp
            private set
    }
}