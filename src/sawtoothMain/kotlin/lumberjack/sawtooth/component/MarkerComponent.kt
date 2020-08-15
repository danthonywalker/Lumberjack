package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent

object MarkerComponent : PatternComponent {
    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        event.marker?.run { builder.append(name) }
    }
}