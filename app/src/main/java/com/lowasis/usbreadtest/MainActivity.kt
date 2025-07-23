package com.lowasis.usbreadtest

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.d("PermissionCheck", "SDK VERSION: ${Build.VERSION.SDK_INT}")



       // checkAndRequestStoragePermission()
    }

    override fun onStart() {
        super.onStart()

//        val volumes = (getSystemService(STORAGE_SERVICE) as StorageManager).storageVolumes
//        Log.i("TAG", "volumes.size = ${volumes.size}, ${volumes.get(1)}")
//        for(volume in volumes) {
//            if (volume.isEmulated || volume.uuid == null) continue
//            Log.i("TAG11", File("/storage/" + volume.uuid + File.separator + "factory.txt").readText())
//            Log.i("TAG12", File("/storage/" + volume.uuid + File.separator + "README.txt").readText())
//        }
    }

    private fun checkAndRequestStoragePermission() {
        // Android 11 이상
        if (!Environment.isExternalStorageManager()) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                intent.data = "package:$packageName".toUri()
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "파일 권한 설정 화면을 열 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}