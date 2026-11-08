package com.bmstu.iu3.core.designsystem

import android.net.Uri
import com.bmstu.iu3.core.model.LinkScheme

internal object FacultyImageSource {
    private const val ASSET_ROOT = "file:///android_asset/images/"
    fun resolve(name: String?, images: Map<String, String>): String? =
        when (LinkScheme.from(name?.let { Uri.parse(it).scheme })) {
            LinkScheme.Https, LinkScheme.File, LinkScheme.Content -> name
            else -> images[name]?.let { ASSET_ROOT + it }
        }
}
