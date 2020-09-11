[![Discord](https://img.shields.io/discord/356994728614690818?label=NeonTech&color=7289DA&style=for-the-badge)](https://discord.gg/ThGXvNF)
[![Actions](https://img.shields.io/github/workflow/status/NeonTech/Lumberjack/Build?style=for-the-badge)](https://github.com/NeonTech/Lumberjack/actions)
[![Bintray](https://img.shields.io/bintray/v/neontech/maven/lumberjack?color=43a047&style=for-the-badge)](https://bintray.com/beta/#/neontech/maven/lumberjack)
[![License](https://img.shields.io/github/license/NeonTech/Lumberjack?style=for-the-badge)](https://www.gnu.org/licenses/lgpl-3.0.en.html)

# Lumberjack

Kotlin Coroutine Multiplatform Logging Library

## 💎 Benefits

* 💪 **Powerful** - Heavily inspired by [Log4J2](https://logging.apache.org/log4j/2.x/), Lumberjack enables powerful logging API concepts such as MDC (Mapped Diagnostic Context), markers, custom levels, and an abstract `Message` API to be utilized in `common` and non-JVM platforms.

* 🧰 **Configurable** - For JVM targets, Lumberjack delegates to [Log4J2 API](https://logging.apache.org/log4j/2.x/) which enables extensive customization in [runtime implementation](https://logging.apache.org/log4j/2.x/runtime-dependencies.html) including delegating to [SLF4J](http://www.slf4j.org/) or, if using `log4j-core`, powerful [configuration](https://logging.apache.org/log4j/2.x/manual/configuration.html) options.

  For non-JVM targets, Lumberjack delegates to *Sawtooth*; an in-house logging engine and configuration API. *Sawtooth* configurations can be changed during runtime and its abstract APIs allow potentially infinite customization.

* 🚀 **Efficient** - By reusing buffers and objects, utilizing caches, and inlining functions; Lumberjack operates with minimal memory overhead and contributes little to no additional pressure for the garbage collector.

* 🌌 **Universal** - Lumberjack supports *all* [Kotlin Multiplatform targets](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#supported-platforms) which makes it perfect for any Kotlin library or application for any platform.

## 📦 Installation

Lumberjack can be installed like any other Kotlin library via [Gradle](https://gradle.org/). For all proceeding examples, `$VERSION` should be replaced with the latest available version on [Bintray](https://bintray.com/neontech/maven/lumberjack). While it is not recommended to manually install dependencies, raw artifacts can also be found on Bintray.

```kotlin
repositories {
  jcenter()
}

dependencies {
  // Replace $PLATFORM with jvm, js, linuxx64, macosx64, mingwx64, etc.
  implementation("dev.neontech.lumberjack:lumberjack-$PLATFORM:$VERSION")
}
```

### Kotlin Multiplatform

For [Kotlin Mutliplatform](https://kotlinlang.org/docs/reference/multiplatform.html) projects, Lumberjack provides [Gradle Metadata](https://kotlinlang.org/docs/reference/building-mpp-with-gradle.html#metadata-publishing) to resolve individual platforms automatically.

```kotlin
repositories {
  jcenter()
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        // Implementations for jvm, js, etc. automatically resolve
        implementation("dev.neontech.lumberjack:lumberjack:$VERSION")
      }
    }
  }
}
```

Alternatively, implementations may manually be resolved with an explicit `-common` declaration in `commonMain`.

```kotlin
repositories {
  jcenter()
}

kotlin {
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("dev.neontech.lumberjack:lumberjack-common:$VERSION")
      }
    }

    val jvmMain by getting {
      dependencies {
        implementation("dev.neontech.lumberjack:lumberjack-jvm:$VERSION")
      }
    }

    // etc.
  }
}
```

## 🖇️ Common API

### Logger

`Logger` is the core class of the Lumberjack API and is responsible for *logging*.

```kotlin
fun myFunction() {
  // May optionally provide a Marker, cause (Throwable), and/or CoroutineContext
  logger.logc(Level.Info, ThreadLocalMutableObjectMessage.message("Hello World!"))
}
```

Lumberjack provides **many** extension functions to automatically apply a `Level` and, lazily, construct a `Message` from any arbitrary object.

```kotlin
fun myFunction() {
  logger.infoc { "Hello World!" }
}
```

Lumberjack enables users to embed additional *context* in log outputs via [`MDC`](#mdc). For non-`suspend` functions, the context must be manually provided via a `CoroutineContext` instance; hence the `c` suffix. For `suspend` functions, the `CoroutineContext` can be automatically provided by removing the `c` suffix.

```kotlin
suspend fun myFunction() {
  logger.info { "Hello World" }
}
```

Loggers can be instantiated with the `Logger.fromName` or the `Logger.fromKClass` factory functions.

### Message

Messages can act as wrappers around objects so that the user can have control over converting objects to String instances without requiring complicated formatters. The conversion is *lazy* and may never be executed (for example, insufficient logging level) which prevents unnecessary allocation of garbage.

### Level

Levels enable users to categorize log statements by *urgency*. This categorization allows filtering persistent logs, controlling the amount of information logged, or a combination of both and additional factors.

Lumberjack provides the following standard logging levels; accessible via `Level` subclasses:

| Object  | Name  | Value           |
| ------- | ----- | --------------- |
| `None`  | OFF   | 0               |
| `Fatal` | FATAL | 100             |
| `Error` | ERROR | 200             |
| `Warn`  | WARN  | 300             |
| `Info`  | INFO  | 400             |
| `Debug` | DEBUG | 500             |
| `Trace` | TRACE | 600             |
| `All`   | ALL   | `Int.MAX_VALUE` |

Lumberjack additionally supports custom logging levels. Invoking the `Level.toLevel` factory function will instantiate a `Level.Custom` instance, or return an existing `Level` if previously instantiated. `Level.fromName` is also provided to *only* return previously instantiated `Level` instances, or otherwise return the provided `defaultLevel` or `Level.Debug`.

### Marker

[Markers can be used to *color* or mark a *single* log statement.](https://stackoverflow.com/a/16820978) Markers can be instantiated with the `Marker.fromName` factory function; optionally, with related parent instances.

```kotlin
val NOTIFY: Marker = Marker.fromName("NOTIFY")
val NOTIFY_ADMIN: Marker = Marker.fromName("NOTIFY_ADMIN", NOTIFY)
```

### MDC

*Mapped Diagnostic Context* enables users to embed additional *context* in log outputs without exposing abstractions to unnecessary information. For example, a rate limiter would log *when* a rate limit has been triggered, but, assuming an abstracted rate limiter API, it would not be possible to log *what* triggered the rate limit; especially in highly concurrent applications.

Lumberjack uses [`CoroutineContext`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/) instances to pass context around in highly concurrent applications. `MDC` is a special `CoroutineContext` that Lumberjack will automatically utilize and provides an interface for storing generic information as a `Map<String, String>`.

Calling the `mdc` top-level function will return the current `MDC` of any coroutine. An optional function can be provided to add or remove key/value pairs to instantiate a new `MDC` for new coroutines.

```kotlin
suspend fun myFunction() {
  val context = mdc { it + ("key" to "value") }
  withContext(context) {
    logger.info { "Hello World" }
  }
}
```

The `MDC.fromContext` factory function instantiates a new `MDC` from non-`suspend` functions. This function discards previous contexts and is ***not*** recommended.

```kotlin
fun myFunction() {
  val context = MDC.fromContext(mapOf("key" to "value"))
  logger.infoc(context = context) { "Hello World" }
}
```
