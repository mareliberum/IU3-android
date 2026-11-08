package com.bmstu.iu3.features.honor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.Honor
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.honor.R
import com.bmstu.iu3.features.honor.ui.components.HonorPersonCard
import com.bmstu.iu3.features.honor.ui.format.isLegend

@Composable
fun HonorGridScreen(
    catalog: Catalog,
    legends: Boolean,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    Page(
        title = if (legends) stringResource(R.string.honor_legends) else stringResource(R.string.honor_graduates),
        back = back,
        profile = { navigate(Route(screen = Screen.Profile)) }) {
        items(
            catalog.honor.filter { it.isLegend() == legends }.chunked(2),
            key = { contentKey<Honor>(id = it.joinToString { person -> person.name }) },
            contentType = { Honor::class }) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { person ->
                    HonorPersonCard(person = person, modifier = Modifier.weight(1f)) {
                        navigate(
                            Route(
                                screen = if (legends) Screen.Legend else Screen.Graduate,
                                id = person.name
                            )
                        )
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}
