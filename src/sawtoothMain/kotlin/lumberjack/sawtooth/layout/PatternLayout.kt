package lumberjack.sawtooth.layout

import lumberjack.sawtooth.component.PatternComponent
import lumberjack.sawtooth.component.PropertyComponent
import lumberjack.sawtooth.component.RawComponent
import lumberjack.sawtooth.event.LogEvent
import lumberjack.sawtooth.event.PropertyKey

class PatternLayout(val components: Array<PatternComponent>) : Layout {
    companion object {
        //language=RegExp
        const val TOKEN_MODIFIER_PATTERN = "\\{(\\w+)\\}"
        private val tokenRegex = "(?<=[^%]|^)%(?:(\\w+)|(?:\\[(\\w+)\\]))((?:$TOKEN_MODIFIER_PATTERN)*)".toRegex()
        private val tokenModifierRegex = TOKEN_MODIFIER_PATTERN.toRegex()

        fun fromPattern(pattern: String, loggerProperties: Set<PropertyKey<*>> = emptySet(), localRegistry: Map<String, PatternComponent> = emptyMap()): PatternLayout {
            var start = 0
            var tokenMatch = tokenRegex.find(pattern, start)
            val components: MutableList<PatternComponent> = ArrayList()
            val localRegistry = componentRegistry + localRegistry + loggerProperties.associate { key -> Pair(key.name, PropertyComponent(key)) }

            while (tokenMatch != null) {
                if (tokenMatch.range.first > start) {
                    components.add(RawComponent(pattern.substring(start, tokenMatch.range.first)))
                }

                val token = tokenMatch.groupValues[1]

                val component = localRegistry[token]
                if (component != null) {
                    component.tune(tokenModifierRegex.findAll(tokenMatch.groupValues[3]).map { result -> result.groupValues[1] }.toList())
                    components.add(component)
                }

                start = tokenMatch.range.last + 1
                tokenMatch = tokenRegex.find(pattern, start)
            }

            if (start < pattern.length) {
                components.add(RawComponent(pattern.substring(start)))
            }

            return PatternLayout(components.toTypedArray())
        }
    }

    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        components.forEach { it.writeTo(builder, event) }
    }
}

internal expect val componentRegistry: Map<String, PatternComponent>