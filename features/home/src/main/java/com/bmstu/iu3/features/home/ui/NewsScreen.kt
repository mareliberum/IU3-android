package com.bmstu.iu3.features.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.LinkedText
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.PhotoGallery
import com.bmstu.iu3.core.model.News
import com.bmstu.iu3.core.navigation.external.ExternalUris
import com.bmstu.iu3.features.home.R

@Composable
fun NewsScreen(
    news: News,
    back: () -> Unit,
    share: (String) -> Unit,
    open: (String) -> Unit,
) {
    var gallery by rememberSaveable(news.id) { mutableIntStateOf(-1) }
    if (gallery >= 0) PhotoGallery(
        photos = news.images.map { it to "" },
        index = gallery
    ) { gallery = -1 }
    Page(title = stringResource(R.string.home_news_context), back = back) {
        if (news.images.isNotEmpty()) item {
            val pager = rememberPagerState { news.images.size }
            HorizontalPager(pager) {
                FacultyImage(
                    name = news.images[it],
                    modifier = Modifier.fillMaxWidth().height(280.dp).clickable { gallery = it })
            }
            Text(stringResource(R.string.home_photo_count, pager.currentPage + 1, news.images.size))
        }
        item {
            Text(news.title, style = MaterialTheme.typography.headlineMedium); Text(
            news.date,
            color = AppGray
        ); LinkedText(text = news.text, open = open)
        }
        item {
            Text(
                stringResource(
                    R.string.home_stats,
                    news.likes,
                    news.views
                )
            ); TextButton({ share(ExternalUris.news(news.id)) }) { Text(stringResource(R.string.home_share)) }
        }
    }
}
