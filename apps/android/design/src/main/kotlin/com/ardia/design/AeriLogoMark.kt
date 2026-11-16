package com.ardia.design

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot

object LogoGeometry {
    const val UNIT = 1000f
    const val TILE_SHARE = 0.9064f
    val lightTile = Color(0xFFF5F6FA)
    val darkTile = Color(0xFF000000)

    private const val OUTLINE = "M801.6,800.5C792.8,800.9 783.8,799.9 775.0,798.4C766.3,797.0 757.5,794.8 749.1,792.0C740.7,789.1 732.5,785.4 724.6,781.4C716.7,777.4 709.1,772.6 701.7,767.7C694.2,762.8 686.9,757.7 680.0,752.2C673.0,746.6 666.3,740.7 659.8,734.6C653.3,728.6 647.0,722.2 640.9,715.8C634.7,709.3 628.7,702.7 622.9,696.0C617.0,689.4 611.4,682.4 605.6,675.6C599.9,668.8 594.1,662.1 588.4,655.2C582.7,648.4 577.1,641.5 571.4,634.6C565.7,627.8 560.5,620.3 554.1,614.2C547.7,608.1 541.0,601.7 533.2,598.1C525.4,594.4 516.0,592.7 507.2,592.3C498.5,591.8 489.4,593.3 480.8,595.3C472.2,597.3 463.7,600.5 455.6,604.1C447.6,607.8 439.8,612.4 432.4,617.3C424.9,622.1 417.9,627.7 411.0,633.3C404.1,638.9 397.6,645.0 391.0,651.0C384.5,657.0 378.0,663.2 371.6,669.4C365.3,675.6 359.0,681.9 352.7,688.2C346.3,694.5 340.2,700.9 333.8,707.1C327.4,713.4 321.0,719.6 314.5,725.6C308.0,731.7 301.4,737.7 294.6,743.4C287.8,749.2 280.9,754.9 273.7,760.1C266.5,765.3 259.0,770.3 251.4,774.8C243.7,779.2 235.8,783.6 227.7,787.1C219.5,790.5 211.0,793.6 202.4,795.7C193.8,797.9 184.9,799.7 176.1,800.1C167.3,800.5 158.2,799.9 149.5,798.2C140.9,796.4 132.2,793.6 124.4,789.5C116.5,785.5 109.0,780.1 102.7,774.1C96.3,768.1 90.4,760.9 86.0,753.3C81.5,745.8 78.3,737.1 76.1,728.6C73.8,720.1 72.8,711.0 72.5,702.2C72.3,693.4 73.1,684.3 74.6,675.6C76.1,666.9 78.7,658.3 81.6,649.9C84.5,641.5 88.1,633.3 91.9,625.2C95.6,617.1 99.9,609.3 104.2,601.5C108.6,593.8 113.3,586.2 117.9,578.6C122.5,571.0 127.2,563.4 131.9,555.8C136.6,548.2 141.1,540.5 145.9,533.0C150.6,525.5 155.6,518.1 160.6,510.7C165.5,503.3 170.6,496.0 175.5,488.6C180.5,481.2 185.2,473.7 190.2,466.2C195.1,458.8 200.1,451.4 205.1,444.1C210.2,436.7 215.3,429.5 220.3,422.1C225.4,414.8 230.3,407.3 235.3,400.0C240.4,392.7 245.4,385.3 250.5,378.0C255.6,370.7 260.8,363.5 265.9,356.2C271.1,348.9 276.2,341.7 281.4,334.4C286.6,327.2 291.8,319.9 297.0,312.8C302.3,305.6 307.6,298.4 312.9,291.3C318.2,284.1 323.6,277.0 329.0,269.9C334.3,262.8 339.6,255.6 345.1,248.6C350.6,241.6 356.2,234.7 361.9,227.8C367.5,220.9 373.1,214.0 379.1,207.4C385.0,200.7 391.1,194.2 397.5,188.0C403.9,181.8 410.5,175.8 417.4,170.2C424.4,164.7 431.6,159.4 439.2,154.8C446.8,150.1 454.8,146.0 463.0,142.6C471.2,139.2 479.7,136.4 488.4,134.4C497.0,132.5 505.9,131.0 514.8,130.8C523.6,130.5 532.6,131.4 541.3,133.0C550.0,134.7 558.7,137.3 566.9,140.5C575.2,143.7 583.3,147.7 591.0,152.1C598.6,156.6 605.9,161.9 612.9,167.4C619.8,172.9 626.3,179.1 632.6,185.4C638.9,191.6 645.1,198.1 650.9,204.9C656.6,211.6 662.0,218.8 667.4,225.9C672.7,233.0 677.9,240.3 682.9,247.6C687.9,255.0 692.6,262.6 697.4,270.1C702.2,277.6 707.0,285.1 711.6,292.7C716.3,300.3 720.8,308.0 725.3,315.6C729.8,323.3 734.4,331.0 738.9,338.7C743.4,346.3 747.9,354.0 752.4,361.7C756.8,369.4 761.4,377.1 765.7,384.9C770.1,392.6 774.4,400.4 778.7,408.2C783.0,416.0 787.4,423.8 791.8,431.5C796.1,439.3 800.5,447.1 804.7,454.9C809.0,462.7 813.2,470.6 817.4,478.4C821.6,486.3 825.9,494.1 830.1,501.9C834.3,509.8 838.4,517.7 842.6,525.5C846.7,533.4 851.0,541.3 855.0,549.2C859.1,557.1 863.1,565.1 867.0,573.1C871.0,581.0 875.1,589.0 878.9,597.0C882.7,605.1 886.6,613.1 890.0,621.3C893.3,629.6 896.7,637.9 899.2,646.4C901.7,654.9 904.0,663.6 905.1,672.4C906.3,681.2 906.8,690.3 906.2,699.1C905.6,707.9 904.2,716.9 901.6,725.3C899.0,733.8 895.4,742.2 890.8,749.7C886.2,757.2 880.4,764.3 874.1,770.4C867.7,776.5 860.4,782.1 852.7,786.3C845.0,790.6 836.4,793.7 827.9,796.1C819.4,798.4 810.4,800.1 801.6,800.5Z"

