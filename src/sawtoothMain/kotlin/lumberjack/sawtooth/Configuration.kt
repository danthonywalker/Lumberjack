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
package lumberjack.sawtooth

import lumberjack.sawtooth.appender.Appender
import lumberjack.sawtooth.appender.RegexCompositeAppender
import lumberjack.sawtooth.event.LogEventFactory
import lumberjack.sawtooth.event.ThreadLocalLogEventFactory
import lumberjack.sawtooth.level.LevelFactory
import lumberjack.sawtooth.level.RegexLevelFactory

@Suppress("DataClassPrivateConstructor")
data class Configuration private constructor(

    internal val levelFactory: LevelFactory,

    internal val logEventFactory: LogEventFactory,

    internal val appender: Appender
) {

    companion object Factory {

        val DEFAULT: Configuration = configure()

        fun configure(
            levelFactory: LevelFactory = RegexLevelFactory.DEFAULT,
            logEventFactory: LogEventFactory = ThreadLocalLogEventFactory.DEFAULT,
            appender: Appender = RegexCompositeAppender.DEFAULT
        ): Configuration = Configuration(
            levelFactory = levelFactory,
            logEventFactory = logEventFactory,
            appender = appender
        )
    }
}

inline fun <T> withProperties(vararg properties: LogProperty<*>, block: (properties: Set<LogProperty<*>>, keys: Set<PropertyKey<*>>) -> T): T =
    block(properties.toSet(), properties.map(LogProperty<*>::key).toSet())