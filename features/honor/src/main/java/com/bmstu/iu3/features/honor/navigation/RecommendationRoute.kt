package com.bmstu.iu3.features.honor.navigation

import androidx.compose.runtime.Composable
import com.bmstu.iu3.core.model.DepartmentLinks
import com.bmstu.iu3.core.navigation.external.MailDraft
import com.bmstu.iu3.features.honor.ui.RecommendationScreen

@Composable
fun RecommendationRoute(
    back: () -> Unit,
    send: (MailDraft) -> Unit,
) {
    RecommendationScreen(back = back) { subject, body, photo ->
        send(
            MailDraft(
                recipients = DepartmentLinks.RECOMMENDATION_EMAILS,
                subject = subject,
                body = body,
                attachment = photo
            )
        )
    }
}
