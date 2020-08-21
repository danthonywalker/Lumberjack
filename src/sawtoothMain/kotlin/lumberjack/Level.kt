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

import lumberjack.internal.ConcurrentMap

actual sealed class Level(

    actual val name: String,

    actual val value: Int
) : Comparable<Level> {

    final override fun compareTo(other: Level): Int = value.compareTo(other.value)

    final override fun equals(other: Any?): Boolean {
        return (other as? Level)?.name == name
    }

    final override fun hashCode(): Int = name.hashCode()

    final override fun toString(): String = name

    actual object None : Level("OFF", 0)

    actual object Fatal : Level("FATAL", 100)

    actual object Error : Level("ERROR", 200)

    actual object Warn : Level("WARN", 300)

    actual object Info : Level("INFO", 400)

    actual object Debug : Level("DEBUG", 500)

    actual object Trace : Level("TRACE", 600)

    actual object All : Level("ALL", Int.MAX_VALUE)

    actual class Custom internal constructor(name: String, value: Int) : Level(name, value)

    actual companion object Factory {

        private val levels = ConcurrentMap<String, Level>()

        actual fun fromName(name: String, defaultLevel: Level): Level = (levels[name] ?: defaultLevel)

        actual fun toLevel(name: String, value: Int): Level {
            return levels.getOrUpdate(name) {
                when (name) {
                    None.name -> None
                    Fatal.name -> Fatal
                    Error.name -> Error
                    Warn.name -> Warn
                    Info.name -> Info
                    Debug.name -> Debug
                    Trace.name -> Trace
                    All.name -> All
                    else -> Custom(name, value)
                }
            }
        }
    }
}
