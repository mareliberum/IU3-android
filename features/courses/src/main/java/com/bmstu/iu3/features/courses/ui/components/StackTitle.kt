package com.bmstu.iu3.features.courses.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bmstu.iu3.core.model.TechnologyStack
import com.bmstu.iu3.features.courses.R

@Composable
internal fun stackTitle(stack: TechnologyStack): String = stringResource(
    when (stack) {
        TechnologyStack.Programming -> R.string.courses_stack_programming
        TechnologyStack.InformationTechnology -> R.string.courses_stack_it
        TechnologyStack.Networks -> R.string.courses_stack_networks
        TechnologyStack.ArtificialIntelligence -> R.string.courses_stack_ai
        TechnologyStack.Electronics -> R.string.courses_stack_electronics
    }
)
