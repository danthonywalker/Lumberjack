package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent

object MessageComponent : PatternComponent {
    override fun writeTo(builder: StringBuilder, event: LogEvent): Unit =
        event.message.writeTo(builder)
}