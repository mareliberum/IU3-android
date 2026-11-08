package com.bmstu.iu3.core.data.di

import com.bmstu.iu3.core.data.api.CatalogRepository
import com.bmstu.iu3.core.data.api.UserRepository
import com.bmstu.iu3.core.data.repository.DefaultCatalogRepository
import com.bmstu.iu3.core.data.repository.DefaultUserRepository
import com.bmstu.iu3.core.data.source.CatalogDataSource
import com.bmstu.iu3.core.data.source.StubCatalogDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Binds
    abstract fun catalog(implementation: DefaultCatalogRepository): CatalogRepository

    @Binds
    abstract fun users(implementation: DefaultUserRepository): UserRepository

    @Binds
    abstract fun source(implementation: StubCatalogDataSource): CatalogDataSource
}
