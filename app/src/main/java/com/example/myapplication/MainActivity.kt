package com.example.myapplication

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.registerReceiver


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sp = getSharedPreferences("Battery_SharedPref",Context.MODE_PRIVATE)
        Log.d("dlkvmkfnvf",sp.getString("BATTERY_LEVEL","").toString())

        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.d("vpfkkvmfvf",batteryManager.computeChargeTimeRemaining().toString())
        }



        /*   val batteryService = Intent(this, BatteryService::class.java)
               startService(batteryService)
       */

        val intent = Intent(this,AlarmBatteryBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this,101,intent,PendingIntent.FLAG_MUTABLE)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+1000, pendingIntent)



    }

}

class AlarmBatteryBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {



        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context?.registerReceiver(null, intentFilter)
        if (batteryStatus != null) {
            val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryPct = level / scale.toFloat() * 100

            val sp = context?.getSharedPreferences("Battery_SharedPref",Context.MODE_PRIVATE)
            val batteryLevel = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
            sp?.edit()?.putString("BATTERY_LEVEL", batteryPct.toString())?.apply()

            showNotification(context,batteryPct.toString())

            val intent = Intent(context,AlarmBatteryBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context,101,intent,PendingIntent.FLAG_MUTABLE)

            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            if(batteryPct > 98f){
            alarmManager.cancel(pendingIntent)
        }else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pendingIntent)
            }
        }
    }

     fun showNotification(context: Context?, battery : String?){

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationAlarm = NotificationChannel("BATTERY_LEVEL_ALARM","Notification_channel",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationAlarm)
        }

        val notification = NotificationCompat.Builder(context,"BATTERY_LEVEL_ALARM")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Alarm_Battery")
            .setContentText("BATTERY:- >>  $battery")
            .setColor(Color.RED).build()

        notificationManager.notify(1,notification)

    }
}