    internal val silhouette = GenericShape { size, _ ->
        val outline = PathParser().parsePathString(OUTLINE).toPath()
        outline.transform(Matrix().apply { scale(size.width / UNIT, size.height / UNIT) })
        addPath(outline)
    }
}

@Composable
fun AeriLogoMark(modifier: Modifier = Modifier, dark: Boolean = LocalAeriDarkTheme.current) {
    Image(
        bitmap = ImageBitmap.imageResource(if (dark) R.drawable.aeri_mark_on_dark else R.drawable.aeri_mark_on_light),
        contentDescription = null,
        modifier = modifier.aspectRatio(1f).clearAndSetSemantics {},
        contentScale = ContentScale.Fit,
        filterQuality = FilterQuality.High,
    )
}

@Composable
fun FidgetLogo(
    modifier: Modifier = Modifier,
    contentDescription: String = "Aeri",
    dark: Boolean = LocalAeriDarkTheme.current,
    greetingSpin: Float = 0f,
    shadow: Boolean = true,
) {
    val reduceMotion = LocalReduceMotion.current
    val haptics = LocalHapticFeedback.current
    val spin = remember { SpinPhysics() }
    var angle by remember { mutableFloatStateOf(0f) }
    var awake by remember { mutableStateOf(false) }
    var settling by remember { mutableStateOf(false) }

    LaunchedEffect(greetingSpin, reduceMotion) {
        if (greetingSpin != 0f && !reduceMotion) {
            spin.kick(greetingSpin)
            awake = true
        }
    }
    LaunchedEffect(awake) {
        if (!awake) return@LaunchedEffect
        var last = withFrameNanos { it }
        var turns = spin.turns
        var lastTick = 0L
        while (spin.isHeld || spin.isMoving) {
            withFrameNanos { now ->
                spin.step((now - last) / 1_000_000_000f)
                last = now
                if (!settling) angle = spin.angle
                if (spin.turns != turns) {
                    turns = spin.turns
                    if (!spin.isHeld && abs(spin.velocity) > FidgetFeel.TICK_SPEED && now - lastTick > FidgetFeel.TICK_GAP_NANOS) {
                        lastTick = now
                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }
            }
        }
        awake = false
    }
    LaunchedEffect(settling) {
        if (!settling) return@LaunchedEffect
        val back = Animatable(angle)
        back.animateTo(0f, Motion.logoReset.spec()) { angle = value }
        spin.reset()
        settling = false
    }

    Box(
        modifier
            .semantics {
                this.contentDescription = contentDescription
                role = Role.Image
            }
            .pointerInput(reduceMotion) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val deadZone = size.width * FidgetFeel.DEAD_ZONE_SHARE
                    settling = false
                    spin.grab(down.uptimeMillis)
                    awake = true
                    var bearing = bearingOrNull(down.position, center, deadZone)
                    var travelled = 0f
                    var lastMillis = down.uptimeMillis
                    var lastPosition = down.position
                    while (true) {
                        val event = awaitPointerEvent()
                        val change = event.changes.firstOrNull { it.id == down.id } ?: break
                        lastMillis = change.uptimeMillis
                        lastPosition = change.position
                        if (!change.pressed) break
                        travelled += change.positionChange().getDistance()
                        val next = bearingOrNull(change.position, center, deadZone)
                        if (next != null && bearing != null) {
                            var delta = next - bearing
                            if (delta > 180f) delta -= 360f
                            if (delta < -180f) delta += 360f
                            spin.dragBy(delta, change.uptimeMillis)
                            angle = spin.angle
                        }
                        bearing = next
                        if (travelled > viewConfiguration.touchSlop) change.consume()
                    }
                    when {
                        reduceMotion -> {
                            spin.reset()
                            settling = true
                        }
                        travelled <= viewConfiguration.touchSlop -> {
                            spin.release(lastMillis)
                            spin.kick(if (lastPosition.x >= center.x) FidgetFeel.TAP_KICK else -FidgetFeel.TAP_KICK)
                        }
                        else -> spin.release(lastMillis)
                    }
                    awake = true
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { rotationZ = angle },
            contentAlignment = Alignment.Center,
        ) {
            AeriLogoMark(
                if (shadow) {
                    Modifier.dropShadow(LogoGeometry.silhouette, Shadow(radius = 14.dp, color = Color.Black.copy(alpha = 0.22f), offset = DpOffset.Zero))
                } else {
                    Modifier
                },
                dark = dark,
            )
        }
    }
}

private object FidgetFeel {
    const val TAP_KICK = 900f
    const val TICK_SPEED = 90f
    const val TICK_GAP_NANOS = 70_000_000L
    const val DEAD_ZONE_SHARE = 0.06f
}

private fun bearingOrNull(point: Offset, center: Offset, deadZone: Float): Float? {
    if (hypot(point.x - center.x, point.y - center.y) < deadZone) return null
    return (atan2(point.y - center.y, point.x - center.x) * 180.0 / PI).toFloat()
}
