package com.kau.smartbutler.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.kau.smartbutler.view.main.home.child.cctv.CCTVDetailActivity


class EventDetectionNotification{
    lateinit var powerManage: PowerManager
    lateinit var wakeLock: PowerManager.WakeLock

    fun createNotification(applicationContext: Context, sendData: String){
        // ADD Notification
        var noti_manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var noti_intent = Intent(applicationContext, CCTVDetailActivity::class.java)
        noti_intent.putExtra("Detected_Event", sendData)
        noti_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        var pendingIntent = PendingIntent.getActivity(applicationContext, 0, noti_intent, PendingIntent.FLAG_CANCEL_CURRENT)
        var builder = NotificationCompat.Builder(applicationContext,"default")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            noti_manager.createNotificationChannel(
                    NotificationChannel(
                            "default",
                            "기본 채널",
                            NotificationManager.IMPORTANCE_HIGH
                    )
            )
        }

        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(com.kau.smartbutler.R.mipmap.ic_launcher)
                .setContentTitle("이상상황 감지")
                .setContentText(sendData)
                .setContentInfo("INFO")
                .setContentIntent(pendingIntent)

        powerManage = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManage.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "My:Tag")
        wakeLock.acquire()

        noti_manager.notify(1, builder.build())

        if (wakeLock.isHeld){
            wakeLock.release()
        }

    }
}