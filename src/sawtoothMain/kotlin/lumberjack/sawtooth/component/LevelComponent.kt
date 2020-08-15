package lumberjack.sawtooth.component

import lumberjack.sawtooth.event.LogEvent

object LevelComponent : PatternComponent {
    override fun writeTo(builder: StringBuilder, event: LogEvent) {
        builder.append(event.level.name)
    }
}