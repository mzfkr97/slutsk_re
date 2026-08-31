# Чек-лист миграции Dagger → Koin

## App слой
- [x] App.kt - переписан на Koin.startKoin()
- [x] AppActivity.kt - использует viewModel() из Koin
- [x] MainActivityViewModelFactory.kt - добавлен mainActivityModule
- [x] CommonModule.kt - создан главный appModule с includes всех подмодулей
- [x] Удалены ApplicationContext.kt и AppComponent.kt

## Core Data слой
- [x] CoreDataModule.kt - переписан на Koin DSL
- [x] InterceptorsModule.kt - переписан на Koin DSL
- [x] RetrofitModule.kt - переписан на Koin DSL с named квалификаторами
- [x] DatabaseModule.kt - переписан на Koin DSL
- [x] RepositoryModule.kt - переписан на Koin DSL

## Common Feature слой
- [x] CommonToolsModule.kt - переписан на Koin DSL
- [x] CommonFeatureModule.kt - переписан на Koin DSL

## Feature Modules

### Onboarding
- [x] OnboardingComponent.kt → onboardingModule в Koin
- [x] OnboardingComponentHolder.kt - упрощен
- [x] OnboardingViewModelFactory.kt - удалена @Inject
- [x] OnboardingFeatureHost.kt - использует Koin
- [x] Удалены ComponentDependencies, ComponentDependenciesProvider, Scope

### Home
- [x] HomeComponent.kt → homeModule в Koin
- [x] HomeComponentHolder.kt - упрощен
- [x] HomeFeatureHost.kt - упрощена
- [x] Удалены ComponentDependencies, ComponentDependenciesProvider

### Currencies
- [x] CurrencyComponent.kt → currenciesModule в Koin
- [x] CurrenciesViewModelFactory.kt - удалена @Inject
- [x] CurrencyComponentHolder.kt - использует GlobalContext
- [x] CurrencyFeatureHost.kt - упрощена
- [x] Добавлены mappers в модуль
- [x] Удалены ComponentDependencies, ComponentDependenciesProvider

### Settings
- [x] SettingsComponent.kt → settingsModule в Koin
- [x] SettingsViewModelFactory.kt - удалена @Inject
- [x] SettingsComponentHolder.kt - использует GlobalContext
- [x] SettingsFeatureHost.kt - упрощена
- [x] Удалены ComponentDependencies, ComponentDependenciesProvider

### Cinema
- [x] CinemaComponent.kt → cinemaModule в Koin
- [x] CinemaViewModelFactory.kt - удалена @Inject
- [x] CinemaComponentHolder.kt - использует GlobalContext
- [x] CinemaFeatureHost.kt - упрощена
- [x] Добавлены mappers в модуль
- [x] Удалены ComponentDependencies, ComponentDependenciesProvider

## Верификация
- [x] Все модули используют Koin DSL (module { })
- [x] Все синглтоны используют single { }
- [x] Все фабрики используют factory { }
- [x] Все зависимости внедряются через get()
- [x] Нет больше @Inject аннотаций на конструкторах
- [x] Нет больше Dagger @Component интерфейсов
- [x] Нет больше @Module аннотаций (используется Koin DSL)
- [x] Все FeatureHost используют Koin для получения ViewModels
- [x] AppActivity использует viewModel() из Koin

## Компиляция
- [ ] ./gradlew build - успешная компиляция
- [ ] Нет ошибок Kotlin компилятора
- [ ] Нет unresolved references на Dagger классы

## Runtime тесты
- [ ] App запускается без ошибок
- [ ] Onboarding экран загружается корректно
- [ ] Home экран загружается корректно
- [ ] Currencies экран загружается корректно
- [ ] Settings экран загружается корректно
- [ ] Cinema экран загружается корректно
- [ ] Navigation между экранами работает
- [ ] ViewModels получают зависимости корректно
- [ ] Нет NullPointerException при инъекции

## Финальная проверка
- [ ] Код отформатирован согласно style guide
- [ ] Нет невиспользуемых импортов
- [ ] Нет логических ошибок в DI конфигурации
- [ ] Проверены все места где используется get() - зависимость должна быть провайдена
