package com.bmstu.iu3.app

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role as AccessibilityRole
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.bmstu.iu3.R
import com.bmstu.iu3.core.designsystem.AppLink
import com.bmstu.iu3.core.designsystem.AppSelectedTab
import com.bmstu.iu3.core.designsystem.AppSeparator
import com.bmstu.iu3.core.designsystem.AppSurface
import com.bmstu.iu3.core.designsystem.AppText
import com.bmstu.iu3.core.designsystem.LocalDarkTheme
import com.bmstu.iu3.core.designsystem.LocalNavigationInset
import com.bmstu.iu3.core.model.LinkScheme
import com.bmstu.iu3.core.model.Profile
import com.bmstu.iu3.core.model.Role
import com.bmstu.iu3.core.navigation.Route
import com.bmstu.iu3.core.navigation.RouteArguments
import com.bmstu.iu3.core.navigation.Screen
import com.bmstu.iu3.features.admissions.navigation.AdmissionsRoute
import com.bmstu.iu3.features.alumni.ui.AlumniScreen
import com.bmstu.iu3.features.courses.navigation.CoursesRoute
import com.bmstu.iu3.features.courses.navigation.StudyPlanRoute
import com.bmstu.iu3.features.department.navigation.DepartmentRoute
import com.bmstu.iu3.features.home.navigation.HomeRoute
import com.bmstu.iu3.features.honor.navigation.HonorRoute
import com.bmstu.iu3.features.honor.navigation.RecommendationRoute
import com.bmstu.iu3.features.instructors.navigation.InstructorsRoute
import com.bmstu.iu3.features.profile.navigation.ProfileRoute
import com.bmstu.iu3.features.schedule.navigation.ScheduleRoute
import com.bmstu.iu3.features.search.navigation.SearchRoute

