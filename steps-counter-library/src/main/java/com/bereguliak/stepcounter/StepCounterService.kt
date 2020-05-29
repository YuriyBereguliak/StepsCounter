package com.bereguliak.stepcounter

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.bereguliak.stepcounter.notification.StepCounterNotificationHelper
import com.bereguliak.stepcounter.notification.StepCounterNotificationHelper.Companion.NOTIFICATION_IDENTIFIER
import com.bereguliak.stepcounter.notification.StepCounterNotificationHelperImpl

class StepCounterService : Service() {

    private val notificationHelper: StepCounterNotificationHelper by lazy {
        StepCounterNotificationHelperImpl(this)
    }

    private var startSteps = 0

    private val sensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {

                val totalSteps = event.values[0].toInt()

                if (startSteps < 1) {
                    startSteps = totalSteps
                }

                val steps = totalSteps - startSteps

                //TODO: notify users about steps
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    //region Service
    override fun onCreate() {
        super.onCreate()
        notificationHelper.createNotificationChannel()
        startForeground(NOTIFICATION_IDENTIFIER, notificationHelper.createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            COMMAND_START_STEP_COUNTER -> handleStartStepCounter()
            COMMAND_STOP_STEP_COUNTER -> handleStopStepCounter()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    //endregion

    //region Utility API
    private fun handleStartStepCounter() {
        val sensorManager = getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager.registerListener(
            sensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL,
            BATCH_LATENCY_15s
        )
    }

    private fun handleStopStepCounter() {
        val sensorManager = getSystemService(Activity.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(sensorEventListener)
        stopForeground(true)
        stopSelf()
    }
    //endregion

    //region Utility structure
    companion object {

        const val ACTION_STEPS_UPDATE = "com.bereguliak.stepcounter.service.ACTION_STEPS_UPDATE"
        const val EXTRA_STEPS = "EXTRA_STEPS"
        const val EXTRA_TOTAL_STEPS = "EXTRA_TOTAL_STEPS"

        private const val BATCH_LATENCY_15s = 15000000

        private const val COMMAND_START_STEP_COUNTER = "COMMAND_START_STEP_COUNTER"
        private const val COMMAND_STOP_STEP_COUNTER = "COMMAND_STOP_STEP_COUNTER"

        fun startStepCounterService(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, StepCounterService::class.java).apply {
                    action = COMMAND_START_STEP_COUNTER
                })
        }

        fun stopStepCounterService(context: Context) {
            ContextCompat.startForegroundService(
                context,
                Intent(context, StepCounterService::class.java).apply {
                    action = COMMAND_STOP_STEP_COUNTER
                })
        }
    }
    //endregion
}