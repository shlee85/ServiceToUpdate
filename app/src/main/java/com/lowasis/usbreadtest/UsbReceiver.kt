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
import java.util.zip.ZipFile
import kotlin.math.PI

class UsbReceiver(): BroadcastReceiver() {
    private var mUsbHandler: Handler?=null
    val appContext = MyApp.instance.applicationContext

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


        when (intent.action) {
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

                Log.i(TAG, "Build version = [${getSysBuildVersion()}]")
                Log.i(TAG, "=============================================================")
                for(volume in volumes) {
                    if (volume.isEmulated || volume.uuid == null) continue
                    Log.i("UsbReceiver1", File("/storage/" + volume.uuid + File.separator + "factory.txt").readText())
                    Log.i("UsbReceiver1", File("/storage/" + volume.uuid + File.separator + "README.txt").readText())

                    val version = getUpdateFileVersion("/storage/"+volume.uuid + File.separator)
                    Log.i(TAG, "version = [$version]")
                }
            }

            UsbManager.ACTION_USB_DEVICE_DETACHED -> {
                Log.i(TAG, "USB 해제됨")
                // 필요시 서비스 정지 로직 추가 가능
            }
        }
    }

    private fun getSysBuildVersion(): String {
        val display = Build.DISPLAY

        val regex = Regex("""\beng\.lowasi\.\d{8}\.\d{6}\b""")  //버전정보만 가져오기 위한 구분
        val match = regex.find(display)
        val buildVersion = match?.value ?: "정보 없음"

        return buildVersion
    }

    private fun getUpdateFileVersion(path: String): String {

        val updateFile = "tetra.zip"
        val targetFile = "META-INF/com/android/metadata"
        val zipFile = ZipFile(File(path+"/"+updateFile))
        val entry = zipFile.getEntry(targetFile)

        if(entry != null) {
            zipFile.getInputStream(entry).bufferedReader().useLines { lines ->
                for(line in lines) {
                    if(line.startsWith("post-build-incremental=")) {   //특정 문자열로 시작하는 검사
                        return line.substringAfter("=", "Unknown") //"=" 이후의 값을 가져온다. 없는 경우 "Unknown"
                    }
                }
            }
        }

        return ""
    }

    companion object {
        private val TAG = UsbReceiver::class.java.simpleName
    }
}