package com.example.moodtune.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeSensorManager(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager? =
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private val accelerometer: Sensor? =
        sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var onShakeListener: (() -> Unit)? = null
    private var lastShakeTime: Long = 0

    // Sensitivity threshold and time debounce
    private val shakeThresholdGravity = 2.3f
    private val shakeSlopTimeMs = 800

    val isSensorAvailable: Boolean
        get() = accelerometer != null

    fun startListening(onShake: () -> Unit) {
        this.onShakeListener = onShake
        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
        onShakeListener = null
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gX = x / SensorManager.GRAVITY_EARTH
        val gY = y / SensorManager.GRAVITY_EARTH
        val gZ = z / SensorManager.GRAVITY_EARTH

        val gForce = sqrt((gX * gX + gY * gY + gZ * gZ).toDouble()).toFloat()

        if (gForce > shakeThresholdGravity) {
            val now = System.currentTimeMillis()
            if (now - lastShakeTime > shakeSlopTimeMs) {
                lastShakeTime = now
                onShakeListener?.invoke()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
