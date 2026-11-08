package com.bmstu.iu3.features.onboarding.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.contains
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.bmstu.iu3.core.designsystem.AppCard
import com.bmstu.iu3.core.designsystem.AppGray
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.FacultyImage
import com.bmstu.iu3.core.designsystem.contentKey
import com.bmstu.iu3.core.model.Catalog
import com.bmstu.iu3.core.model.ImageAssets
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.navigation.OnboardingStep
import com.bmstu.iu3.features.onboarding.R
import com.bmstu.iu3.features.onboarding.ui.components.Dots
import com.bmstu.iu3.features.onboarding.ui.components.roleTitle
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(catalog: Catalog, onComplete: (Profile) -> Unit) {
    val stack = rememberNavBackStack(OnboardingStep(index = 0))
    var role by rememberSaveable { mutableStateOf<Role?>(null) }
    var selected by rememberSaveable(role) { mutableStateOf("") }
    var query by rememberSaveable { mutableStateOf("") }
    val render: @Composable (OnboardingStep) -> Unit = { page ->
        val step = page.index
        val pager = rememberPagerState { 2 }
        val scope = rememberCoroutineScope()
        val next: () -> Unit = {
            if (step == 0) {
                if (pager.currentPage == 0) scope.launch { pager.animateScrollToPage(1) }
                else stack.add(OnboardingStep(index = 2))
            } else if (step == 2 && role in listOf(Role.Student, Role.Teacher)) stack.add(
                OnboardingStep(index = 3)
            )
            else onComplete(
                Profile(
                    completed = true,
                    role = role,
                    group = selected.takeIf { role == Role.Student }.orEmpty(),
                    teacher = selected.takeIf { role == Role.Teacher }.orEmpty()
                )
            )
        }
        Scaffold(contentWindowInsets = WindowInsets.safeDrawing, bottomBar = {
            Surface {
                Column(Modifier.navigationBarsPadding().imePadding().padding(16.dp)) {
                    Button(
                        dropUnlessResumed(block = next),
                        enabled = when (step) {
                            0 -> true; 2 -> role != null; else -> selected.isNotBlank()
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().height(52.dp)
                    ) { Text(stringResource(R.string.onboarding_next)) }
                }
            }
        }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
                Dots(index = if (step == 0) pager.currentPage else step)
                if (step == 0) HorizontalPager(pager, modifier = Modifier.weight(1f)) { index ->
                    Column(
                        Modifier.fillMaxWidth().padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        FacultyImage(
                            if (index == 0) ImageAssets.ONBOARDING_WELCOME else ImageAssets.ONBOARDING_SCHEDULE,
                            Modifier.fillMaxWidth().weight(1f),
                            crop = false
                        )
                        Text(
                            if (index == 0) stringResource(R.string.onboarding_welcome) else stringResource(
                                R.string.onboarding_schedule_intro
                            ),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppLink
                        )
                        Text(
                            if (index == 0) stringResource(R.string.onboarding_welcome_text_715fc)
                            else stringResource(R.string.onboarding_schedule_text_28048)
                        )
                    }
                } else if (step == 2) {
                    LazyColumn(
                        Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                stringResource(R.string.onboarding_your_role),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                stringResource(R.string.onboarding_role_description),
                                color = AppGray
                            )
                        }
                        items(
                            listOf(
                                listOf(Role.Applicant, Role.Graduate),
                                listOf(Role.Teacher, Role.Student)
                            ),
                            key = { contentKey<Role>(id = it.joinToString { role -> role.name }) },
                            contentType = { Role::class }) { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                row.forEach { item ->
                                    Card(
                                        onClick = dropUnlessResumed { role = item },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(20.dp),
                                        border = if (role == item) BorderStroke(
                                            2.dp,
                                            AppLink
                                        ) else null,
                                        colors = CardDefaults.cardColors(containerColor = AppCard)
                                    ) {
                                        Column(
                                            Modifier.padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            FacultyImage(
                                                ImageAssets.role(item),
                                                Modifier.fillMaxWidth().height(85.dp),
                                                crop = false
                                            )
                                            Text(
                                                roleTitle(role = item),
                                                modifier = Modifier.padding(top = 12.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    val title =
                        if (role == Role.Student) stringResource(R.string.onboarding_your_group) else stringResource(
                            R.string.onboarding_your_name
                        )
                    Text(
                        title,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    OutlinedTextField(
                        query,
                        { query = it },
                        label = { Text(title) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    )
                    val options =
                        if (role == Role.Student) catalog.timetable.groups.keys.sorted() else catalog.staff.map { it.name }
                            .sorted()
                    LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp)) {
                        items(
                            options.filter { it.contains(query, true) },
                            key = { contentKey<String>(id = it) },
                            contentType = { String::class }) { option ->
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected == option, onClick = { selected = option })
                                TextButton(onClick = { selected = option }) { Text(option) }
                            }
                        }
                    }
                }
            }
        }
    }
    val latestRender by rememberUpdatedState(render)
    NavDisplay(
        backStack = stack, onBack = { if (stack.size > 1) stack.removeLastOrNull() },
        entryProvider = { key -> NavEntry(key) { latestRender(key as OnboardingStep) } })
}
