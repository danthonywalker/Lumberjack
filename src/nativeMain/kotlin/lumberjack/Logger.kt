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

import lumberjack.internal.getOrPut
import lumberjack.message.Message
import lumberjack.sawtooth.Configuration
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.freeze
import kotlin.native.concurrent.isFrozen
import kotlin.reflect.KClass

actual class Logger private constructor(

    actual val name: String
) {

    actual val level: Level
        get() = configuration.levelFactory.fromLogger(this)

    actual fun logc(
        level: Level,
        message: Message,
        marker: Marker?,
        cause: Throwable?,
        context: CoroutineContext
    ) {
        if ((level > Level.None) && (this.level >= level)) {
            val event = configuration.logEventFactory.fromLogging(
                logger = this,
                level = level,
                message = message,
                marker = marker,
                cause = cause,
                context = context
            )

            configuration.appender.append(event)
        }
    }

    override fun toString(): String = name

    override fun equals(other: Any?): Boolean {
        return (other as? Logger)?.name == name
    }

    override fun hashCode(): Int = name.hashCode()

    actual companion object Factory {

        @Suppress("ObjectPropertyName")
        private val _configuration = AtomicReference(Configuration.DEFAULT)

        private val loggers = AtomicReference(emptyMap<String, Logger>())

        var configuration: Configuration
            get() = _configuration.value
            set(value) = value.run { _configuration.value = if (isFrozen) this else freeze() }

        actual fun fromName(name: String): Logger =
            loggers.getOrPut(name) { Logger(name) }

        actual fun fromKClass(kClass: KClass<*>): Logger = fromName(kClass.qualifiedName!!)
    }
}
