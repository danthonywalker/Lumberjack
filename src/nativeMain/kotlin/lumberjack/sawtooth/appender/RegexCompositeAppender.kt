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
package lumberjack.sawtooth.appender

import lumberjack.Logger
import lumberjack.internal.getOrPut
import lumberjack.sawtooth.event.LogEvent
import kotlin.native.concurrent.AtomicReference

actual class RegexCompositeAppender private constructor(

    private val defaultAppender: Appender,

    private val regexAppenders: List<RegexAppender>
) : Appender {

    private val cache = AtomicReference(emptyMap<Logger, Appender>())

    override fun append(event: LogEvent) {
        val logger = event.logger

        cache.getOrPut(logger) {
            regexAppenders.firstOrNull { it.regex.matches(logger.name) }?.appender ?: defaultAppender
        }.append(event)
    }

    override fun toString(): String {
        return "RegexCompositeAppender(" +
            "defaultAppender=$defaultAppender" +
            ", regexAppenders=$regexAppenders" +
            ")"
    }

    actual companion object Factory {

        internal actual val DEFAULT_APPENDER: Appender = PrintAppender.DEFAULT

        actual val DEFAULT: RegexCompositeAppender = configure()

        actual fun configure(
            defaultAppender: Appender,
            regexAppenders: List<RegexAppender>
        ): RegexCompositeAppender = RegexCompositeAppender(
            defaultAppender = defaultAppender,
            regexAppenders = regexAppenders
        )
    }
}
