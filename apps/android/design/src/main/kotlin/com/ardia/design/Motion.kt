package com.ardia.design

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

object SwiftEasing {
    val easeOut: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
    val easeIn: Easing = CubicBezierEasing(0.42f, 0f, 1f, 1f)
    val easeInOut: Easing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
    val linear: Easing = LinearEasing
}

@Immutable
sealed interface AeriMotion {
    fun <T> spec(visibilityThreshold: T? = null): FiniteAnimationSpec<T>
}

@Immutable
data class AeriSpring(val response: Float, val dampingFraction: Float) : AeriMotion {
    val stiffness: Float get() = (2.0 * PI / response).pow(2).toFloat()

    val dampingRatio: Float get() = dampingFraction

    val damping: Float get() = (2.0 * dampingFraction * sqrt(stiffness.toDouble())).toFloat()

    override fun <T> spec(visibilityThreshold: T?): FiniteAnimationSpec<T> =
        spring(dampingRatio = dampingRatio, stiffness = stiffness, visibilityThreshold = visibilityThreshold)

    companion object {
        fun withBounce(duration: Float, bounce: Float): AeriSpring =
            AeriSpring(response = duration, dampingFraction = if (bounce >= 0f) 1f - bounce else 1f / (1f + bounce))
    }
}

@Immutable
data class AeriTween(val durationMillis: Int, val easing: Easing, val delayMillis: Int = 0) : AeriMotion {
    override fun <T> spec(visibilityThreshold: T?): FiniteAnimationSpec<T> =
        tween(durationMillis = durationMillis, delayMillis = delayMillis, easing = easing)
}

object Motion {
    val snappy = AeriSpring(response = 0.3f, dampingFraction = 0.86f)
    val smooth = AeriSpring(response = 0.42f, dampingFraction = 0.9f)
    val tabStack = AeriSpring.withBounce(duration = 0.4f, bounce = 0.2f)
    val modeToggle = AeriSpring.withBounce(duration = 0.3f, bounce = 0.15f)

    val searchRise = AeriSpring(response = 0.39f, dampingFraction = 1f)
    val surfaceSwap = AeriTween(300, SwiftEasing.easeOut)
    val surfaceFade = AeriTween(200, SwiftEasing.easeOut)
    val trayDim = AeriTween(300, SwiftEasing.easeInOut)
    val trayDismiss = AeriSpring(response = 0.32f, dampingFraction = 1f)
    val traySettle = AeriSpring(response = 0.34f, dampingFraction = 0.86f)
    val resultsIn = AeriTween(140, SwiftEasing.easeOut)
    val pager = AeriSpring(response = 0.3f, dampingFraction = 0.9f)
    val press = AeriSpring(response = 0.2f, dampingFraction = 0.7f)
    val barPressIn = AeriSpring.withBounce(duration = 0.08f, bounce = 0f)
    val barPressOut = AeriSpring.withBounce(duration = 0.16f, bounce = 0.1f)
    val barCollapse = AeriSpring.withBounce(duration = 0.4f, bounce = 0f)
    val progressReveal = AeriTween(200, SwiftEasing.easeOut)
    val progressAdvance = AeriTween(450, SwiftEasing.easeOut)
    val progressFill = AeriTween(200, SwiftEasing.easeOut)
    val progressFade = AeriTween(250, SwiftEasing.easeOut)
    const val progressRevealDelayMillis = 150L
    const val placeholderDelayMillis = 300L
    val placeholderIn = AeriTween(220, SwiftEasing.easeOut)
    val pagerSettle = AeriSpring.withBounce(duration = 0.38f, bounce = 0f)
    val pagerFade = AeriTween(200, SwiftEasing.easeOut)
    val barTone = AeriTween(250, SwiftEasing.easeOut)
    val reducedFade = AeriTween(200, SwiftEasing.easeInOut)
    val reducedSettle = AeriTween(200, SwiftEasing.easeOut)
    val logoReset = AeriTween(300, SwiftEasing.easeOut)
    val glassPressIn = AeriSpring(response = 0.16f, dampingFraction = 0.82f)
    val glassRelease = AeriSpring(response = 0.36f, dampingFraction = 0.42f)
    val glassPop = AeriSpring(response = 0.38f, dampingFraction = 0.62f)
    val glassFold = AeriSpring(response = 0.22f, dampingFraction = 1f)
    val glassRise = AeriSpring(response = 0.44f, dampingFraction = 0.78f)
    val glassToggle = AeriSpring(response = 0.34f, dampingFraction = 0.55f)
    val glassPush = AeriSpring(response = 0.42f, dampingFraction = 0.86f)
}
