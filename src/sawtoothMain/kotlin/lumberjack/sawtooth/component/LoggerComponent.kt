package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent

object LoggerComponent : PatternComponent {
    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        builder.append(event.logger.name)
    }
}