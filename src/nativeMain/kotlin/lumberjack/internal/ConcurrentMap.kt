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

internal actual class ConcurrentMap<K, V> actual constructor() : MutableMap<K, V> {

    private val delegate = AtomicReference(emptyMap<K, V>())

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = TODO(this::entries.name)

    override val keys: MutableSet<K>
        get() = TODO(this::keys.name)

    override val size: Int
        get() = delegate.value.size

    override val values: MutableCollection<V>
        get() = TODO(this::values.name)

    override fun clear(): Unit = delegate.run { value = emptyMap() }

    override fun containsKey(key: K): Boolean = delegate.value.containsKey(key)

    override fun containsValue(value: V): Boolean = delegate.value.containsValue(value)

    override fun get(key: K): V? = delegate.value[key]

    override fun isEmpty(): Boolean = delegate.value.isEmpty()

    override fun put(key: K, value: V): V? {
        var cachedPair: Pair<K, V>? = null

        return delegate.getAndUpdate {
            if (it[key] == value) {
                return@getAndUpdate it
            }

            cachedPair = cachedPair ?: (key to value)
            it + cachedPair!!
        }[key]
    }

    override fun putAll(from: Map<out K, V>): Unit = delegate.update { it + from }

    override fun remove(key: K): V? {
        return delegate.getAndUpdate {
            if (key !in it) {
                return@getAndUpdate it
            }

            it - key
        }[key]
    }

    actual inline fun getOrUpdate(key: K, value: () -> V): V {
        // Cache helps reduce allocations during contentions
        var cachedPair: Pair<K, V>? = null

        return delegate.updateAndGet {
            if (key in it) {
                return@updateAndGet it
            }

            cachedPair = cachedPair ?: (key to value())
            it + cachedPair!!
        }.getValue(key)
    }

    override fun equals(other: Any?): Boolean = (delegate.value == other)

    override fun hashCode(): Int = delegate.value.hashCode()

    override fun toString(): String = delegate.value.toString()
}
