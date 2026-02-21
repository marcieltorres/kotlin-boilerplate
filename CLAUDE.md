# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project State

This is an early-stage Kotlin boilerplate. No build system, source files, or tests have been added yet.

## Environment

- **JDK**: Amazon Corretto 20 (configured in `.idea/misc.xml`)
- **Language**: Kotlin

## Expected Build System

The `.gitignore` includes entries for Gradle (`.kotlin/` Kotlin Gradle plugin data directory), so Gradle is the likely build tool to be added. When a `build.gradle.kts` or `settings.gradle.kts` is present, use:

```bash
./gradlew build          # compile and assemble
./gradlew test           # run all tests
./gradlew test --tests "com.example.FooTest.methodName"  # run a single test
./gradlew check          # run tests + static analysis
./gradlew ktlintCheck    # lint (if ktlint is configured)
./gradlew ktlintFormat   # auto-fix lint issues
```

Update this file once a build system and project structure are established.