@Composable
internal fun MainNavigation(
    profile: Profile,
    contactEmail: String,
    foreground: () -> Unit,
) {
    val context = LocalContext.current
    var selected by rememberSaveable { mutableStateOf(Screen.Home) }
    var searchFocus by rememberSaveable { mutableIntStateOf(0) }
    var failedUri by rememberSaveable { mutableStateOf("") }
    val links = remember(context) { ExternalLinkHandler(context = context) { failedUri = it } }
    val open: (String) -> Unit = links::open
    val share: (String) -> Unit = links::share
    val mail: (String, String) -> Unit =
        { subject, body -> links.email(listOf(contactEmail), subject, body) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { foreground() }
    val roots = listOf(
        Screen.Home,
        Screen.Schedule,
        Screen.Department,
        Screen.Search,
        Screen.Admissions,
        Screen.Alumni
    )
    val stacks =
        roots.associateWith { screen -> key(screen) { rememberNavBackStack(Route(screen = screen)) } }
    val current = selected
    val stack = stacks.getValue(current)
    val selectedScreen by rememberUpdatedState(current)
    val latestStacks by rememberUpdatedState(stacks)
    val navigate: (Route) -> Unit = remember {
        { route ->
            val target = latestStacks.getValue(selectedScreen)
            val last = target.lastOrNull() as? Route
            if (last == null || last.screen != route.screen || last.id != route.id || last.arguments != route.arguments) target.add(
                route
            )
        }
    }
    val back: () -> Unit = remember {
        {
            val target = latestStacks.getValue(selectedScreen)
            if (target.size > 1) target.removeLastOrNull() else selected = Screen.Home
        }
    }
    val render: @Composable (Route) -> Unit = { route ->
        when (route.screen) {
            Screen.Home, Screen.Events, Screen.Event, Screen.NewsList, Screen.News, Screen.Projects, Screen.Project -> HomeRoute(
                route = route,
                back = back,
                navigate = navigate,
                open = open,
                share = share
            )

            Screen.Schedule -> ScheduleRoute(
                route = route,
                back = back,
                navigate = navigate,
                open = open,
                share = share
            )

            Screen.Department, Screen.Contacts, Screen.Achievements -> DepartmentRoute(
                route = route,
                back = back,
                navigate = navigate,
                open = open,
                share = share
            )

            Screen.Search -> SearchRoute(focusRequest = searchFocus, navigate = navigate)
            Screen.Admissions, Screen.Program -> AdmissionsRoute(
                route = route,
                back = back,
                navigate = navigate,
                open = open,
                share = share,
                parentBack = if (stack.size > 1) back else null
            )

            Screen.Alumni -> AlumniScreen(navigate = navigate, open = open, mail = mail)
            Screen.Instructors, Screen.Instructor -> InstructorsRoute(
                route = route,
                back = back,
                navigate = navigate,
                open = open,
                share = share
            )

            Screen.Stacks, Screen.Courses, Screen.Course -> CoursesRoute(
                route = route,
                back = back,
                navigate = navigate,
                open = open,
                share = share
            )

            Screen.StudyPlan -> StudyPlanRoute(
                arguments = (route.arguments as? RouteArguments.StudyPlan)
                    ?: RouteArguments.StudyPlan(), back = back, navigate = navigate
            )

            Screen.Honor, Screen.HonorList, Screen.Legend, Screen.Graduate -> HonorRoute(
                route = route,
                back = back,
                navigate = navigate,
                open = open,
                share = share
            )

            Screen.Recommendation -> RecommendationRoute(back = back, send = links::email)
            Screen.Profile, Screen.Notifications -> ProfileRoute(
                route = route,
                back = back,
                navigate = navigate
            )
        }
    }

    // Navigation 3 remembers entries by key; their content must read current data and callbacks.
    val latestRender by rememberUpdatedState(render)
    val provider: (NavKey) -> NavEntry<NavKey> = remember {
        { key -> NavEntry(key) { latestRender(key as Route) } }
    }
    val decorated = stacks.mapValues { (_, backStack) ->
        val decorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
            rememberViewModelStoreNavEntryDecorator<NavKey>()
        )
        rememberDecoratedNavEntries(backStack, decorators, provider)
    }
    val second = when (profile.role) {
        Role.Applicant -> Screen.Admissions; Role.Graduate -> Screen.Alumni; else -> Screen.Schedule
    }
    val tabs = listOf(Screen.Home, second, Screen.Department, Screen.Search)
    val labels = mapOf(
        Screen.Home to stringResource(R.string.app_home),
        Screen.Schedule to stringResource(R.string.app_schedule),
        Screen.Department to stringResource(R.string.app_department),
        Screen.Search to stringResource(R.string.app_search),
        Screen.Admissions to stringResource(R.string.app_directions),
        Screen.Alumni to stringResource(R.string.app_alumni)
    )

    val blueHeader = profile.role in listOf(
        Role.Student,
        Role.Teacher,
        null
    ) && (stack.lastOrNull() as? Route)?.screen in listOf(
        Screen.Home,
        Screen.Schedule,
        Screen.Department
    )
    val darkTheme = LocalDarkTheme.current
    SideEffect {
        (context as? ComponentActivity)?.let { activity ->
            activity.enableEdgeToEdge(
                statusBarStyle = if (blueHeader || darkTheme) SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                else SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT
                ),
                navigationBarStyle = if (darkTheme) SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                else SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT
                )
            )
            activity.window.isNavigationBarContrastEnforced = false
        }
    }
    val entries =
        if (current == Screen.Home) decorated.getValue(Screen.Home) else decorated.getValue(Screen.Home) + decorated.getValue(
            current
        )
    val selectTab: (Screen) -> Unit = { screen ->
        val target = stacks.getValue(screen)
        while (target.size > 1) target.removeLastOrNull()
        if (current == screen && screen == Screen.Search) searchFocus++
        selected = screen
    }
    BoxWithConstraints(Modifier.fillMaxSize()) {
        val compact = maxWidth < 600.dp || maxHeight < 480.dp
        CompositionLocalProvider(LocalNavigationInset provides if (compact) 82.dp else 0.dp) {
            if (compact) Box(Modifier.fillMaxSize()) {
                NavDisplay(entries = entries, onBack = back, modifier = Modifier.fillMaxSize())
                val keyboardVisible =
                    WindowInsets.ime.getBottom(androidx.compose.ui.platform.LocalDensity.current) > 0
                if (!keyboardVisible) Surface(
                    modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(42.dp),
                    color = AppSurface.copy(alpha = .96f),
                    shadowElevation = 1.dp,
                    border = BorderStroke(.5.dp, AppSeparator)
                ) {
                    Row(Modifier.fillMaxWidth().padding(4.dp)) {
                        tabs.forEach { screen ->
                            val active = current == screen
                            Surface(
                                onClick = { selectTab(screen) },
                                modifier = Modifier.weight(1f).height(54.dp).semantics {
                                    role = AccessibilityRole.Tab; this.selected = active
                                },
                                shape = RoundedCornerShape(36.dp),
                                color = if (active) AppSelectedTab else Color.Transparent
                            ) {
                                Column(
                                    Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        tabIcon(screen = screen),
                                        null,
                                        modifier = Modifier.size(26.dp),
                                        tint = if (active) AppLink else AppText
                                    )
                                    Text(
                                        labels.getValue(screen),
                                        fontSize = 10.sp,
                                        fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
                                        color = if (active) AppLink else AppText,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            } else NavigationSuiteScaffold(navigationSuiteItems = {
                tabs.forEach { screen ->
                    item(
                        selected = current == screen,
                        onClick = { selectTab(screen) },
                        icon = { Icon(tabIcon(screen = screen), null) },
                        label = { Text(labels.getValue(screen)) })
                }
            }) { NavDisplay(entries = entries, onBack = back, modifier = Modifier.fillMaxSize()) }
        }
    }
    if (failedUri.isNotBlank()) {
        val mailFailure = LinkScheme.from(Uri.parse(failedUri).scheme) == LinkScheme.Mail
        val copyValue = if (mailFailure) Uri.decode(
            failedUri.substringAfter(LinkScheme.Mail.prefix).substringBefore('?')
        ) else failedUri
        AlertDialog(
            onDismissRequest = { failedUri = "" },
            title = { Text(stringResource(if (mailFailure) R.string.app_mail_error else R.string.app_no_app)) },
            text = { Text(copyValue) }, confirmButton = {
                TextButton({
                    val clipboard =
                        context.getSystemService(android.content.ClipboardManager::class.java)
                    clipboard.setPrimaryClip(
                        android.content.ClipData.newPlainText(
                            "",
                            copyValue
                        )
                    ); failedUri = ""
                }) { Text(stringResource(if (mailFailure) R.string.app_copy_addresses else R.string.app_copy)) }
            },
            dismissButton = {
                TextButton({
                    failedUri = ""
                }) { Text(stringResource(R.string.app_close)) }
            })
    }

}
