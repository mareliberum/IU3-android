package com.bmstu.iu3.features.search.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.AppSearchField
import com.bmstu.iu3.core.designsystem.AppSeparator
import com.bmstu.iu3.core.designsystem.AppText
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.LocalDarkTheme
import com.bmstu.iu3.core.designsystem.PlainMainPage
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.AdmissionProgramId
import com.bmstu.iu3.core.model.StudyProgram
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.search.R
import com.bmstu.iu3.features.search.domain.SearchResult
import com.bmstu.iu3.features.search.domain.SearchResultKind
import com.bmstu.iu3.features.search.presentation.SearchUiState

@Composable
fun SearchScreen(
    state: SearchUiState,
    focusRequest: Int,
    updateQuery: (String) -> Unit,
    remember: (String) -> Unit,
    forget: (String) -> Unit,
    navigate: (Route) -> Unit,
) {
    val query = state.query
    val recent = state.recent
    val candidates = state.popular
    val focus = androidx.compose.runtime.remember { FocusRequester() }
    val listState = rememberLazyListState()
    val keyboard = LocalSoftwareKeyboardController.current
    var handledFocusRequest by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(focusRequest) {
        if (focusRequest > handledFocusRequest) {
            handledFocusRequest = focusRequest
            listState.scrollToItem(0)
            withFrameNanos { }
            focus.requestFocus()
            withFrameNanos { }
            keyboard?.show()
        }
    }
    PlainMainPage(stringResource(R.string.search_search), listState = listState) {

        item {
            TextField(
                query,
                { updateQuery(it) },
                placeholder = {
                    Text(
                        stringResource(R.string.search_enter_query),
                        color = if (LocalDarkTheme.current) AppGray else AppGray.copy(alpha = .5f)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = AppGray) },
                trailingIcon = {
                    if (query.isNotEmpty()) IconButton({
                        updateQuery("")
                    }) { Icon(Icons.Default.Close, stringResource(R.string.search_clear_query)) }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = AppSearchField,
                    unfocusedContainerColor = AppSearchField,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { remember(query) })
            )
        }
        if (query.isBlank()) {
            if (recent.isNotEmpty()) item {
                Text(
                    stringResource(R.string.search_recent),
                    fontSize = 13.sp,
                    color = AppGray,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            if (recent.isNotEmpty()) item {
                Column {
                    recent.forEach { value ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .heightIn(min = 48.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                Modifier
                                    .weight(1f)
                                    .clickable { updateQuery(value); remember(value) }
                                    .padding(vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.History,
                                    null,
                                    tint = AppText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    value,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AppText,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            IconButton({ forget(value) }) {
                                Icon(
                                    Icons.Default.Close,
                                    stringResource(R.string.search_remove),
                                    tint = AppText
                                )
                            }
                        }
                        HorizontalDivider(color = AppSeparator)
                    }
                }
            }
            item {
                Text(
                    stringResource(R.string.search_popular),
                    fontSize = 13.sp,
                    color = AppGray,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    candidates.forEach { value ->
                        Surface(
                            modifier = Modifier.clickable { updateQuery(value); remember(value) },
                            color = AppCard,
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Text(
                                value,
                                color = AppLink,
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

        } else {
            val results = state.results
            val sectionResults = state.sections
            if (results.isEmpty() && sectionResults.isEmpty()) item {
                EmptyState(
                    title = stringResource(R.string.search_nothing),
                    subtitle = stringResource(R.string.search_try_again)
                )
            }
            items(
                sectionResults,
                key = { contentKey<Screen>(id = it.first.name) },
                contentType = { Screen::class }) { (screen, title) ->
                ItemCard(
                    title,
                    onClick = { remember(query); navigate(Route(screen = screen)) })
            }
            items(
                results,
                key = {
                    contentKey<SearchResult>(
                        id = listOf(
                            it.kind.name,
                            it.id,
                            it.title,
                            it.subtitle
                        ).joinToString()
                    )
                },
                contentType = { SearchResult::class }) { result ->
                ItemCard(result.title, result.subtitle, onClick = {
                    remember(query)
                    val destination = when (result.kind) {
                        SearchResultKind.Instructor -> Route(
                            screen = Screen.Instructor,
                            id = result.id
                        )

                        SearchResultKind.Course -> Route(screen = Screen.Course, id = result.id)
                        SearchResultKind.Project -> Route(screen = Screen.Project, id = result.id)
                        SearchResultKind.Legend -> Route(screen = Screen.Legend, id = result.id)
                        SearchResultKind.Graduate -> Route(screen = Screen.Graduate, id = result.id)
                        SearchResultKind.Event -> Route(screen = Screen.Event, id = result.id)
                        SearchResultKind.StudyPlan -> Route(
                            Screen.StudyPlan,
                            arguments = RouteArguments.StudyPlan(
                                StudyProgram(code = result.id),
                                result.semester
                            )
                        )

                        SearchResultKind.AdmissionProgram -> Route(
                            Screen.Program,
                            arguments = RouteArguments.Admission(AdmissionProgramId(code = result.id))
                        )

                        SearchResultKind.Schedule -> Route(screen = Screen.Schedule)
                    }
                    navigate(destination)
                })
            }
        }
    }
}
