package lumberjack.sawtooth.layout

import lumberjack.sawtooth.component.*
import kotlin.native.concurrent.AtomicReference

internal val _componentRegistry = AtomicReference(mapOf(
    "msg" to MessageComponent,
    "level" to LevelComponent,
    "logger" to LoggerComponent,
    "marker" to MarkerComponent,
    "mdc" to MDCComponent,
    "cause" to CauseComponent
))

internal actual val componentRegistry: Map<String, PatternComponent>
    get() = _componentRegistry.value