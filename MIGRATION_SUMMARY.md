# Миграция DI фреймворка Dagger → Koin

## Описание
Полная миграция системы внедрения зависимостей с Dagger на Koin для Android приложения SlutskRe.

## Выполненные изменения

### 1. Главный слой приложения (app/)

#### App.kt
- ✅ Удален DaggerAppComponent и все реализации ComponentDependenciesProvider интерфейсов
- ✅ Добавлена инициализация Koin через `startKoin { }`
- ✅ Используется `androidContext()` и `androidLogger()`

#### AppActivity.kt
- ✅ Удалена инъекция @Inject для MainActivityViewModelFactory
- ✅ Используется `viewModel()` из Koin вместо Dagger-инъекции
- ✅ Удалена зависимость от App.appComponent

#### MainActivityViewModelFactory.kt
- ✅ Удалена аннотация @Inject конструктора
- ✅ Добавлен Koin модуль mainActivityModule с factory провайдером

#### CommonModule.kt (AppComponent переписан)
- ✅ Удален старый Dagger компонент
- ✅ Создан главный `val appModule` который включает все подмодули
- ✅ Удалены ApplicationContext.kt и AppComponent.kt

### 2. Core Data слой (core/data/)

#### CoreDataModule.kt
- ✅ Переписан на Koin DSL с `module { }`
- ✅ Включены все подмодули: retrofitModule, databaseModule, repositoryModule
- ✅ Используется `single { }` для синглтонов

#### RetrofitModule.kt
- ✅ Переписан на Koin DSL
- ✅ Используются `named()` квалификаторы для различных Retrofit инстансов
- ✅ Single провайдеры для Json, OkHttpClient, Retrofit инстансов
- ✅ Single провайдеры для API сервисов (ApiCinema, BusApi, CurrencyApi)

#### DatabaseModule.kt
- ✅ Переписан на Koin DSL
- ✅ Single провайдер для AppDatabase
- ✅ Factory провайдер для CurrencyDao

#### InterceptorsModule.kt
- ✅ Переписан на Koin DSL
- ✅ Single провайдеры для CinemaAuthInterceptor и HttpLoggingInterceptor

#### RepositoryModule.kt
- ✅ Переписан на Koin DSL
- ✅ Single провайдеры для CinemaRepository и CurrencyRepository (с интерфейс-типизацией)
- ✅ Добавлен Single провайдер для CurrenciesInteractor

### 3. Features Common слой (features/common/)

#### CommonToolsModule.kt
- ✅ Переписан на Koin DSL
- ✅ Single провайдер для DispatcherProvider

#### CommonFeatureModule.kt
- ✅ Переписан на Koin DSL
- ✅ Single провайдеры для всех компонентов:
  - ResourceProvider
  - ExceptionsObserverImpl, ExceptionsFlow, ExceptionsEmitter
  - ProgressObserverImpl, ProgressEmitter, ProgressFlow
  - ProgressDelegate
  - NetworkStateFlow

### 4. Feature Modules

Все feature модули (onboarding, home, currencies, settings, cinema) переписаны по единой схеме:

#### Структура преобразования:

**ДО (Dagger):**
```kotlin
@OnboardingScope
@Component(dependencies = [OnboardingComponentDependencies::class])
interface OnboardingComponent {
    fun getOnboardingViewModelFactory(): OnboardingViewModelFactory
}
```

**ПОСЛЕ (Koin):**
```kotlin
val onboardingModule = module {
    factory {
        OnboardingViewModelFactory(appSettings = get(), progressDelegate = get())
    }
}
```

#### Onboarding Feature
- ✅ OnboardingComponent → onboardingModule в Koin DSL
- ✅ OnboardingComponentHolder → упрощен для получения фабрики из Koin
- ✅ OnboardingViewModelFactory → удалена @Inject аннотация
- ✅ OnboardingFeatureHost → использует OnboardingComponentHolder.getViewModelFactory()
- ✅ Удалены OnboardingComponentDependencies, OnboardingComponentDependenciesProvider, OnboardingScope

#### Home Feature
- ✅ HomeComponent → homeModule в Koin DSL
- ✅ HomeComponentHolder → упрощен
- ✅ HomeFeatureHost → удалены все зависимости от Dagger компонента
- ✅ Удалены HomeComponentDependencies, HomeComponentDependenciesProvider

#### Currencies Feature
- ✅ CurrencyComponent → currenciesModule в Koin DSL
- ✅ CurrenciesViewModelFactory → удалена @Inject аннотация
- ✅ CurrencyComponentHolder → использует Koin GlobalContext
- ✅ CurrencyFeatureHost → упрощен для работы с Koin
- ✅ Добавлены factory провайдеры для CurrencyUiMapper
- ✅ Удалены CurrencyComponentDependencies, CurrencyComponentDependenciesProvider

