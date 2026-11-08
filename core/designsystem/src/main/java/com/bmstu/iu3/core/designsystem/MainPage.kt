package com.bmstu.iu3.core.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val LocalNavigationInset = staticCompositionLocalOf { 0.dp }

@Composable
fun BlueHeader(
    title: String,
    subtitle: String? = null,
    profile: (() -> Unit)? = null,
) {
    Surface(color = AppBlue, shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)) {
        Row(
            Modifier.fillMaxWidth().statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    title, color = Color.White.copy(alpha = .92f), fontSize = 31.sp,
                    lineHeight = 37.sp, fontWeight = FontWeight.Bold
                )
                if (!subtitle.isNullOrBlank()) Text(
                    subtitle,
                    color = Color.White.copy(alpha = .8f),
                    fontSize = 14.sp
                )
            }
            if (profile != null) IconButton(profile) {
                Icon(
                    Icons.Default.Person,
                    stringResource(R.string.ds_profile),
                    tint = Color.White.copy(alpha = .9f),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
fun MainPage(
    title: String,
    subtitle: String? = null,
    profile: (() -> Unit)? = null,
    listState: LazyListState = rememberLazyListState(),
    branded: Boolean = true,
    topPadding: Dp = 14.dp,
    content: LazyListScope.() -> Unit,
) {
    if (!branded) {
        PlainMainPage(title, profile = profile, titleColor = AppLink, content = content)
        return
    }
    Surface(color = AppBackground) {
        Column(Modifier.fillMaxSize()) {
            BlueHeader(title = title, subtitle = subtitle, profile = profile)
            LazyColumn(
                Modifier.weight(1f).fillMaxWidth(), state = listState,
                contentPadding = PaddingValues(
                    start = 14.dp, end = 14.dp, top = topPadding,
                    bottom = LocalNavigationInset.current + WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp), content = content
            )
        }
    }
}

@Composable
fun SegmentedControl(
    first: String,
    second: String,
    secondSelected: Boolean,
    onSelect: (Boolean) -> Unit,
) {
    Surface(
        color = AppCard,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(first, second).forEachIndexed { index, label ->
                val selected = secondSelected == (index == 1)
                Surface(
                    onClick = { onSelect(index == 1) },
                    modifier = Modifier.weight(1f),
                    color = if (selected) AppBlue else Color.Transparent,
                    shape = RoundedCornerShape(14.dp),
                    shadowElevation = if (selected) 3.dp else 0.dp
                ) {
                    Box(Modifier.height(46.dp), contentAlignment = Alignment.Center) {
                        Text(
                            label,
                            color = if (selected) Color.White else AppText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompactCard(
    title: String,
    subtitle: String,
    image: String?,
    onClick: () -> Unit,
) {
    Card(
        onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = AppCard)
    ) {
        Row(Modifier.height(116.dp)) {
            FacultyImage(
                image,
                Modifier.width(110.dp).fillMaxHeight(),
                shape = RoundedCornerShape(0.dp)
            )
            Column(
                Modifier.weight(1f).padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    title,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(subtitle, color = AppGray, fontSize = 13.sp, lineHeight = 16.sp, maxLines = 1)
            }
        }
    }
}

@Composable
fun PlainMainPage(
    title: String,
    profile: (() -> Unit)? = null,
    titleColor: Color = AppText,
    listState: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    Surface(color = AppBackground) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Row(
                Modifier.fillMaxWidth()
                    .padding(start = 16.dp, end = 8.dp, top = 10.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title,
                    color = titleColor,
                    fontSize = 27.sp,
                    lineHeight = 33.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (profile != null) IconButton(profile) {
                    Icon(
                        Icons.Default.Person,
                        stringResource(R.string.ds_profile)
                    )
                }
            }
            LazyColumn(
                Modifier.weight(1f).fillMaxWidth().imePadding(), state = listState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = LocalNavigationInset.current + 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp), content = content
            )
        }
    }
}
