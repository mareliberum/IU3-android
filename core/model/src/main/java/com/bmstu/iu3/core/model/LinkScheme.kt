package com.bmstu.iu3.core.model

enum class LinkScheme(val code: String) {
    Https("https"), Http("http"), Mail("mailto"), Phone("tel"), File("file"), Content("content"), Package(
        "package"
    );

    val prefix: String get() = "$code:"

    companion object {
        fun from(code: String?): LinkScheme? = entries.firstOrNull { it.code == code }
    }
}
