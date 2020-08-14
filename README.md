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

## 📄 Documentation

Documentation including installation instructions and API usage can be found on the [Wiki](https://github.com/NeonTech/Lumberjack/wiki).
