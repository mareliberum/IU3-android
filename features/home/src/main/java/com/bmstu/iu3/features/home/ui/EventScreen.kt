package com.bmstu.iu3.features.home.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.HtmlText
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.model.EventLogic
import com.bmstu.iu3.core.model.EventStatus
import com.bmstu.iu3.core.model.FacultyEvent
import com.bmstu.iu3.core.navigation.external.ExternalUris
import com.bmstu.iu3.features.home.R
import com.bmstu.iu3.features.home.ui.format.eventDetailDate

@Composable
fun EventScreen(
    event: FacultyEvent,
    back: () -> Unit,
    open: (String) -> Unit,
    share: (String) -> Unit,
) {
    Page(title = stringResource(R.string.home_event_context), back = back) {
        item { FacultyImage(name = event.image, modifier = Modifier.fillMaxWidth().height(240.dp)) }
        item { Text(event.title, style = MaterialTheme.typography.headlineMedium) }
        item {
            Text(eventDetailDate(event = event), style = MaterialTheme.typography.titleLarge)
            Text(
                when (EventLogic.status(event)) {
                    EventStatus.Ongoing -> stringResource(R.string.home_ongoing); EventStatus.Finished -> stringResource(
                    R.string.home_finished
                ); else -> stringResource(
                    R.string.home_upcoming
                )
                }, color = AppGray
            )
            Text(event.location); Text(event.organizer, color = AppGray)
        }
        item { Text(event.tags.joinToString(" · "), color = AppLink) }
        item { HtmlText(html = event.description, open = open) }
        if (EventLogic.hasPhysicalLocation(event)) item {
            OutlinedButton({
                open(
                    ExternalUris.map(event.location)
                )
            }) { Text(stringResource(R.string.home_map)) }
        }
        if (!event.registrationURL.isNullOrBlank() && EventLogic.status(event) != EventStatus.Finished) item {
            Button({ open(event.registrationURL.orEmpty()) }) { Text(stringResource(R.string.home_register)) }
        }
        item {
            OutlinedButton({ open(event.url) }) { Text(stringResource(R.string.home_event_website)) }; TextButton(
            { share(event.url) }) { Text(stringResource(R.string.home_share)) }
        }
    }
}
