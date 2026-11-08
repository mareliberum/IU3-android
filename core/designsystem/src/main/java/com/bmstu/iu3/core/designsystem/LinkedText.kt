package com.bmstu.iu3.core.designsystem

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink

@Composable
fun LinkedText(text: String, open: (String) -> Unit) {
    val linkColor = AppLink
    val annotated = buildAnnotatedString {
        var start = 0
        webLinkPattern.findAll(text).forEach { match ->
            append(text.substring(start, match.range.first))
            val raw = match.value
            val url = raw.trimEnd('.', ',', ')', ';', '!', '?')
            withLink(
                LinkAnnotation.Url(
                    url,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = linkColor,
                            textDecoration = TextDecoration.Underline
                        )
                    ),
                    linkInteractionListener = { open(url) })
            ) { append(url) }
            append(raw.substring(url.length))
            start = match.range.last + 1
        }
        append(text.substring(start))
    }
    Text(annotated)
}

@Composable
fun HtmlText(html: String, open: (String) -> Unit) {
    Text(
        AnnotatedString.fromHtml(
            html,
            linkStyles = TextLinkStyles(
                style = SpanStyle(
                    color = AppLink,
                    textDecoration = TextDecoration.Underline
                )
            ),
            linkInteractionListener = { link -> (link as? LinkAnnotation.Url)?.url?.let(open) })
    )
}

private val webLinkPattern = Regex("https?://[^\\s<>]+")
