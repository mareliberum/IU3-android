package com.bmstu.iu3.core.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.bmstu.iu3.core.model.AcademicTime
import java.time.ZonedDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun rememberMoscowTime(): ZonedDateTime {
    val owner = LocalLifecycleOwner.current
    val zone = AcademicTime.Zone
    var now by remember { mutableStateOf(ZonedDateTime.now(zone)) }
    LaunchedEffect(owner) {
        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (isActive) {
                now = ZonedDateTime.now(zone)
                delay(30_000)
            }
        }
    }
    return now
}
