package com.bmstu.iu3.core.notifications

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import com.bmstu.iu3.core.common.di.IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
internal object NotificationModule {
    @Provides
    fun formatter(implementation: AndroidReminderFormatter): ReminderFormatter = implementation

    @Provides
    @Singleton
    fun scheduler(
        @ApplicationContext context: Context,
        formatter: ReminderFormatter,
        @IoDispatcher dispatcher: CoroutineDispatcher,
    ): ReminderScheduler = AndroidReminderScheduler(
        context = context,
        formatter = formatter,
        dispatcher = dispatcher
    )
}
