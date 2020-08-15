package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent
import lumberjack.sawtooth.layout.Layout

interface PatternComponent: Layout {
    override fun writeTo(builder: StringBuilder, event: LogEvent)
    fun tune(modifiers: List<String>) {}
}