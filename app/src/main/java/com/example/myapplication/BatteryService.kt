package com.example.myapplication

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.IBinder
import android.widget.Toast
import java.util.Calendar

class BatteryService : Service(){

    var batteryReceiver : BatteryBroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        batteryReceiver = BatteryBroadcastReceiver()

        Toast.makeText(this,Calendar.getInstance().get(Calendar.MINUTE).toString(),Toast.LENGTH_LONG).show()

//        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val sp = getSharedPreferences("Battery_SharedPref", Context.MODE_PRIVATE)
        val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
        sp?.edit()?.putString("BATTERY_LEVEL", Calendar.getInstance().get(Calendar.MINUTE).toString())?.apply()


        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
      //  unregisterReceiver(batteryReceiver)
    }
}
