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
package lumberjack.sawtooth.event

import lumberjack.Level
import lumberjack.Logger
import lumberjack.Marker
import lumberjack.message.Message
import lumberjack.message.ThreadLocalMutableStringMessage
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal actual object ThreadLocalLogEvent : LogEvent {

    actual override var logger: Logger = Logger.fromKClass(ThreadLocalLogEvent::class)

    actual override var level: Level = Level.None

    actual override var message: Message = ThreadLocalMutableStringMessage

    actual override var marker: Marker? = null

    actual override var cause: Throwable? = null

    actual override var context: CoroutineContext = EmptyCoroutineContext

    actual var properties: Map<PropertyKey<*>, LogProperty<*>> = emptyMap()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> property(key: PropertyKey<T>): T? {
        return properties[key]?.value(this) as? T
    }
}
