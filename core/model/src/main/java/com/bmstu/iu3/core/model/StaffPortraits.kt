package com.bmstu.iu3.core.model

object StaffPortraits {
    private val FEMALE_PATRONYMIC_SUFFIXES = listOf("вна", "чна")
    fun fallback(name: String): String =
        if (FEMALE_PATRONYMIC_SUFFIXES.any(name.substringAfterLast(' ')::endsWith)) ImageAssets.PLACEHOLDER_FEMALE else ImageAssets.PLACEHOLDER_MALE
}
