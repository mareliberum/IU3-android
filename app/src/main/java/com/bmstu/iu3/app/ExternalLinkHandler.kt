package com.bmstu.iu3.app

import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bmstu.iu3.core.navigation.external.MailDraft
import com.bmstu.iu3.core.navigation.external.ExternalUris
import com.bmstu.iu3.core.model.LinkScheme

internal class ExternalLinkHandler(
    private val context: Context,
    private val unhandled: (String) -> Unit,
) {
    fun open(value: String) {
        if (value.isBlank()) return
        val uri = Uri.parse(value)
        val action = when (LinkScheme.from(uri.scheme)) {
            LinkScheme.Https, LinkScheme.Http -> Intent.ACTION_VIEW
            LinkScheme.Mail -> Intent.ACTION_SENDTO
            LinkScheme.Phone -> Intent.ACTION_DIAL
            else -> return
        }
        runCatching { context.startActivity(Intent(action, uri)) }.onFailure { unhandled(value) }
    }

    fun share(value: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = ClipDescription.MIMETYPE_TEXT_PLAIN
            putExtra(Intent.EXTRA_TEXT, value)
        }
        runCatching {
            context.startActivity(
                Intent.createChooser(
                    intent,
                    null
                )
            )
        }.onFailure { unhandled(value) }
    }

    fun email(
        addresses: List<String>,
        subject: String,
        body: String,
        attachment: String? = null,
    ) {
        val fallback = ExternalUris.email(addresses, subject, body)
        if (attachment == null) {
            open(fallback); return
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = EMAIL_MIME_TYPE
            putExtra(Intent.EXTRA_EMAIL, addresses.toTypedArray())
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            putExtra(Intent.EXTRA_STREAM, Uri.parse(attachment))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        runCatching { context.startActivity(Intent.createChooser(intent, null)) }.onFailure {
            open(
                fallback
            )
        }
    }

    fun email(draft: MailDraft) =
        email(draft.recipients, draft.subject, draft.body, draft.attachment)

    private companion object {
        const val EMAIL_MIME_TYPE = "message/rfc822"
    }
}
