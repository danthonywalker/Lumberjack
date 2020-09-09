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
package lumberjack.message

import org.apache.logging.log4j.message.ReusableObjectMessage

actual object ThreadLocalMutableObjectMessage : MutableObjectMessage {

    @Suppress("ObjectPropertyName")
    private val _log4JMessage = ThreadLocal.withInitial(::ReusableObjectMessage)

    override val log4JMessage: Log4JMessage
        get() = _log4JMessage.get()

    override var message: Any?
        get() = _log4JMessage.get().parameter
        set(value) = _log4JMessage.get().set(value)

    override fun writeTo(builder: StringBuilder): Unit = _log4JMessage.get().formatTo(builder)

    override fun toString(): String = message.toString()
}
