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

import kotlinx.coroutines.ThreadContextElement
import org.apache.logging.log4j.ThreadContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

actual class MDC private constructor(

    private val context: Context
) : AbstractCoroutineContextElement(MDC),
    CoroutineContext.Element,
    Context by context,
    ThreadContextElement<Context> {

    override fun restoreThreadContext(context: CoroutineContext, oldState: Context) {
        ThreadContext.clearMap()
        ThreadContext.putAll(oldState)
    }

    override fun updateThreadContext(context: CoroutineContext): Context {
        // Get a *copy* of the context map; oldState does not view changes
        val oldState = ThreadContext.getContext()
        ThreadContext.clearMap()
        ThreadContext.putAll(this)
        return oldState
    }

    override fun toString(): String = context.toString()

    override fun equals(other: Any?): Boolean = (context == other)

    override fun hashCode(): Int = context.hashCode()

    actual companion object Key : CoroutineContext.Key<MDC> {

        private val EMPTY: MDC = MDC(emptyMap())

        actual fun fromContext(context: Context): MDC {
            return when { // Avoid allocate if possible
                context.isEmpty() -> EMPTY
                context is MDC -> context
                else -> MDC(context)
            }
        }
    }
}
