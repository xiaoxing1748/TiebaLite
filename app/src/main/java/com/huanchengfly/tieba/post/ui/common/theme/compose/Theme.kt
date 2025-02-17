package com.huanchengfly.tieba.post.ui.common.theme.compose

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.material.ContentAlpha
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.huanchengfly.tieba.post.R
import com.huanchengfly.tieba.post.rememberPreferenceAsState
import com.huanchengfly.tieba.post.ui.common.theme.ThemeDelegate
import com.huanchengfly.tieba.post.utils.ThemeUtil
import com.huanchengfly.tieba.post.utils.appPreferences
import com.huanchengfly.tieba.post.utils.compose.darken

@Stable
data class ExtendedColorScheme(
    val theme: String,
    val isNightMode: Boolean,
    val primary: Color = Color.Unspecified,
    val onPrimary: Color = Color.Unspecified,
    val accent: Color = Color.Unspecified,
    val onAccent: Color = Color.Unspecified,
    val topBar: Color = Color.Unspecified,
    val onTopBar: Color = Color.Unspecified,
    val onTopBarSecondary: Color = Color.Unspecified,
    val onTopBarActive: Color = Color.Unspecified,
    val topBarSurface: Color = Color.Unspecified,
    val onTopBarSurface: Color = Color.Unspecified,
    val bottomBar: Color = Color.Unspecified,
    val bottomBarSurface: Color = Color.Unspecified,
    val onBottomBarSurface: Color = Color.Unspecified,
    val text: Color = Color.Unspecified,
    val textSecondary: Color = Color.Unspecified,
    val textDisabled: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val chip: Color = Color.Unspecified,
    val onChip: Color = Color.Unspecified,
    val unselected: Color = Color.Unspecified,
    val card: Color = Color.Unspecified,
    val floorCard: Color = Color.Unspecified,
    val divider: Color = Color.Unspecified,
    val shadow: Color = Color.Unspecified,
    val indicator: Color = Color.Unspecified,
    val windowBackground: Color = Color.Unspecified,
    val placeholder: Color = Color.Unspecified,
)

private val LocalExtendedColorScheme = staticCompositionLocalOf {
    ExtendedColorScheme(
        ThemeUtil.THEME_DEFAULT,
        false,
    )
}


@SuppressLint("ConflictingOnColor")
fun getColorPalette(
    darkTheme: Boolean,
    extendedColorScheme: ExtendedColorScheme
): ColorScheme {
    val primaryColor = extendedColorScheme.accent
    val secondaryColor = extendedColorScheme.primary
    return if (darkTheme) {
        darkColors(
            primary = primaryColor,
            primaryVariant = primaryColor.darken(),
            secondary = secondaryColor,
            secondaryVariant = Color(0xFF3F310A),
            onSecondary = Color(0xFFFFFFFF),
            background = extendedColorScheme.background,
            onBackground = extendedColorScheme.text,
            surface = extendedColorScheme.card,
        )
        darkColorScheme()
    } else {
        lightColors(
            primary = primaryColor,
            primaryVariant = primaryColor.darken(),
            secondary = secondaryColor,
            secondaryVariant = Color(0xFF000000),
            onSecondary = Color(0xFFFFFFFF),
            background = extendedColorScheme.background,
            onBackground = extendedColorScheme.text,
            surface = extendedColorScheme.card,
        )
        lightColorScheme()
    }
}

@Composable
private fun getDynamicColor(
    theme: String,
    tonalPalette: TonalPalette,
): ExtendedColorScheme {
    val isDarkColorPalette = ThemeUtil.isNightMode(theme)
    return if (isDarkColorPalette) {
        if (theme == ThemeUtil.THEME_AMOLED_DARK) {
            getBlackDarkDynamicColor(tonalPalette)
        } else {
            getDarkDynamicColor(tonalPalette)
        }
    } else {
        getLightDynamicColor(tonalPalette)
    }
}

