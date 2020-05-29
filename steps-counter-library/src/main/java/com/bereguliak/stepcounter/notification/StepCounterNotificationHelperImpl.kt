package com.bereguliak.stepcounter.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bereguliak.stepcounter.R
import com.bereguliak.stepcounter.notification.StepCounterNotificationHelper.Companion.NOTIFICATION_CHANNEL_ID

internal class StepCounterNotificationHelperImpl(private val context: Context) :
    StepCounterNotificationHelper {

    //region StepCounterNotificationHelper
    override fun createNotification(): Notification {
        return NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_shoe)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
            .setContentTitle(context.getString(R.string.app_notification_step_content_title))
            .build()
    }

    override fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelName = context.getString(R.string.notification_channel_name)
            val channelDescription = context.getString(R.string.notification_channel_desctiption)
            val channelImportance = NotificationManager.IMPORTANCE_MIN

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, channelImportance).apply {
                    description = channelDescription
                    setShowBadge(false)
                    lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                }

            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }
    //endregion
}