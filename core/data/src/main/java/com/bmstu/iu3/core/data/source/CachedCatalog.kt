package com.bmstu.iu3.core.data.source

import com.bmstu.iu3.core.model.Catalog
import kotlinx.serialization.Serializable

@Serializable
internal data class CachedCatalog(val savedAt: Long, val catalog: Catalog)
