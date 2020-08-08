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
import kotlin.coroutines.CoroutineContext

internal expect object ThreadLocalLogEvent : LogEvent {

    override var logger: Logger

    override var level: Level

    override var message: Message

    override var marker: Marker?

    override var cause: Throwable?

    override var context: CoroutineContext

    var properties: Map<PropertyKey<*>, LogProperty<*>>
}
