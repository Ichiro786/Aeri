package com.ardia.design

import androidx.compose.ui.unit.dp

object SettingsTile {
    val side = 29.dp
    val heroSide = 64.dp
}

object SettingsTint {
    val neutral = aeriColor(0x86888D)
    val red = aeriColor(0xFF5A5F)
    val orange = aeriColor(0xFF901A)
    val yellow = aeriColor(0xFFC829)
    val blue = aeriColor(0x458AFF)
    val deepBlue = aeriColor(0x007AFF)
    val purple = aeriColor(0xB059EE)
    val green = aeriColor(0x3EA82E)

    val sync = blue
    val ads = red
    val trackers = yellow
    val cookies = orange
    val search = deepBlue
    val archive = purple
    val keyboard = yellow
    val swipe = blue
    val nightShade = aeriColor(0x5856D6)
    val appLinks = green
    val bottomBar = aeriColor(0x30B0C7)
    val defaultBrowser = deepBlue
    val clearData = red
    val export = aeriColor(0x0153FF)
    val onboarding = orange
}

object MenuLayout {
    val sheetRadius = 38.dp
    val inset = 16.dp
    val innerRadius = sheetRadius - inset
    val capsuleHeight = innerRadius * 2
    val tileHeight = 68.dp
    val rowHeight = 50.dp
}

object AeriColors {
    val accentText = Palette.formAccent
    val title = SystemPalette.label
}
