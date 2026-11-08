package com.bmstu.iu3.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.bmstu.iu3.core.designsystem.R
import com.bmstu.iu3.core.model.EducationLevel

val AppBlue = Color(0xFF0968F9)

val AppBlack = Color(0xFF0C0C0C)

val LocalDarkTheme = staticCompositionLocalOf { false }

val AppBackground: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.background

val AppSurface: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surface

val AppText: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurface

val AppLink: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary

val AppCard: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primaryContainer

val AppLightCard: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceVariant

val AppSeparator: Color @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outlineVariant

val AppSelectedTab: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFF353D49
    ) else Color(0xFFEAEAEA)

val AppSearchField: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFF1D2632
    ) else Color(0xFFEEF7FF)

val AppRoomChip: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFF263D64
    ) else Color(0xFFDFE9FF)

val AppLessonChip: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFF303540
    ) else Color(0xFFEEEEF1)

val AppLessonText: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFFC9CFDA
    ) else Color(0xFF515564)

val AppGray: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFFA5ADB9
    ) else Color(0xFF888888)

val AppStrongSelection: Color @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) AppBlue else AppBlack

val AppLegendGold = Color(0xFFE8C27B)

val AppSteel = Color(0xFFDADCE0)

val AppControlGreen: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFF007A5F
    ) else Color(0xFF009E78)

val AppControlOrange: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFFB3442A
    ) else Color(0xFFD95433)

val AppControlNeutral: Color
    @Composable @ReadOnlyComposable get() = if (LocalDarkTheme.current) Color(
        0xFF596475
    ) else Color(0xFF888888)

val AppHonorGoldGradient =
    listOf(Color(0xFFD99C27), Color(0xFFFEE18B), Color(0xFFE5B33E), Color(0xFFCA8D1D))

val AppHonorSteelGradient = listOf(
    Color(0xFFD8D8D8),
    Color(0xFFBEBEBE),
    Color(0xFFF5F5F5),
    Color(0xFFBDBDBD),
    Color(0xFFECECEC),
    Color(0xFFB0B0B0)
)

val AppHonorMedallionGradient = listOf(Color(0xFFF5D999), AppLegendGold, Color(0xFFCC9E52))

val AppHonorBronze = Color(0xFF7A541F)

private val LightColors = lightColorScheme(
    primary = AppBlue,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = AppBlack,
    surface = Color.White,
    onSurface = AppBlack,
    primaryContainer = Color(0xFFE2F0FC),
    onPrimaryContainer = AppBlack,
    secondary = AppBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE2F0FC),
    onSecondaryContainer = AppBlack,
    surfaceVariant = Color(0xFFEDF2FF),
    onSurfaceVariant = AppBlack,
    surfaceContainer = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color(0xFFEDF2FF),
    surfaceContainerLowest = Color.White,
    outline = Color(0xFFE4E4E8),
    outlineVariant = Color(0xFFE4E4E8),
    surfaceTint = Color.Transparent,
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8BB8FF),
    onPrimary = Color(0xFF102E55),
    background = Color(0xFF111318),
    onBackground = Color(0xFFEEF1F6),
    surface = Color(0xFF181C22),
    onSurface = Color(0xFFEEF1F6),
    primaryContainer = Color(0xFF202F40),
    onPrimaryContainer = Color(0xFFEEF1F6),
    secondary = Color(0xFF8BB8FF),
    onSecondary = Color(0xFF102E55),
    secondaryContainer = Color(0xFF202F40),
    onSecondaryContainer = Color(0xFFEEF1F6),
    surfaceVariant = Color(0xFF242C3A),
    onSurfaceVariant = Color(0xFFB9C1CC),
    surfaceContainer = Color(0xFF1D222A),
    surfaceContainerLow = Color(0xFF191E25),
    surfaceContainerHigh = Color(0xFF252D38),
    surfaceContainerHighest = Color(0xFF313B48),
    surfaceContainerLowest = Color(0xFF0B0E12),
    outline = Color(0xFF626D7E),
    outlineVariant = Color(0xFF39424F),
    surfaceTint = Color.Transparent,
)

