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
@file:JvmName("LoggerJVM")

package lumberjack

import lumberjack.message.Message
import lumberjack.message.ThreadLocalMutableStringMessage
import lumberjack.message.message
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.jvm.JvmName
import kotlin.reflect.KClass

expect class Logger {

    val name: String

    val level: Level

    fun log(
        level: Level,
        context: CoroutineContext,
        message: Message,
        marker: Marker? = null,
        cause: Throwable? = null
    )

    companion object Factory {

        fun fromName(name: String): Logger
    }
}

inline fun Logger.log(
    level: Level,
    context: CoroutineContext,
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
) {
    if (this.level >= level) {
        val rawMessage = message() // ThreadLocalMutableStringMessage provides most efficient generic type conversion
        val typedMessage = (rawMessage as? Message) ?: ThreadLocalMutableStringMessage.message(rawMessage.toString())
        log(level, context, typedMessage, marker, cause)
    }
}

suspend inline fun Logger.log(
    level: Level,
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(level, coroutineContext, message, marker, cause)

suspend inline fun Logger.log(
    level: Level,
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(level, coroutineContext, marker, cause, message)
