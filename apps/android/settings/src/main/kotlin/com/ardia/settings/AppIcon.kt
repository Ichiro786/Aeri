package com.ardia.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ardia.design.AeriLogoMark
import com.ardia.design.FidgetLogo
import com.ardia.design.LocalAeriDarkTheme
import com.ardia.design.LogoGeometry
import com.ardia.design.SettingsLayout
import com.ardia.design.SystemPalette

@Composable
fun AppIcon(
    modifier: Modifier = Modifier,
    side: Dp = 29.dp,
    shadow: Boolean = false,
    scalesWithText: Boolean = true,
    spins: Boolean = false,
    greetingSpin: Float = 0f,
) {
    val density = LocalDensity.current
    val dark = LocalAeriDarkTheme.current
    val scaled = if (scalesWithText) side * minOf(density.fontScale, SettingsLayout.appIconMaxScale) else side
    val shape = RoundedCornerShape(scaled * 0.225f)
    val tile = if (dark) LogoGeometry.darkTile else LogoGeometry.lightTile
    val edge = if (dark) Color.White.copy(alpha = 0.14f) else SystemPalette.label.current.copy(alpha = 0.08f)
    Box(
        modifier
            .size(scaled)
            .let { if (shadow) it.shadow(10.dp, shape, ambientColor = Color.Black.copy(alpha = 0.12f), spotColor = Color.Black.copy(alpha = 0.12f)) else it }
            .clip(shape)
            .background(tile)
            .border(0.5.dp, edge, shape)
            .let { if (spins) it else it.clearAndSetSemantics {} },
        contentAlignment = Alignment.Center,
    ) {
        val mark = Modifier.size(scaled * LogoGeometry.TILE_SHARE)
        if (spins) {
            FidgetLogo(mark, contentDescription = stringResource(R.string.about_name), dark = dark, greetingSpin = greetingSpin, shadow = false)
        } else {
            AeriLogoMark(mark, dark = dark)
        }
    }
}
