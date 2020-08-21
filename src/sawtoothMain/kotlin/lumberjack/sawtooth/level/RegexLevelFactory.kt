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
package lumberjack.sawtooth.level

import lumberjack.Level
import lumberjack.Logger
import lumberjack.internal.ConcurrentMap

class RegexLevelFactory private constructor(

    private val defaultLevel: Level,

    private val regexLevels: List<RegexLevel>
) : LevelFactory {

    private val cache = ConcurrentMap<Logger, Level>()

    override fun fromLogger(logger: Logger): Level = cache.getOrUpdate(logger) {
        regexLevels.firstOrNull { it.regex.matches(logger.name) }?.level ?: defaultLevel
    }

    override fun toString(): String {
        return "RegexLevelFactory(" +
            "defaultLevel=$defaultLevel" +
            ", regexLevels=$regexLevels" +
            ")"
    }

    companion object Factory {

        val DEFAULT: RegexLevelFactory = configure()

        fun configure(
            defaultLevel: Level = Level.Debug,
            regexLevels: List<RegexLevel> = emptyList()
        ): RegexLevelFactory = RegexLevelFactory(
            defaultLevel = defaultLevel,
            regexLevels = regexLevels
        )
    }
}

fun RegexLevelFactory.Factory.configure(
    defaultLevel: Level = Level.Debug,
    vararg regexLevels: RegexLevel
): RegexLevelFactory = configure(defaultLevel, listOf(*regexLevels))
