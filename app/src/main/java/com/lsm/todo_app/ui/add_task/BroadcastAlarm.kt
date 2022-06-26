package com.lsm.todo_app.ui.add_task

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lsm.todo_app.R

class BroadcastAlarm : BroadcastReceiver() {

    private fun simpleNotification(context: Context, title: String?) {
        val builder = NotificationCompat.Builder(context.applicationContext, "0")
            .setSmallIcon(R.drawable.ic_baseline_priority_high_24_black)
            .setContentTitle("You have task to do:")
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(0, builder.build())
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Alarm Bell", "Alarm just fired")
        simpleNotification(context, intent.getStringExtra("title"))
    }
}