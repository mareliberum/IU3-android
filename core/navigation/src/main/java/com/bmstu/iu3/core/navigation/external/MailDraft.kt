package com.bmstu.iu3.core.navigation.external

data class MailDraft(
    val recipients: List<String>,
    val subject: String,
    val body: String,
    val attachment: String? = null,
)
