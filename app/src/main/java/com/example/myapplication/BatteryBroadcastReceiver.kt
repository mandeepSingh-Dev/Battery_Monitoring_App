package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.widget.Toast

class BatteryBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        val sp = context?.getSharedPreferences("Battery_SharedPref",Context.MODE_PRIVATE)
        val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
        sp?.edit()?.putString("BATTERY_LEVEL", batteryLevel.toString())?.apply()
    }
}

