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

import lumberjack.sawtooth.event.LogEvent
import lumberjack.sawtooth.layout.DeprecatedLayout
import lumberjack.sawtooth.layout.Layout

class PrintAppender private constructor(

    private val layout: Layout
) : Appender {

    override fun append(event: LogEvent) {
        // Always print to standard output
        builder.clear()
        layout.writeTo(builder, event)
        println(builder)
    }

    override fun equals(other: Any?): Boolean {
        return (other as? PrintAppender)?.layout == layout
    }

    override fun hashCode(): Int = layout.hashCode()

    override fun toString(): String {
        return "PrintAppender(" +
            "layout=$layout" +
            ")"
    }

    companion object Factory {

        val DEFAULT: PrintAppender = withLayout()

        fun withLayout(
            layout: Layout = DeprecatedLayout
        ): PrintAppender = PrintAppender(layout)
    }
}

@ThreadLocal
private val builder = StringBuilder()
