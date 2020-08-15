package lumberjack.sawtooth.component

import lumberjack.MDC
import lumberjack.sawtooth.event.LogEvent

object MDCComponent : PatternComponent {
    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        event.context[MDC]?.takeIf(MDC::isNotEmpty)?.let(builder::append)
    }
}