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
@file:JvmName("MarkerJVM")

package lumberjack

import kotlin.jvm.JvmName

expect class Marker {

    val name: String

    val parents: Map<String, Marker>

    companion object {

        fun getMarker(name: String, parents: Set<Marker> = emptySet()): Marker
    }
}

fun Marker.Companion.getMarker(name: String, vararg parents: Marker): Marker = getMarker(name, setOf(*parents))
