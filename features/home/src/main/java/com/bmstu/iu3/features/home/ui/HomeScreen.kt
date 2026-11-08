package com.bmstu.iu3.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.CompactCard
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.MainPage
import com.bmstu.iu3.core.designsystem.PlainMainPage
import com.bmstu.iu3.core.designsystem.Section
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.designsystem.studyWeekTitle
import com.bmstu.iu3.core.model.AcademicTime
import com.bmstu.iu3.core.model.FacultyEvent
import com.bmstu.iu3.core.model.News
import com.bmstu.iu3.core.model.Project
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.home.R
import com.bmstu.iu3.features.home.presentation.HomeUiState
import com.bmstu.iu3.features.home.ui.format.eventDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    refresh: () -> Unit,
    navigate: (Route) -> Unit,
) {
    val catalog = state.catalog
    val profile = state.profile
    val offline = state.offline
    val moment = state.moment
    val today = moment.toLocalDate()
    val now = moment.toLocalTime()
    val subtitle = studyWeekTitle(date = today)
    var expanded by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (listState.firstVisibleItemIndex > 0) expanded = false
    }
    val remaining = state.remaining
    val feed: LazyListScope.() -> Unit = {
        remaining.firstOrNull()?.let { next ->
            item {
                ItemCard(
                    next.title,
                    next.from + " · " + next.room,
                    onClick = { navigate(Route(screen = Screen.Schedule)) })
                if (remaining.size > 1) TextButton({ expanded = !expanded }) {
                    Text(
                        if (expanded) stringResource(R.string.home_collapse_lessons) else stringResource(
                            R.string.home_more_lessons,
                            remaining.size - 1
                        )
                    )
                }
                if (expanded) remaining.drop(1).forEach { lesson ->
                    ItemCard(
                        lesson.title,
                        lesson.from + " · " + lesson.room,
                        onClick = { navigate(Route(screen = Screen.Schedule)) })
                }
            }
        }
        if (offline) item { Text(stringResource(R.string.home_offline), color = AppGray) }
        item {
            Section(
                title = stringResource(R.string.home_events),
                onAll = { navigate(Route(screen = Screen.Events)) })
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    state.featured,
                    key = { contentKey<FacultyEvent>(id = it.id) },
                    contentType = { FacultyEvent::class }) { event ->
                    Card(
                        onClick = { navigate(Route(screen = Screen.Event, id = event.id)) },
                        modifier = Modifier.width(270.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = AppCard)
                    ) {
                        FacultyImage(
                            event.image,
                            Modifier.fillMaxWidth().height(154.dp),
                            shape = RoundedCornerShape(0.dp)
                        )
                        Column(
                            Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(eventDate(event = event), color = AppGray, fontSize = 13.sp)
                            Text(
                                event.title,
                                fontSize = 17.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.SemiBold,
                                minLines = 2,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
        if (catalog.news.isNotEmpty()) {
            item {
                Section(
                    title = stringResource(R.string.home_news),
                    onAll = { navigate(Route(screen = Screen.NewsList)) })
            }
            items(
                catalog.news.take(6),
                key = { contentKey<News>(id = it.id) },
                contentType = { News::class }) { news ->
                CompactCard(
                    title = news.title,
                    subtitle = news.date,
                    image = news.images.firstOrNull()
                ) { navigate(Route(screen = Screen.News, id = news.id)) }
            }
        }
        item {
            Spacer(Modifier.height(2.dp)); Section(
            title = stringResource(R.string.home_projects),
            onAll = { navigate(Route(screen = Screen.Projects)) })
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(
                    state.projects,
                    key = { contentKey<Project>(id = it.title) },
                    contentType = { Project::class }) { project ->
                    Card(
                        onClick = { navigate(Route(screen = Screen.Project, id = project.title)) },
                        modifier = Modifier.width(252.dp).height(166.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            FacultyImage(
                                project.image,
                                Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(0.dp)
                            )
                            Box(
                                Modifier.fillMaxSize().background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = .78f)
                                        )
                                    )
                                )
                            )
                            Column(
                                Modifier.align(Alignment.BottomStart).padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    project.title,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    project.subtitle,
                                    color = Color.White.copy(alpha = .8f),
                                    fontSize = 13.sp,
                                    lineHeight = 16.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    PullToRefreshBox(isRefreshing = false, onRefresh = refresh) {
        if (profile.role in listOf(Role.Student, Role.Teacher, null)) MainPage(
            today.format(
                AcademicTime.DayMonth
            ),
            subtitle,
            profile = { navigate(Route(screen = Screen.Profile)) },
            listState = listState,
            topPadding = 8.dp,
            content = feed
        )
        else PlainMainPage(
            stringResource(R.string.home_home),
            profile = { navigate(Route(screen = Screen.Profile)) },
            content = feed
        )
    }
}
