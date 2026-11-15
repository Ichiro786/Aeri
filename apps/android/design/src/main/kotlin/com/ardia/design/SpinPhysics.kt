package com.ardia.design

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round
import kotlin.math.sign
import kotlin.math.sin

class SpinPhysics(
    private val uprightPull: Float = 600f,
    private val coastDrag: Float = 0.55f,
    private val settleDrag: Float = 4f,
    private val settleSpeed: Float = 140f,
    private val maxSpeed: Float = 3000f,
) {
    var angle = 0f
        private set
    var velocity = 0f
        private set
    var isHeld = false
        private set
    val isMoving: Boolean get() = !isHeld && (velocity != 0f || offUpright() != 0f)
    val turns: Int get() = floor(angle / 360f).toInt()

    private val samples = ArrayDeque<Pair<Long, Float>>()

    fun grab(atMillis: Long) {
        isHeld = true
        velocity = 0f
        samples.clear()
        samples.addLast(atMillis to angle)
    }

    fun dragBy(degrees: Float, atMillis: Long) {
        if (!isHeld) return
        angle += degrees
        samples.addLast(atMillis to angle)
        while (samples.size > 2 && atMillis - samples.first().first > SAMPLE_WINDOW_MILLIS) samples.removeFirst()
    }

    fun release(atMillis: Long) {
        if (!isHeld) return
        isHeld = false
        val newest = samples.lastOrNull()
        val oldest = samples.firstOrNull { atMillis - it.first <= SAMPLE_WINDOW_MILLIS }
        velocity = if (newest == null || oldest == null || atMillis - newest.first > STALE_MILLIS || newest.first <= oldest.first) {
            0f
        } else {
            ((newest.second - oldest.second) / ((newest.first - oldest.first) / 1000f)).coerceIn(-maxSpeed, maxSpeed)
        }
        samples.clear()
    }

    fun kick(degreesPerSecond: Float) {
        isHeld = false
        velocity = (velocity + degreesPerSecond).coerceIn(-maxSpeed, maxSpeed)
    }

    fun reset() {
        isHeld = false
        angle = 0f
        velocity = 0f
        samples.clear()
    }

    fun step(seconds: Float): Boolean {
        if (isHeld) return false
        val total = seconds.coerceIn(0f, MAX_FRAME_SECONDS)
        val slices = ceil(total / SLICE_SECONDS).toInt().coerceAtLeast(1)
        val slice = total / slices
        repeat(slices) {
            val drag = coastDrag + settleDrag / (1f + (velocity / settleSpeed) * (velocity / settleSpeed))
            val pull = -uprightPull * sin(angle * PI / 180.0).toFloat()
            velocity += (pull - drag * velocity) * slice
            velocity = velocity.coerceIn(-maxSpeed, maxSpeed)
            angle += velocity * slice
            val off = offUpright()
            if (abs(velocity) < REST_SPEED) {
                if (abs(off) < REST_ANGLE) {
                    angle = round(angle / 360f) * 360f
                    velocity = 0f
                    return false
                }
                if (abs(off) > 180f - REST_ANGLE) velocity += TIP_NUDGE * if (off == 0f) 1f else sign(off)
            }
        }
        return true
    }

    private fun offUpright(): Float = angle - round(angle / 360f) * 360f

    private companion object {
        const val SAMPLE_WINDOW_MILLIS = 100L
        const val STALE_MILLIS = 60L
        const val MAX_FRAME_SECONDS = 0.05f
        const val SLICE_SECONDS = 1f / 240f
        const val REST_SPEED = 2f
        const val REST_ANGLE = 0.3f
        const val TIP_NUDGE = 8f
    }
}
