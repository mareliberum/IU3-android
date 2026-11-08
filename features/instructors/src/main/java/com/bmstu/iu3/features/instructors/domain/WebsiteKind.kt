package com.bmstu.iu3.features.instructors.domain

enum class WebsiteKind(val domain: String) {
    Telegram("t.me"), Vk("vk.com"), Github("github.com"), Youtube("youtube"),
    ResearchGate("researchgate"), Scholar("scholar.google"), LinkedIn("linkedin"), Other("");

    companion object {
        fun identify(url: String): WebsiteKind =
            entries.firstOrNull { it != Other && it.domain in url } ?: Other
    }
}
