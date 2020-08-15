package lumberjack.sawtooth.event

class LambdaLogProperty<T : Any>(override val key: PropertyKey<T>, val supply: (LogEvent) -> T?) : LogProperty<T> {
    companion object {
        inline operator fun <reified T : Any> invoke(name: String, noinline supply: (LogEvent) -> T?): LambdaLogProperty<T> = LambdaLogProperty(PropertyKey(name, T::class), supply)
    }

    override fun value(event: LogEvent): T? = supply(event)
}