package com.lowasis.usbreadtest

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager

class MyApp: Application() {
    lateinit var mountReceiver: UsbReceiver

    override fun onCreate() {
        super.onCreate()
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