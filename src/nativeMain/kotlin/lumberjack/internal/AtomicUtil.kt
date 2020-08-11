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
package lumberjack.internal

import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.native.concurrent.isFrozen

internal inline fun <T> AtomicReference<T>.updateAndGet(block: (T) -> T): T {
    while (true) { // Effectively blocks the thread until a CAS is successful
        val current = value
        val update = block(current)

        if (!update.isFrozen) {
            update.freeze()
        }

        if (compareAndSet(current, update)) {
            return update
        }
    }
}

internal inline fun <K, V> AtomicReference<Map<K, V>>.getOrPut(key: K, value: () -> V): V {
    var cachedPair: Pair<K, V>? = null // Cache helps reduce allocations during contentions

    val localMap = updateAndGet {
        if (key in it) {
            return@updateAndGet it
        }

        cachedPair = cachedPair ?: (key to value())
        it + cachedPair!!
    }

    return localMap[key] ?: error(key.toString())
}