val LocalFaceCenters = staticCompositionLocalOf<Map<String, Pair<Float, Float>>> { emptyMap() }

val LocalImages = staticCompositionLocalOf<Map<String, String>> { emptyMap() }

@Composable
fun IU3Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    CompositionLocalProvider(LocalDarkTheme provides darkTheme) {
        MaterialTheme(
            colorScheme = colors, typography = Typography(
                bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, letterSpacing = 0.sp),
                bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.sp),
                bodySmall = TextStyle(fontSize = 12.sp, lineHeight = 16.sp),
                titleMedium = TextStyle(
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                titleLarge = TextStyle(
                    fontSize = 24.sp,
                    lineHeight = 29.sp,
                    fontWeight = FontWeight.Bold
                ),
                headlineMedium = TextStyle(
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold
                ),
                headlineLarge = TextStyle(
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Bold
                ),
                labelLarge = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                ),
                labelMedium = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                labelSmall = TextStyle(fontSize = 11.sp, lineHeight = 14.sp),
            )
        ) {
            Surface(
                Modifier.fillMaxSize(),
                color = colors.background,
                contentColor = colors.onBackground
            ) { content() }
        }
    }
}

@Composable
fun FacultyImage(
    name: String?,
    modifier: Modifier = Modifier,
    description: String? = null,
    crop: Boolean = true,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp),
    background: Color = AppCard,
) {
    val model = FacultyImageSource.resolve(name, LocalImages.current)
    val center = LocalFaceCenters.current[name]
    val alignment = center?.let { FaceAlignment(x = it.first, y = it.second) } ?: Alignment.Center
    Surface(modifier = modifier.clip(shape), shape = shape, color = background) {
        AsyncImage(
            model = model,
            contentDescription = description,
            alignment = alignment,
            contentScale = if (crop) ContentScale.Crop else ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Page(
    title: String,
    back: (() -> Unit)? = null,
    profile: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                if (back != null) OutlinedIconButton(
                    onClick = back,
                    border = androidx.compose.foundation.BorderStroke(.5.dp, AppSeparator)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.ds_back))
                }
            }, actions = {
                actions(); if (profile != null) OutlinedIconButton(
                onClick = profile,
                border = androidx.compose.foundation.BorderStroke(.5.dp, AppSeparator)
            ) {
                Icon(Icons.Default.Person, stringResource(R.string.ds_profile))
            }
            })
    }) { padding ->
        LazyColumn(
            Modifier.fillMaxSize().consumeWindowInsets(padding).imePadding(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = padding.calculateTopPadding() + 12.dp,
                bottom = padding.calculateBottomPadding() + LocalNavigationInset.current + 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp), content = content
        )
    }
}

@Composable
fun Section(title: String, onAll: (() -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        if (onAll != null) TextButton(onClick = onAll) {
            Text(
                stringResource(R.string.ds_all),
                color = AppText,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ItemCard(
    title: String,
    subtitle: String = "",
    image: String? = null,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val body: @Composable ColumnScope.() -> Unit = {
        if (image != null) FacultyImage(
            name = image,
            modifier = Modifier.fillMaxWidth().height(176.dp)
        )
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            if (subtitle.isNotBlank()) Text(
                subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = AppGray
            )
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    if (onClick == null) Card(
        modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppCard), content = body
    )
    else Card(
        onClick = onClick, modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppCard), content = body
    )
}

@Composable
fun EmptyState(title: String, subtitle: String = "") {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if (subtitle.isNotBlank()) Text(subtitle, color = AppGray)
    }
}

@Composable
fun educationLevelTitle(level: EducationLevel): String = when (level) {
    EducationLevel.Bachelor -> stringResource(R.string.ds_degree_bachelor)
    EducationLevel.Master -> stringResource(R.string.ds_degree_master)
    else -> level.code
}
