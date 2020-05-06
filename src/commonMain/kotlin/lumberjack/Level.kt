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

expect sealed class Level : Comparable<Level> {

    val name: String

    val value: Int

    object None : Level

    object Fatal : Level

    object Error : Level

    object Warn : Level

    object Info : Level

    object Debug : Level

    object Trace : Level

    object All : Level

    class Custom : Level

    companion object Factory {

        fun fromName(name: String, defaultLevel: Level = None): Level

        fun toLevel(name: String, value: Int): Level
    }
}
