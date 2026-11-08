package com.bmstu.iu3.core.data.source

import com.bmstu.iu3.core.model.Catalog

internal interface CatalogDataSource {
    suspend fun load(): Catalog
}