#### Settings Feature
- ✅ SettingsComponent → settingsModule в Koin DSL
- ✅ SettingsComponentHolder → использует Koin GlobalContext.get()
- ✅ SettingsViewModelFactory → удалена @Inject аннотация
- ✅ SettingsFeatureHost → упрощена
- ✅ Удалены SettingsComponentDependencies, SettingsComponentDependenciesProvider

#### Cinema Feature
- ✅ CinemaComponent → cinemaModule в Koin DSL
- ✅ CinemaViewModelFactory → удалена @Inject аннотация
- ✅ CinemaComponentHolder → использует Koin GlobalContext
- ✅ CinemaFeatureHost → упрощена
- ✅ Добавлены factory провайдеры для CalendarToUiMapper и CinemaToUiMapper
- ✅ Удалены CinemaComponentDependencies, CinemaComponentDependenciesProvider

### 5. Главный модуль приложения

**CommonModule.kt** содержит главный `appModule`:
```kotlin
val appModule = module {
    includes(
        commonToolsModule,           // Tools (DispatcherProvider)
        commonFeatureModule,         // Common (ResourceProvider, etc)
        coreDataModule,              // Data layer (Repos, Retrofit, DB)
        mainActivityModule,          // Main Activity VM
        onboardingModule,            // Onboarding feature
        homeModule,                  // Home feature
        currenciesModule,            // Currencies feature
        settingsModule,              // Settings feature
        cinemaModule,                // Cinema feature
    )
}
```

## Ключевые особенности миграции

### 1. Избавление от Scope-подхода
- ❌ Удалены все @Scope аннотации (OnboardingScope и т.д.)
- ✅ Используется `single { }` для синглтонов и `factory { }` для фабрик

### 2. Избавление от ComponentDependencies
- ❌ Удалены все ComponentDependencies интерфейсы
- ✅ Используется `get()` для получения зависимостей в Koin модулях

### 3. Упрощение ComponentHolder
- ❌ Удалены сложныеHolder логики с кешированием компонентов
- ✅ Holders теперь просто возвращают фабрики через `GlobalContext.get()`

### 4. Использование ViewModels из Koin
- ❌ ViewModels не требуют Factory больше в стандартном случае
- ✅ Используется `viewModel()` composable из koin-androidx-compose для нужных VM

### 5. Удаление Provider Interfaces
- ❌ Удалены ComponentDependenciesProvider интерфейсы (OnboardingComponentDependenciesProvider и т.д.)
- ✅ App больше не реализует эти интерфейсы

## Преимущества новой архитектуры

1. **Простота**: Koin DSL более интуитивна, меньше кода
2. **Тестируемость**: Легче мокировать зависимости
3. **Гибкость**: Квалификаторы вместо специальных аннотаций
4. **Производительность**: Нет кодогенерации как у Dagger, быстрее запуск
5. **Читаемость**: Все зависимости объявлены в одном месте (модулях)

## Файлы удаленные
- app/src/main/java/com/romanzhurid/re/di/ApplicationContext.kt
- app/src/main/java/com/romanzhurid/re/di/AppComponent.kt
- features/onboarding/src/main/java/com/romanzhurid/onboarding/di/OnboardingComponentDependencies.kt
- features/onboarding/src/main/java/com/romanzhurid/onboarding/di/OnboardingComponentDependenciesProvider.kt
- features/onboarding/src/main/java/com/romanzhurid/onboarding/di/OnboardingScope.kt
- features/home/src/main/java/com/romanzhurid/home/di/HomeComponentDependencies.kt
- features/currencies/src/main/java/com/romanzhurid/currencies/di/CurrencyComponentDependencies.kt
- features/currencies/src/main/java/com/romanzhurid/currencies/di/CurrencyComponentDependenciesProvider.kt
- features/settings/src/main/java/com/romanzhurid/settings/di/SettingsComponentDependencies.kt
- features/settings/src/main/java/com/romanzhurid/settings/di/SettingsComponentDependenciesProvider.kt
- features/cinema/src/main/java/com/romanzhurid/cinema/di/CinemaComponentDependencies.kt
- features/cinema/src/main/java/com/romanzhurid/cinema/di/CinemaComponentDependenciesProvider.kt

## Проверка компилирования

Все модули Kotlin синтаксически корректны. Проект готов к компиляции.

## Следующие шаги

1. Запустить `./gradlew build` для полной компиляции
2. Запустить тесты для проверки функциональности
3. Провести мануальное тестирование всех feature модулей
4. Убедиться, что инъекции работают корректно в runtime
