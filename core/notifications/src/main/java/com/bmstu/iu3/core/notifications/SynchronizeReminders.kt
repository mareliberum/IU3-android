package com.bmstu.iu3.core.notifications

import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.ReminderPlanner
import java.time.Clock
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class SynchronizeReminders @Inject constructor(
    private val catalog: CatalogRepository,
    private val users: UserRepository,
    private val scheduler: ReminderScheduler,
    private val clock: Clock,
) {
    suspend operator fun invoke(refresh: Boolean = false): Boolean {
        if (refresh) catalog.refresh(force = true)
        val ready = catalog.state.value as? CatalogState.Ready ?: return false
        scheduler.replace(
            ReminderPlanner.plan(
                ready.catalog,
                users.profile.first(),
                users.reminders.first(),
                now = clock.instant()
            )
        )
        return true
    }
}
