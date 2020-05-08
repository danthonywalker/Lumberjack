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
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.jvm.JvmName

expect class Logger {

    val name: String

    val level: Level

    fun logc(
        level: Level,
        message: Message,
        marker: Marker? = null,
        cause: Throwable? = null,
        context: CoroutineContext = EmptyCoroutineContext
    )

    companion object Factory {

        fun fromName(name: String): Logger
    }
}

inline fun Logger.logc(
    level: Level,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
) {
    if (this.level >= level) {
        val rawMessage = message() // ThreadLocalMutableStringMessage provides most efficient generic type conversion
        val typedMessage = (rawMessage as? Message) ?: ThreadLocalMutableStringMessage.message(rawMessage.toString())
        logc(level, typedMessage, marker, cause, context)
    }
}

suspend inline fun Logger.log(
    level: Level,
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = logc(level, message, marker, cause, coroutineContext)

suspend inline fun Logger.log(
    level: Level,
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = logc(level, marker, cause, coroutineContext, message)

fun Logger.nonec(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.None, message, marker, cause, context)

inline fun Logger.nonec(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.None, marker, cause, context, message)

suspend inline fun Logger.none(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.None, message, marker, cause)

suspend inline fun Logger.none(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.None, marker, cause, message)

fun Logger.fatalc(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.Fatal, message, marker, cause, context)

inline fun Logger.fatalc(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.Fatal, marker, cause, context, message)

suspend inline fun Logger.fatal(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.Fatal, message, marker, cause)

suspend inline fun Logger.fatal(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.Fatal, marker, cause, message)

fun Logger.errorc(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.Error, message, marker, cause, context)

inline fun Logger.errorc(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.Error, marker, cause, context, message)

suspend inline fun Logger.error(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.Error, message, marker, cause)

suspend inline fun Logger.error(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.Error, marker, cause, message)

fun Logger.warnc(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.Warn, message, marker, cause, context)

inline fun Logger.warnc(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.Warn, marker, cause, context, message)

suspend inline fun Logger.warn(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.Warn, message, marker, cause)

suspend inline fun Logger.warn(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.Warn, marker, cause, message)

fun Logger.infoc(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.Info, message, marker, cause, context)

inline fun Logger.infoc(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.Info, marker, cause, context, message)

suspend inline fun Logger.info(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.Info, message, marker, cause)

suspend inline fun Logger.info(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.Info, marker, cause, message)

fun Logger.debugc(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.Debug, message, marker, cause, context)

inline fun Logger.debugc(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.Debug, marker, cause, context, message)

suspend inline fun Logger.debug(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.Debug, message, marker, cause)

suspend inline fun Logger.debug(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.Debug, marker, cause, message)

fun Logger.tracec(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.Trace, message, marker, cause, context)

inline fun Logger.tracec(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.Trace, marker, cause, context, message)

suspend inline fun Logger.trace(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.Trace, message, marker, cause)

suspend inline fun Logger.trace(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.Trace, marker, cause, message)

fun Logger.allc(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext
): Unit = logc(Level.All, message, marker, cause, context)

inline fun Logger.allc(
    marker: Marker? = null,
    cause: Throwable? = null,
    context: CoroutineContext = EmptyCoroutineContext,
    message: () -> Any?
): Unit = logc(Level.All, marker, cause, context, message)

suspend inline fun Logger.all(
    message: Message,
    marker: Marker? = null,
    cause: Throwable? = null
): Unit = log(Level.All, message, marker, cause)

suspend inline fun Logger.all(
    marker: Marker? = null,
    cause: Throwable? = null,
    message: () -> Any?
): Unit = log(Level.All, marker, cause, message)
