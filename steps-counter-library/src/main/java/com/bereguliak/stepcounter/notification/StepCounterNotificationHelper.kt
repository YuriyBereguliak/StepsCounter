package com.bereguliak.stepcounter.notification

import android.app.Notification

internal interface StepCounterNotificationHelper {

    fun createNotificationChannel()
    fun createNotification(): Notification

    //region Utility structure
    companion object {
        internal const val NOTIFICATION_IDENTIFIER = 10001
        internal const val NOTIFICATION_CHANNEL_ID = "steps_counter_channel"
    }
    //endregion
}