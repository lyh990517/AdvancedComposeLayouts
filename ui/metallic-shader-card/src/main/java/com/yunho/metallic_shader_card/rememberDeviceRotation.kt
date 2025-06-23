package com.yunho.metallic_shader_card

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberDeviceRotation(): Pair<Float, Float> {
    val context = LocalContext.current
    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    var pitch by remember { mutableFloatStateOf(0f) }
    var roll by remember { mutableFloatStateOf(0f) }
    var initialPitch by remember { mutableFloatStateOf(Float.NaN) }
    var initialRoll by remember { mutableFloatStateOf(Float.NaN) }

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    val orientationAngles = FloatArray(3)

                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    SensorManager.getOrientation(rotationMatrix, orientationAngles)

                    val rawPitch = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
                    val rawRoll = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()

                    if (initialPitch.isNaN()) {
                        initialPitch = rawPitch
                        initialRoll = rawRoll
                    }

                    pitch = (rawPitch - initialPitch)
                    roll = (rawRoll - initialRoll)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager.registerListener(listener, rotationVector, SensorManager.SENSOR_DELAY_GAME)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return -pitch to -roll
}
