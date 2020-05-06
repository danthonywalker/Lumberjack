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
package lumberjack

import java.util.concurrent.ConcurrentHashMap

typealias Log4JLevel = org.apache.logging.log4j.Level

actual sealed class Level(

    val log4JLevel: Log4JLevel
) : Comparable<Level> {

    actual val name: String
        get() = log4JLevel.name()

    actual val value: Int
        get() = log4JLevel.intLevel()

    final override fun compareTo(other: Level): Int = value.compareTo(other.value)

    final override fun toString(): String = log4JLevel.toString()

    final override fun equals(other: Any?): Boolean {
        return (other as? Level)?.log4JLevel == log4JLevel
    }

    final override fun hashCode(): Int = log4JLevel.hashCode()

    actual object None : Level(Log4JLevel.OFF)

    actual object Fatal : Level(Log4JLevel.FATAL)

    actual object Error : Level(Log4JLevel.ERROR)

    actual object Warn : Level(Log4JLevel.WARN)

    actual object Info : Level(Log4JLevel.INFO)

    actual object Debug : Level(Log4JLevel.DEBUG)

    actual object Trace : Level(Log4JLevel.TRACE)

    actual object All : Level(Log4JLevel.ALL)

    actual class Custom internal constructor(log4JLevel: Log4JLevel) : Level(log4JLevel)

    actual companion object Factory {

        private val levels = ConcurrentHashMap<Log4JLevel, Level>(8)

        actual fun fromName(name: String, defaultLevel: Level): Level {
            return fromLevel(Log4JLevel.toLevel(name, defaultLevel.log4JLevel))
        }

        actual fun toLevel(name: String, value: Int): Level = fromLevel(Log4JLevel.forName(name, value))

        fun fromLevel(level: Log4JLevel): Level = levels.computeIfAbsent(level) {
            when (it) { // Use `it` so JVM can optimize lambda to prevent garbage
                None.log4JLevel -> None
                Fatal.log4JLevel -> Fatal
                Error.log4JLevel -> Error
                Warn.log4JLevel -> Warn
                Info.log4JLevel -> Info
                Debug.log4JLevel -> Debug
                Trace.log4JLevel -> Trace
                All.log4JLevel -> All
                else -> Custom(it)
            }
        }
    }
}
