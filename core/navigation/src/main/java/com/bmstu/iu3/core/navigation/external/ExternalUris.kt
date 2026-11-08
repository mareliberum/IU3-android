package com.bmstu.iu3.core.navigation.external

import android.net.Uri
import com.bmstu.iu3.core.model.LinkScheme

object ExternalUris {
    fun email(address: String): String = LinkScheme.Mail.prefix + address
    fun phone(number: String): String =
        LinkScheme.Phone.prefix + number.filter { it.isDigit() || it == '+' }

    fun email(
        addresses: List<String>,
        subject: String,
        body: String,
    ): String =
        email(addresses.joinToString(",")) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(
            body
        )

    fun scholar(title: String): String =
        Uri.parse(SCHOLAR_SEARCH).buildUpon().appendQueryParameter("q", title).build().toString()

    fun map(address: String): String =
        Uri.parse(MAPS).buildUpon().appendQueryParameter("text", address).build().toString()

    fun map(longitude: Double, latitude: Double): String = Uri.parse(MAPS).buildUpon()
        .appendQueryParameter("pt", "$longitude,$latitude").appendQueryParameter("z", "17")
        .appendQueryParameter("l", "map").build().toString()

    fun news(id: String): String = NEWS_PREFIX + id
    private const val SCHOLAR_SEARCH = "https://scholar.google.com/scholar"
    private const val MAPS = "https://yandex.ru/maps/"
    private const val NEWS_PREFIX = "https://vk.com/wall-5090_"
}
