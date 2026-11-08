package com.bmstu.iu3.features.instructors.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.EmptyState
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Staff
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.instructors.R
import com.bmstu.iu3.features.instructors.presentation.InstructorsUiState
import com.bmstu.iu3.features.instructors.ui.components.DeputyCard
import com.bmstu.iu3.features.instructors.ui.components.HeadCard
import com.bmstu.iu3.features.instructors.ui.components.StaffRow

@Composable
fun InstructorsScreen(
    state: InstructorsUiState,
    updateQuery: (String) -> Unit,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    val query = state.query
    var searching by rememberSaveable { mutableStateOf(false) }
    Page(
        title = stringResource(R.string.instructors_teachers),
        back = back,
        profile = { navigate(Route(screen = Screen.Profile)) },
        actions = {
            IconButton({ searching = !searching }) {
                Icon(
                    Icons.Default.Search,
                    stringResource(R.string.instructors_search)
                )
            }
        }) {
        if (searching) item {
            OutlinedTextField(
                query,
                { updateQuery(it) },
                label = { Text(stringResource(R.string.instructors_search)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        val staff = state.staff
        val head = state.head
        val deputies = state.deputies
        if (staff.isEmpty()) item { EmptyState(title = stringResource(R.string.instructors_nothing)) }
        head?.let { person ->
            item {
                HeadCard(person = person) {
                    navigate(
                        Route(
                            screen = Screen.Instructor,
                            id = person.name
                        )
                    )
                }
            }
        }
        items(
            deputies.chunked(2),
            key = { contentKey<Staff>(id = it.joinToString { person -> person.name }) },
            contentType = { Staff::class }) { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { person ->
                    DeputyCard(person = person, modifier = Modifier.weight(1f)) {
                        navigate(
                            Route(
                                screen = Screen.Instructor,
                                id = person.name
                            )
                        )
                    }
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        items(
            state.others,
            key = { contentKey<Staff>(id = it.name) }, contentType = { Staff::class }) { person ->
            StaffRow(
                person = person,
                onClick = { navigate(Route(screen = Screen.Instructor, id = person.name)) })
        }
    }
}
