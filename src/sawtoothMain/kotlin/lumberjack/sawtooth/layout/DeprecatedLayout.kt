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
package lumberjack.sawtooth.layout

import lumberjack.MDC
import lumberjack.sawtooth.event.LogEvent

@Deprecated(
    """
    This class was designed to be a quick, temporary, default Layout implementation for Appender instances. It is
    intended for this class to be removed and replaced with a more customizable, permanent, default alternative.
    """
)
object DeprecatedLayout : Layout {

    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        builder.append(event.level.name)
        builder.append(' ')
        builder.append(event.logger.name)
        builder.append(' ')

        val marker = event.marker
        if (marker != null) {
            builder.append(marker.name)
            builder.append(' ')
        }

        event.message.writeTo(builder)

        val context = event.context[MDC]
        if (!context.isNullOrEmpty()) {
            builder.append(' ')
            builder.append(context)
        }

        var cause = event.cause
        while (cause != null) {
            builder.append(
                """

                Caused by: 
                """.trimIndent()
            )

            builder.append(cause::class.simpleName)
            builder.append(" (")
            builder.append(cause.message)
            builder.append(')')

            cause = cause.cause
        }
    }
}
