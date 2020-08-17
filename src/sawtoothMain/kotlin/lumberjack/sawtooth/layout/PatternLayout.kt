/*
 * This file is part of Lumberjack.
 *
 * Lumberjack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lumberjack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Lumberjack.  If not, see <https://www.gnu.org/licenses/>.
 */
package lumberjack.sawtooth.layout

import lumberjack.sawtooth.component.*
import lumberjack.sawtooth.event.LogEvent
import lumberjack.sawtooth.event.PropertyKey

class PatternLayout(val components: List<PatternComponent>) : Layout {
    companion object Factory {
        //language=RegExp
        /** Matches an opening brace, followed by one or more word characters [0-9a-zA-Z_], followed by a closing brace */
        const val TOKEN_MODIFIER_PATTERN = "\\{(\\w+)\\}"

        /**
         * Regex is notoriously hellish, so let's run through what this does
         * This is a regex string to match a token - that is, a percent sign '%' followed by one or more word characters, optionally enclosed with brackets
         * The regex functions like so:
         * - `(?<=[^%]|^)` Requires the first character to either be anything *other* than a percent sign, *or* have it be the start of the string. Does not match that character.
         * - `%` Match a single percent sign
         * - `(?:(\w+)|(?:\[(\w+)\]))` Use a non-capturing group to select one of two options - either match one or more word characters, or match an opening bracket, one or more word characters, and then a closing bracket
         * - `((?:$TOKEN_MODIFIER_PATTERN)*)` Use a non-capturing group to match the token modifier pattern zero or more times, and capture that whole result
         */
        private val tokenRegex = "(?<=[^%]|^)%(?:(\\w+)|(?:\\[(\\w+)\\]))((?:$TOKEN_MODIFIER_PATTERN)*)".toRegex()

        /** Because regex can't capture a group multiple times, we need a separate regex to find all matches in the modifier match */
        private val tokenModifierRegex = TOKEN_MODIFIER_PATTERN.toRegex()

        internal val defaultComponentRegistry: Map<String, PatternComponentInitialiser> = mapOf(
            "m" to { MessageComponent },
            "msg" to { MessageComponent },
            "message" to { MessageComponent },
            "lvl" to { LevelComponent },
            "level" to { LevelComponent },
            "logger" to { LoggerComponent },
            "marker" to { MarkerComponent },
            "mdc" to { MDCComponent },
            "cause" to { CauseComponent }
        )

        fun fromPattern(pattern: String, loggerProperties: Set<PropertyKey<*>> = emptySet(), localRegistry: Map<String, PatternComponentInitialiser> = emptyMap()): PatternLayout {
            val components: MutableList<PatternComponent> = ArrayList()
            val modifiers: MutableList<String> = ArrayList()

            //Compile a map of initialisers from our global registry, our local registry, and the properties that have been passed in
            val registry =
                componentRegistry +
                localRegistry +
                loggerProperties.associate { key -> Pair(key.name, { _: List<String> -> PropertyComponent(key) }) }

            var start = 0
            //Find our first match in the pattern
            var tokenMatch = tokenRegex.find(pattern, start)

            //While we have a valid match
            while (tokenMatch != null) {
                //If the match is further into the string than the starting position, we have some text to write as-is
                if (tokenMatch.range.first > start) {
                    components.add(RawComponent(pattern.substring(start, tokenMatch.range.first)))
                }

                //Get the token (remember: groupValues[0] is the whole matched string)
                //If groupValues[1] is empty, it means we matched a sequence with brackets around it, so get the second group value
                val token = tokenMatch.groupValues[1].takeUnless(String::isEmpty) ?: tokenMatch.groupValues[2]

                val componentInitialiser = registry[token]
                if (componentInitialiser != null) {
                    //If we have an initialiser, invoke it and add it to the list of components
                    //The list of modifiers is obtained by finding all valid matches of tokenModifierRegex on the third group value
                    modifiers.clear()
                    components.add(componentInitialiser(tokenModifierRegex.findAll(tokenMatch.groupValues[3]).mapTo(modifiers) { result -> result.groupValues[1] }))
                }

                //Increment our starting position to the end of our match, then recurse and find another match
                start = tokenMatch.range.last + 1
                tokenMatch = tokenRegex.find(pattern, start)
            }

            //If the last position we ended at isn't the end of the pattern, we have leftover text to write as is
            if (start < pattern.length) {
                components.add(RawComponent(pattern.substring(start)))
            }

            return PatternLayout(components)
        }
    }

    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        components.forEach { it.writeTo(builder, event) }
    }
}

public expect var PatternLayout.Factory.componentRegistry: Map<String, PatternComponentInitialiser>
