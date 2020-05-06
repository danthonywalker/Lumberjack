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

import org.apache.logging.log4j.MarkerManager
import java.util.concurrent.ConcurrentHashMap

typealias Log4JMarker = org.apache.logging.log4j.Marker

actual class Marker private constructor(

    val log4JMarker: Log4JMarker
) {

    actual val name: String
        get() = log4JMarker.name

    actual val parents: Map<String, Marker>
        // Implementation creates temporary `Pair` objects vs. optimal none
        get() = log4JMarker.parents.associate { it.name to fromMarker(it) }

    override fun toString(): String = log4JMarker.toString()

    override fun equals(other: Any?): Boolean {
        return (other as? Marker)?.log4JMarker == log4JMarker
    }

    override fun hashCode(): Int = log4JMarker.hashCode()

    actual companion object Factory {

        private val markers = ConcurrentHashMap<Log4JMarker, Marker>()

        actual fun fromName(name: String, parents: Set<Marker>): Marker {
            return markers.computeIfAbsent(MarkerManager.getMarker(name)) {
                // Implementation creates 3 `parents` copies vs. optimal 2
                val log4JMarkerParents = parents.map(Marker::log4JMarker)
                it.setParents(*log4JMarkerParents.toTypedArray())
                Marker(it)
            }
        }

        fun fromMarker(marker: Log4JMarker): Marker = markers.computeIfAbsent(marker, ::Marker)
    }
}
