package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent

data class RawComponent(val string: String) : PatternComponent {
    constructor(value: Any?) : this(value.toString())

    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        builder.append(string)
    }
}