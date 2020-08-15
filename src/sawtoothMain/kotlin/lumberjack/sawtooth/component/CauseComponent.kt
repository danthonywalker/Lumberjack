package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent

object CauseComponent : PatternComponent {
    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        var cause = event.cause
        while (cause != null) {
            builder.append("\nCaused by: ")

            builder.append(cause::class.simpleName)
            builder.append(" (")
            builder.append(cause.message)
            builder.append(')')

            cause = cause.cause
        }
    }
}