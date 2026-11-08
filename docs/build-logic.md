# Настройка Gradle-модулей

Общие настройки сборки находятся в included build [build-logic](../build-logic/build.gradle.kts). Модуль выбирает convention-плагин, задаёт namespace и объявляет свои зависимости. Версии библиотек, плагинов, SDK и Java находятся в [libs.versions.toml](../gradle/libs.versions.toml).

## Плагины

| Alias в `libs.plugins` | Назначение |
|---|---|
| `iu3.android.application` | Android-приложение: compile/min/target SDK, Java, instrumentation runner |
| `iu3.android.library` | Обычный core-модуль: Android Library, compile/min SDK, Java |
| `iu3.android.api` | Контракты и модели: настройки library, генерация Android-ресурсов выключена |
| `iu3.android.feature` | Feature-модуль: library и Compose с общими UI-зависимостями |
| `iu3.android.compose` | Compose compiler, build feature, BOM, UI/Foundation/Material/icons и Lifecycle |
| `iu3.android.hilt` | Hilt и KSP, runtime и compiler; для feature также Hilt ViewModel Compose |
| `iu3.android.serialization` | Kotlin serialization compiler; видимость JSON runtime модуль выбирает сам через `api` или `implementation` |

Hilt подключается там, где есть внедряемые зависимости. Например, статический `features:alumni` использует только feature-плагин. `core:designsystem` подключает library и compose. Android-модули продолжают использовать встроенный Kotlin AGP 9.

## Feature-модуль

```kotlin
plugins {
    alias(libs.plugins.iu3.android.feature)
    alias(libs.plugins.iu3.android.hilt)
}

android {
    namespace = "com.bmstu.iu3.features.example"
}

dependencies {
    implementation(projects.core.dataApi)
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
}
```

Связи между модулями объявляются явно: feature-плагин добавляет общие внешние зависимости UI, а набор core-модулей выбирается в самой фиче. Это сохраняет видимый граф зависимостей и позволяет фичам использовать разные контракты.

## API-модуль

```kotlin
plugins {
    alias(libs.plugins.iu3.android.api)
}

android {
    namespace = "com.bmstu.iu3.core.example.api"
}

dependencies {
    api(projects.core.model)
    api(libs.coroutines)
}
```

`api` используется для типов, которые входят в публичные сигнатуры модуля. Зависимости реализации подключаются через `implementation`.

## Версии и новые модули

`android-compileSdk`, `android-minSdk`, `android-targetSdk` и `jvm` в каталоге задают общие значения. Application ID, версия приложения и namespace остаются в модуле-владельце. Исходные зависимости тестовых наборов сохранены в `app`; плагины не добавляют наборы тестов автоматически.

Для текущей связки KSP/Hilt в `gradle.properties` задано `ksp.incremental=false`: при инкрементальной обработке наблюдалась потеря регистраций ViewModel при успешной сборке APK. Изменённые KSP-модули обрабатываются полностью; configuration cache и обычные up-to-date проверки Gradle работают. Перед возвратом инкрементальности нужны проверка полноты Hilt-регистраций после изменения исходника и запуск приложения; подробности — в [отчёте проверки](verification.md).

Новый модуль регистрируется через `include(":features:example")` в корневом `settings.gradle.kts`. После Gradle Sync становится доступен `projects.features.example`. Зависимости объявляются через эти [type-safe project accessors](https://docs.gradle.org/current/userguide/declaring_dependencies_basics.html#sec:type-safe-project-accessors); `:core:data-api` соответствует `projects.core.dataApi`.

Плагины реализованы обычными Kotlin-классами в `build-logic/src/main/kotlin/com/bmstu/iu3/buildlogic`, зарегистрированы через `gradlePlugin` и доступны основному проекту через `pluginManagement.includeBuild`. Included build читает тот же каталог версий. Общая конфигурация применяется плагином каждого модуля; корневых `allprojects/subprojects` блоков нет. Подход соответствует [разделению общей build logic в Gradle](https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html).