@Composable
private fun getDynamicTopBarColor(
    tonalPalette: TonalPalette,
    isNightMode: Boolean = false,
): Color {
    val topBarUsePrimaryColor = LocalContext.current.appPreferences.toolbarPrimaryColor
    val primaryColor = tonalPalette.primary40
    val backgroundColor = tonalPalette.neutralVariant99
    return if (topBarUsePrimaryColor) {
        primaryColor
    } else {
        backgroundColor
    }
}

@Composable
private fun getDynamicOnTopBarColor(
    tonalPalette: TonalPalette,
): Color {
    val topBarUsePrimaryColor =
        LocalContext.current.appPreferences.toolbarPrimaryColor
    val onPrimaryColor = tonalPalette.primary100
    val onBackgroundColor = tonalPalette.neutralVariant10
    return if (topBarUsePrimaryColor) {
        onPrimaryColor
    } else {
        onBackgroundColor
    }
}

@Composable
private fun getDynamicOnTopBarSecondaryColor(
    tonalPalette: TonalPalette,
    isNightMode: Boolean = false,
): Color {
    val topBarUsePrimaryColor =
        LocalContext.current.appPreferences.toolbarPrimaryColor
    val primaryColor = tonalPalette.primary80
    val backgroundColor = tonalPalette.neutralVariant40
    return if (topBarUsePrimaryColor) {
        primaryColor
    } else {
        backgroundColor
    }
}

@Composable
private fun getDynamicOnTopBarActiveColor(
    tonalPalette: TonalPalette,
): Color {
    val topBarUsePrimaryColor =
        LocalContext.current.appPreferences.toolbarPrimaryColor
    val primaryColor = tonalPalette.primary100
    val backgroundColor = tonalPalette.neutralVariant0
    return if (topBarUsePrimaryColor) {
        primaryColor
    } else {
        backgroundColor
    }
}

@Composable
private fun getDynamicTopBarSurfaceColor(
    tonalPalette: TonalPalette,
): Color {
    val topBarUsePrimaryColor =
        LocalContext.current.appPreferences.toolbarPrimaryColor
    val primaryColor = tonalPalette.primary90
    val backgroundColor = tonalPalette.neutralVariant95
    return if (topBarUsePrimaryColor) {
        primaryColor
    } else {
        backgroundColor
    }
}

@Composable
private fun getDynamicOnTopBarSurfaceColor(
    tonalPalette: TonalPalette,
): Color {
    val topBarUsePrimaryColor =
        LocalContext.current.appPreferences.toolbarPrimaryColor
    val primaryColor = tonalPalette.primary10
    val backgroundColor = tonalPalette.neutralVariant30
    return if (topBarUsePrimaryColor) {
        primaryColor
    } else {
        backgroundColor
    }
}

@Composable
private fun getLightDynamicColor(tonalPalette: TonalPalette): ExtendedColorScheme {
    return ExtendedColorScheme(
        theme = "dynamic",
        isNightMode = false,
        primary = tonalPalette.primary40,
        onPrimary = tonalPalette.primary100,
        accent = tonalPalette.secondary40,
        onAccent = tonalPalette.secondary100,
        topBar = getDynamicTopBarColor(tonalPalette),
        onTopBar = getDynamicOnTopBarColor(tonalPalette),
        onTopBarSecondary = getDynamicOnTopBarSecondaryColor(tonalPalette),
        onTopBarActive = getDynamicOnTopBarActiveColor(tonalPalette),
        topBarSurface = getDynamicTopBarSurfaceColor(tonalPalette),
        onTopBarSurface = getDynamicOnTopBarSurfaceColor(tonalPalette),
        bottomBar = tonalPalette.neutralVariant99,
        bottomBarSurface = tonalPalette.neutralVariant95,
        onBottomBarSurface = tonalPalette.neutralVariant30,
        text = tonalPalette.neutralVariant10,
        textSecondary = tonalPalette.neutralVariant40,
        textDisabled = tonalPalette.neutralVariant70,
        background = tonalPalette.neutralVariant99,
        chip = tonalPalette.neutralVariant95,
        onChip = tonalPalette.neutralVariant40,
        unselected = tonalPalette.neutralVariant60,
        card = tonalPalette.neutralVariant99,
        floorCard = tonalPalette.neutralVariant95,
        divider = tonalPalette.neutralVariant95,
        shadow = tonalPalette.neutralVariant90,
        indicator = tonalPalette.neutralVariant95,
        windowBackground = tonalPalette.neutralVariant99,
        placeholder = tonalPalette.neutralVariant100,
    )
}

