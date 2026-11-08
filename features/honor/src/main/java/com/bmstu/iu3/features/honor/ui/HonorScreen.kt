package com.bmstu.iu3.features.honor.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.Honor
import com.bmstu.iu3.core.model.HonorKind
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.honor.R
import com.bmstu.iu3.features.honor.ui.components.HonorPersonCard
import com.bmstu.iu3.features.honor.ui.components.RecommendGraduateCard
import com.bmstu.iu3.features.honor.ui.format.isLegend

@Composable
fun HonorScreen(
    catalog: Catalog,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    Page(
        title = stringResource(R.string.honor_honor),
        back = back,
        profile = { navigate(Route(screen = Screen.Profile)) }) {
        listOf(true, false).forEach { legends ->
            item {
                Box(Modifier.padding(top = if (legends) 0.dp else 12.dp)) {
                    Section(
                        title = if (legends) stringResource(R.string.honor_legends) else stringResource(
                            R.string.honor_graduates
                        ),
                        onAll = {
                            navigate(
                                Route(
                                    Screen.HonorList,
                                    arguments = RouteArguments.HonorList(if (legends) HonorKind.Legend else HonorKind.Graduate)
                                )
                            )
                        })
                }
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(
                        catalog.honor.filter { it.isLegend() == legends },
                        key = { contentKey<Honor>(id = it.name) },
                        contentType = { Honor::class }) { person ->
                        HonorPersonCard(person = person, modifier = Modifier.width(165.dp)) {
                            navigate(
                                Route(
                                    screen = if (legends) Screen.Legend else Screen.Graduate,
                                    id = person.name
                                )
                            )
                        }
                    }
                    if (!legends) item {
                        RecommendGraduateCard(modifier = Modifier.width(165.dp)) {
                            navigate(
                                Route(screen = Screen.Recommendation)
                            )
                        }
                    }
                }
            }
        }
    }
}
