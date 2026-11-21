package com.example.moodtune.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LightSensorManager(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager? =
        context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager

    private val lightSensor: Sensor? =
        sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)

    private val _luxState = MutableStateFlow<Float>(100f) // Default bright room lux
    val luxState: StateFlow<Float> = _luxState.asStateFlow()

    val isSensorAvailable: Boolean
        get() = lightSensor != null

    fun startListening() {
        lightSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_LIGHT) return
        val lux = event.values[0]
        _luxState.value = lux
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }

    companion object {
        const val DIMMED_LUX_THRESHOLD = 15f
    }
}
