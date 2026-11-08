package com.bmstu.iu3.core.notifications

import android.util.Log
import com.bmstu.iu3.core.common.di.ApplicationScope
import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.CatalogState
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.model.ReminderPlanner
import java.time.Clock
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Singleton
class ReminderCoordinator @Inject constructor(
    private val catalog: CatalogRepository,
    private val users: UserRepository,
    private val scheduler: ReminderScheduler,
    private val jobs: ReminderJobs,
    private val clock: Clock,
    @ApplicationScope private val scope: CoroutineScope,
) {
    private val started = AtomicBoolean()
    fun start() {
        if (!started.compareAndSet(false, true)) return
        jobs.schedule()
        scope.launch {
            combine(catalog.state, users.profile, users.reminders) { state, profile, settings ->
                (state as? CatalogState.Ready)?.catalog?.let {
                    ReminderPlanner.plan(
                        it,
                        profile,
                        settings,
                        now = clock.instant()
                    )
                }
            }.filterNotNull().collectLatest {
                try {
                    scheduler.replace(it)
                } catch (cancelled: CancellationException) {
                    throw cancelled
                } catch (error: Exception) {
                    Log.w(TAG, SCHEDULE_FAILURE, error)
                }
            }
        }
    }

    private companion object {
        const val TAG = "ReminderCoordinator"
        const val SCHEDULE_FAILURE = "Cannot update reminder alarms"
    }
}
