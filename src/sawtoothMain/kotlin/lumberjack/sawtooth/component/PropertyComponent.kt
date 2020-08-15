package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent
import lumberjack.sawtooth.event.PropertyKey

open class PropertyComponent<T : Any>(val property: PropertyKey<T>) : PatternComponent {
    private var formatOptions: List<String> = emptyList()

    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        builder.append(event.property(property)?.let(this::format))
    }

    override fun tune(modifiers: List<String>) {
        formatOptions = modifiers
    }

    open fun format(property: T): String = property.toString()
}