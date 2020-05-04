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

import lumberjack.message.Message
import kotlin.coroutines.CoroutineContext

internal class DefaultLogger(

    override val log4JLogger: Log4JLogger
) : Logger {

    override val name: String
        get() = log4JLogger.name

    override val level: Level
        get() = Level.fromLevel(log4JLogger.level)

    override fun log(level: Level, context: CoroutineContext, message: Message, marker: Marker?, cause: Throwable?) {
        log4JLogger.log(level.log4JLevel, marker?.log4JMarker, message.log4JMessage, cause) // TODO CoroutineContext
    }

    override fun toString(): String = log4JLogger.toString()

    override fun equals(other: Any?): Boolean {
        return (other as? Logger)?.log4JLogger == log4JLogger
    }

    override fun hashCode(): Int = log4JLogger.hashCode()
}
