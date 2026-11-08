package com.bmstu.iu3.features.instructors.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.features.instructors.R
import com.bmstu.iu3.features.instructors.domain.WebsiteKind

@Composable
internal fun websiteTitle(url: String): String = stringResource(
    when (WebsiteKind.identify(url)) {
        WebsiteKind.Telegram -> R.string.instructors_telegram
        WebsiteKind.Vk -> R.string.instructors_vk
        WebsiteKind.Github -> R.string.instructors_github
        WebsiteKind.Youtube -> R.string.instructors_youtube
        WebsiteKind.ResearchGate -> R.string.instructors_researchgate
        WebsiteKind.Scholar -> R.string.instructors_google_scholar
        else -> R.string.instructors_website
    }
)
