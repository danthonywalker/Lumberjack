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
package lumberjack.sawtooth.layout.pattern

object DefaultPatternComponentFactory : PatternComponentFactory {

    override fun fromToken(token: String, modifiers: List<String>): PatternComponent? {
        // https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout
        return when (token) {
            "c", "logger" -> LoggerComponent.withModifiers(modifiers)
            "ex", "exception", "throwable" -> CauseComponent.withModifiers(modifiers)
            "m", "msg", "message" -> MessageComponent.withModifiers(modifiers)
            "marker" -> MarkerComponent.withModifiers(modifiers)
            "p", "level" -> LevelComponent.withModifiers(modifiers)
            "x", "mdc", "MDC" -> MDCComponent.withModifiers(modifiers)
            else -> null
        }
    }

    override fun toString(): String = "DefaultPatternComponentFactory()"
}
