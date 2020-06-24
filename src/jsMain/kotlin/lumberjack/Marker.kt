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

actual class Marker private constructor(

    actual val name: String,

    actual val parents: Map<String, Marker>
) {

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        return (other as? Marker)?.name == name
    }

    override fun hashCode(): Int = name.hashCode()

    actual companion object Factory {

        private val markers = HashMap<String, Marker>()

        actual fun fromName(name: String, parents: Set<Marker>): Marker {
            return markers.getOrPut(name) { Marker(name, parents.associateBy(Marker::name)) }
        }
    }
}
