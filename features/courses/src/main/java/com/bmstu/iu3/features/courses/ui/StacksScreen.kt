package com.bmstu.iu3.features.courses.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bmstu.iu3.core.designsystem.AppBlue
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppText
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.Page
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.TechnologyStack
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.courses.R
import com.bmstu.iu3.features.courses.ui.components.stackImages
import com.bmstu.iu3.features.courses.ui.components.stackTitle

@Composable
fun StacksScreen(
    catalog: Catalog,
    back: () -> Unit,
    navigate: (Route) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(emptyList<TechnologyStack>()) }
    val expandLabel = stringResource(R.string.courses_expanded)
    val collapseLabel = stringResource(R.string.courses_collapsed)
    Page(
        title = stringResource(R.string.courses_stacks),
        back = back,
        profile = { navigate(Route(screen = Screen.Profile)) }) {
        item {
            Card(
                onClick = { navigate(Route(screen = Screen.StudyPlan)) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = AppBlue),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        color = Color.White.copy(alpha = .18f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp).size(24.dp)
                        )
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            stringResource(R.string.courses_plan),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White
                        )
                        Text(
                            stringResource(R.string.courses_plan_entry),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = .85f)
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        null,
                        tint = Color.White.copy(alpha = .8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        items(
            catalog.technologies.entries.toList(),
            key = { contentKey<TechnologyStack>(id = it.key.name) },
            contentType = { TechnologyStack::class }) { (stack, technologies) ->
            val isExpanded = stack in expanded
            val rotation by animateFloatAsState(
                if (isExpanded) 180f else 0f,
                label = STACK_CHEVRON_ANIMATION
            )
            Column(Modifier.padding(top = 4.dp)) {
                Card(
                    onClick = { expanded = if (isExpanded) expanded - stack else expanded + stack },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    colors = CardDefaults.cardColors(containerColor = AppCard),
                    modifier = Modifier.fillMaxWidth().semantics {
                        stateDescription = if (isExpanded) expandLabel else collapseLabel
                    }) {
                    Row(
                        Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        FacultyImage(
                            stackImages[stack],
                            Modifier.size(56.dp),
                            crop = false,
                            background = Color.Transparent
                        )
                        Text(
                            stackTitle(stack = stack),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f),
                            color = AppText
                        )
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            null,
                            modifier = Modifier.size(24.dp).graphicsLayer { rotationZ = rotation },
                            tint = AppText
                        )
                    }
                }
                AnimatedVisibility(isExpanded) {
                    Column(
                        Modifier.padding(top = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        TextButton({
                            navigate(
                                Route(Screen.Courses, arguments = RouteArguments.Courses(stack))
                            )
                        }) { Text(stringResource(R.string.courses_all_courses)) }
                        technologies.chunked(2).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                row.forEach { tech ->
                                    Card(
                                        onClick = {
                                            navigate(
                                                Route(
                                                    Screen.Courses,
                                                    arguments = RouteArguments.Courses(
                                                        stack,
                                                        tech.title
                                                    )
                                                )
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = CardDefaults.cardColors(containerColor = AppCard)
                                    ) {
                                        Column(
                                            Modifier.padding(14.dp),
                                            verticalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            FacultyImage(
                                                tech.image,
                                                Modifier.fillMaxWidth().height(144.dp),
                                                crop = false
                                            )
                                            Text(
                                                tech.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                                if (row.size == 1) Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val STACK_CHEVRON_ANIMATION = "stackChevron"
