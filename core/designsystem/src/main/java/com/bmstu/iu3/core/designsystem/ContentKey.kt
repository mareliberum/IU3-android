package com.bmstu.iu3.core.designsystem

inline fun <reified T> contentKey(id: String): String = "${T::class.java.name}:$id"