@Composable
private fun getDarkDynamicColor(tonalPalette: TonalPalette): ExtendedColorScheme {
    return ExtendedColorScheme(
        theme = "dynamic",
        isNightMode = true,
        primary = tonalPalette.primary80,
        onPrimary = tonalPalette.primary10,
        accent = tonalPalette.secondary80,
        onAccent = tonalPalette.secondary20,
        topBar = tonalPalette.neutralVariant10,
        onTopBar = tonalPalette.neutralVariant90,
        onTopBarSecondary = tonalPalette.neutralVariant70,
        onTopBarActive = tonalPalette.neutralVariant100,
        topBarSurface = tonalPalette.neutralVariant20,
        onTopBarSurface = tonalPalette.neutralVariant70,
        bottomBar = tonalPalette.neutralVariant10,
        bottomBarSurface = tonalPalette.neutralVariant20,
        onBottomBarSurface = tonalPalette.neutralVariant70,
        text = tonalPalette.neutralVariant90,
        textSecondary = tonalPalette.neutralVariant70,
        textDisabled = tonalPalette.neutralVariant50,
        background = tonalPalette.neutralVariant10,
        chip = tonalPalette.neutralVariant20,
        onChip = tonalPalette.neutralVariant60,
        unselected = tonalPalette.neutralVariant40,
        card = tonalPalette.neutralVariant20,
        floorCard = tonalPalette.neutralVariant20,
        divider = tonalPalette.neutralVariant20,
        shadow = tonalPalette.neutralVariant20,
        indicator = tonalPalette.neutralVariant10,
        windowBackground = tonalPalette.neutralVariant10,
        placeholder = tonalPalette.neutralVariant50,
    )
}

@Composable
private fun getBlackDarkDynamicColor(tonalPalette: TonalPalette): ExtendedColorScheme {
    return ExtendedColorScheme(
        theme = "dynamic",
        isNightMode = true,
        primary = tonalPalette.primary80,
        onPrimary = tonalPalette.primary10,
        accent = tonalPalette.secondary80,
        onAccent = tonalPalette.secondary20,
        topBar = tonalPalette.neutralVariant0,
        onTopBar = tonalPalette.neutralVariant90,
        onTopBarSecondary = tonalPalette.neutralVariant70,
        onTopBarActive = tonalPalette.neutralVariant100,
        topBarSurface = tonalPalette.neutralVariant10,
        onTopBarSurface = tonalPalette.neutralVariant70,
        bottomBar = tonalPalette.neutralVariant0,
        bottomBarSurface = tonalPalette.neutralVariant10,
        onBottomBarSurface = tonalPalette.neutralVariant70,
        text = tonalPalette.neutralVariant90,
        textSecondary = tonalPalette.neutralVariant70,
        textDisabled = tonalPalette.neutralVariant50,
        background = tonalPalette.neutralVariant0,
        chip = tonalPalette.neutralVariant10,
        onChip = tonalPalette.neutralVariant50,
        unselected = tonalPalette.neutralVariant40,
        card = tonalPalette.neutralVariant10,
        floorCard = tonalPalette.neutralVariant10,
        divider = tonalPalette.neutralVariant10,
        shadow = tonalPalette.neutralVariant10,
        indicator = tonalPalette.neutralVariant10,
        windowBackground = tonalPalette.neutralVariant0,
        placeholder = tonalPalette.neutralVariant50,
    )
}

