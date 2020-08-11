# Lumberjack

Kotlin Coroutine Multiplatform Logging Library

## 🏃 Quick Example

One of the main benefits of Lumberjack is its ability to automatically log *context* on any Kotlin platform using
coroutines. Lumberjack can log inside a suspended function to automatically derive a `CoroutineContext` or in a
non-suspended function and manually providing a `CoroutineContext`.

```kotlin
class Example {

  fun example() {
    logger.infoc { "Hello World!" }
  }

  suspend fun suspendExample() {
    logger.info { "Hello World!" }
  }

    suspend fun contextExample() {
        val context = setOf(
            "key" to "value",
            "foo" to "bar"
        )

        val newContext = MDC() + context
        withContext(MDC(newContext)) {
        }
    }

  companion object {

    private val logger = Logger.fromKClass(Example::class)
  }
}
```

## 📦 Installation

Repository and package coordinates can be found in our [release notes](https://github.com/NeonTech/Lumberjack/releases)!