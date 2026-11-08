package com.bmstu.iu3.features.home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.designsystem.CompactCard
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.FacultyEvent
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.home.R
import com.bmstu.iu3.features.home.presentation.EventsUiState
import com.bmstu.iu3.features.home.ui.format.eventDate

@Composable
fun EventsScreen(
    state: EventsUiState,
    updateQuery: (String) -> Unit,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    val query = state.query
    Page(title = stringResource(R.string.home_events), back = back) {
        item {
            OutlinedTextField(
                query,
                { updateQuery(it) },
                label = { Text(stringResource(R.string.home_search)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        val events = state.events
        if (events.isEmpty()) item { EmptyState(title = stringResource(R.string.home_no_events)) }
        items(
            events,
            key = { contentKey<FacultyEvent>(id = it.id) },
            contentType = { FacultyEvent::class }) { event ->
            CompactCard(
                title = event.title,
                subtitle = eventDate(event = event),
                image = event.image
            ) { navigate(Route(screen = Screen.Event, id = event.id)) }
        }
    }
}