@Composable
private fun getThemeColorForTheme(theme: String): ExtendedColorScheme {
    val context = LocalContext.current
    val nowTheme = ThemeUtil.getCurrentTheme(theme)
    val textColor =
        Color(ThemeDelegate.getColorByAttr(context, R.attr.colorText, nowTheme))
    val bottomBarColor =
        Color(ThemeDelegate.getColorByAttr(context, R.attr.colorNavBar, nowTheme))
    return ExtendedColorScheme(
        theme = nowTheme,
        isNightMode = ThemeUtil.isNightMode(nowTheme),
        primary = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorNewPrimary,
                nowTheme
            )
        ),
        onPrimary = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorOnAccent,
                nowTheme
            )
        ),
        accent = Color(ThemeDelegate.getColorByAttr(context, R.attr.colorAccent, nowTheme)),
        onAccent = Color(ThemeDelegate.getColorByAttr(context, R.attr.colorOnAccent, nowTheme)),
        topBar = Color(ThemeDelegate.getColorByAttr(context, R.attr.colorToolbar, nowTheme)),
        onTopBar = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorToolbarItem,
                nowTheme
            )
        ),
        onTopBarSecondary = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorToolbarItemSecondary,
                nowTheme
            )
        ),
        onTopBarActive = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorToolbarItemActive,
                nowTheme
            )
        ),
        topBarSurface = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorToolbarSurface,
                nowTheme
            )
        ),
        onTopBarSurface = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorOnToolbarSurface,
                nowTheme
            )
        ),
        bottomBar = bottomBarColor,
        bottomBarSurface = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorNavBarSurface,
                nowTheme
            )
        ),
        onBottomBarSurface = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorOnNavBarSurface,
                nowTheme
            )
        ),
        text = textColor.copy(alpha = ContentAlpha.high),
        textSecondary = textColor.copy(alpha = ContentAlpha.medium),
        textDisabled = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.color_text_disabled,
                nowTheme
            )
        ),
        background = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorBackground,
                nowTheme
            )
        ),
        chip = Color(ThemeDelegate.getColorByAttr(context, R.attr.colorChip, nowTheme)),
        onChip = Color(ThemeDelegate.getColorByAttr(context, R.attr.colorOnChip, nowTheme)),
        unselected = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorUnselected,
                nowTheme
            )
        ),
        card = Color(ThemeDelegate.getColorByAttr(context, R.attr.colorCard, nowTheme)),
        floorCard = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorFloorCard,
                nowTheme
            )
        ),
        divider = Color(ThemeDelegate.getColorByAttr(context, R.attr.colorDivider, nowTheme)),
        shadow = Color(ThemeDelegate.getColorByAttr(context, R.attr.shadow_color, nowTheme)),
        indicator = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorIndicator,
                nowTheme
            )
        ),
        windowBackground = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorWindowBackground,
                nowTheme
            )
        ),
        placeholder = Color(
            ThemeDelegate.getColorByAttr(
                context,
                R.attr.colorPlaceholder,
                nowTheme
            )
        ),
    )
}

@Composable
fun TiebaLiteTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val theme by remember { ThemeUtil.themeState }
    val isDynamicTheme by rememberPreferenceAsState(
        key = booleanPreferencesKey("useDynamicColorTheme"),
        defaultValue = false
    )
    val isDarkColorPalette by remember {
        derivedStateOf {
            ThemeUtil.isNightMode(theme) || (ThemeUtil.isTranslucentTheme(theme) && theme.contains("light"))
        }
    }

    val useDynamicTheme = isDynamicTheme && Build.VERSION.SDK_INT >= 31

    val extendedColorScheme = if (!useDynamicTheme || ThemeUtil.isTranslucentTheme(theme)) {
        getThemeColorForTheme(theme)
    } else {
        getDynamicColor(theme, dynamicTonalPalette(context))
    }

    val colors = getColorPalette(isDarkColorPalette, extendedColorScheme)

    CompositionLocalProvider(LocalExtendedColorScheme provides extendedColorScheme) {
        MaterialTheme(
            colorScheme = colors,
            typography = TiebaTypography,
            shapes = TiebaShapes,
            content = content
        )
    }
}

object ExtendedTheme {
    val colorScheme: ExtendedColorScheme
        @Composable
        get() = LocalExtendedColorScheme.current
}
