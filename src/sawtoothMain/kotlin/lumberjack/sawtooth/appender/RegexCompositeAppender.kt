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

expect class RegexCompositeAppender : Appender {

    companion object Factory {

        internal val DEFAULT_APPENDER: Appender

        val DEFAULT: RegexCompositeAppender

        fun configure(
            defaultAppender: Appender = DEFAULT_APPENDER,
            regexAppenders: List<RegexAppender> = emptyList()
        ): RegexCompositeAppender
    }
}

fun RegexCompositeAppender.Factory.configure(
    defaultAppender: Appender = DEFAULT_APPENDER,
    vararg regexAppenders: RegexAppender
): RegexCompositeAppender = configure(defaultAppender, listOf(*regexAppenders))
