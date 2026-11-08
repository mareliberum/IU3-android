package com.bmstu.iu3.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
internal object DataInfrastructureModule {
    @Provides
    @Singleton
    fun json(): Json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
}
