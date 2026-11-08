package com.bmstu.iu3.features.home.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.News
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.home.R

@Composable
fun NewsListScreen(
    catalog: Catalog,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    Page(title = stringResource(R.string.home_all_news), back = back) {
        if (catalog.news.isEmpty()) item { EmptyState(title = stringResource(R.string.home_no_news)) }
        items(
            catalog.news,
            key = { contentKey<News>(id = it.id) },
            contentType = { News::class }) { news ->
            ItemCard(
                news.title,
                news.date,
                news.images.firstOrNull(),
                onClick = { navigate(Route(screen = Screen.News, id = news.id)) })
        }
    }
}
