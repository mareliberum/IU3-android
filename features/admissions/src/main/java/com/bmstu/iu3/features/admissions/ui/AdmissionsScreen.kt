package com.bmstu.iu3.features.admissions.ui

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.designsystem.ItemCard
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.AdmissionProgram
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.admissions.R

@Composable
fun AdmissionsScreen(
    catalog: Catalog,
    back: (() -> Unit)? = null,
    navigate: (Route) -> Unit,
) {
    Page(
        title = stringResource(R.string.admissions_admissions),
        back = back,
        profile = { navigate(Route(screen = Screen.Profile)) }) {
        items(
            catalog.programs,
            key = { contentKey<AdmissionProgram>(id = it.id.code) },
            contentType = { AdmissionProgram::class }) { program ->
            ItemCard(
                program.shortTitle,
                program.tagline + "\n" + program.badges.joinToString(" · "),
                program.heroImage,
                onClick = {
                    navigate(
                        Route(
                            Screen.Program,
                            arguments = RouteArguments.Admission(program.id)
                        )
                    )
                })
        }
    }
}
