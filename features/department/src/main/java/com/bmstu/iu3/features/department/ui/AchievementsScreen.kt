package com.bmstu.iu3.features.department.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Achievement
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.ImageAssets
import com.bmstu.iu3.features.department.R

@Composable
fun AchievementsScreen(catalog: Catalog, back: () -> Unit) {
    Page(title = stringResource(R.string.department_achievements), back = back) {
        items(
            catalog.achievements,
            key = { contentKey<Achievement>(id = it.year.toString() + it.title) },
            contentType = { Achievement::class }) { item ->
            ItemCard(
                title = item.title,
                subtitle = item.year.toString() + " · " + item.description,
                image = ImageAssets.BENTO_ACHIEVEMENTS
            )
        }
    }
}
