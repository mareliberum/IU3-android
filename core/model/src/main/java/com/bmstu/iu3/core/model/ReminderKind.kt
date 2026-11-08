package com.bmstu.iu3.core.model


enum class ReminderKind(val key: String) {
    Lesson("lesson"), Laboratories("labs"), DaySummary("summary"),
    Controls("controls"), NextControls("controls_next"), EventTomorrow("event_tomorrow"), EventToday(
        "event_today"
    )
}
