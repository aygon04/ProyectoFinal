package com.example.proyectofinal

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class AlarmReceiver:BroadcastReceiver()
{
lateinit var mNotificationManager:NotificationManager
    override fun onReceive(context: Context, intent: Intent) {
       mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        deliverNotification(context)
    }
    fun deliverNotification(context: Context)
    {
        val contentIntent = Intent(context , Main2Activity::class.java)
        val contentPendingIntent=PendingIntent.getActivity(
            context,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "primary_notification_channel")
            .setSmallIcon(R.drawable.ic_menu_camera)
            .setContentTitle("Entra a jugar!")
            .setContentText("Deberias de entrar a jugar")
            .setContentIntent(contentPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            Log.e("AlarmReceiver","AlarmReceiver")
        mNotificationManager.notify(0,builder.build())
    }
}