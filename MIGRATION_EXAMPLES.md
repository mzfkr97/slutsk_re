# Примеры ключевых изменений при миграции Dagger → Koin

## 1. Инициализация приложения

### ДО (Dagger):
```kotlin
class App : 
    Application(),
    OnboardingComponentDependenciesProvider,
    HomeComponentDependenciesProvider,
    CurrencyComponentDependenciesProvider,
    SettingsComponentDependenciesProvider,
    CinemaComponentDependenciesProvider {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }

    override val onboardingComponentDependencies: OnboardingComponentDependencies
        get() = appComponent
    // ... другие dependencies ...
}
```

### ПОСЛЕ (Koin):
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}
```

## 2. Главный компонент

### ДО (Dagger):
```kotlin
@Singleton
@Component(
    modules = [
        CommonModule::class,
        CommonFeatureModule::class,
        CommonToolsModule::class,
        CoreDataModule::class,
        RepositoryModule::class,
    ]
)
interface AppComponent :
    OnboardingComponentDependencies,
    HomeComponentDependencies,
    CurrencyComponentDependencies,
    SettingsComponentDependencies,
    CinemaComponentDependencies {

    fun inject(activity: AppActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
```

### ПОСЛЕ (Koin):
```kotlin
val appModule = module {
    includes(
        commonToolsModule,
        commonFeatureModule,
        coreDataModule,
        mainActivityModule,
        onboardingModule,
        homeModule,
        currenciesModule,
        settingsModule,
        cinemaModule,
    )
}
```

## 3. Модули данных

### ДО (Dagger):
```kotlin
@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideJson(): Json { ... }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient { ... }

    @CinemaApi
    @Provides
    @Singleton
    fun provideCinemaOkHttpClient(...): OkHttpClient { ... }
}
```

### ПОСЛЕ (Koin):
```kotlin
val retrofitModule = module {
    includes(interceptorsModule)

    single {
        Json { ... }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(named("cinemaHttpClient")) {
        val okHttpClient: OkHttpClient = get()
        val cinemaAuthInterceptor: CinemaAuthInterceptor = get()
        okHttpClient.newBuilder()
            .addInterceptor(cinemaAuthInterceptor)
            .build()
    }
}
```

## 4. Feature компоненты

### ДО (Dagger):
```kotlin
@OnboardingScope
@Component(
    dependencies = [OnboardingComponentDependencies::class]
)
interface OnboardingComponent {
    fun getOnboardingViewModelFactory(): OnboardingViewModelFactory

    companion object {
        private var component: OnboardingComponent? = null

        fun get(): OnboardingComponent {
            return component ?: throw NotImplementedError("Component must be initialized")
        }
    }
}

object OnboardingComponentHolder {
    private val components = mutableMapOf<String, OnboardingComponent>()

    fun get(instanceId: String, dependencies: OnboardingComponentDependencies): OnboardingComponent {
        return components.getOrPut(instanceId) {
            DaggerOnboardingComponent.builder()
                .onboardingComponentDependencies(dependencies)
                .build()
        }
    }

    fun clear(instanceId: String) {
        components.remove(instanceId)
    }
}
```

### ПОСЛЕ (Koin):
```kotlin
val onboardingModule = module {
    factory {
        OnboardingViewModelFactory(
            appSettings = get(),
            progressDelegate = get(),
        )
    }
}

object OnboardingComponentHolder {
    fun getViewModelFactory(): OnboardingViewModelFactory {
        return GlobalContext.get().get()
    }
}
```

## 5. ViewModelFactory

### ДО (Dagger):
```kotlin
class OnboardingViewModelFactory @Inject constructor(
    private val appSettings: AppSettings,
    private val progressDelegate: ProgressDelegate,
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardingViewModel(
            appSettings = appSettings,
            progressDelegate = progressDelegate,
        ) as T
    }
}
```

### ПОСЛЕ (Koin):
```kotlin
class OnboardingViewModelFactory(
    private val appSettings: AppSettings,
    private val progressDelegate: ProgressDelegate,
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OnboardingViewModel(
            appSettings = appSettings,
            progressDelegate = progressDelegate,
        ) as T
    }
}

// В модуле:
val onboardingModule = module {
    factory {
        OnboardingViewModelFactory(get(), get())
    }
}
```

## 6. Composable экраны

### ДО (Dagger):
```kotlin
@Composable
fun OnboardingFeatureHost(route: AppRoute.Onboarding) {
    val context = LocalContext.current.applicationContext
    val appNavigator = LocalNavigator.current

    val component = remember(route.instanceId) {
        OnboardingComponentHolder.get(
            instanceId = route.instanceId,
            dependencies = (context as OnboardingComponentDependenciesProvider)
                .onboardingComponentDependencies
        )
    }

    FeatureHost(
        route = route.instanceId,
        clearComponent = {
            OnboardingComponentHolder.clear(route.instanceId)
        },
        initialStack = { listOf(OnboardingFeatureRoute.Onboarding) },
        entryProviderFactory = {
            entryProvider<OnboardingFeatureRoute> {
                entry<OnboardingFeatureRoute.Onboarding> {
                    val viewModel = viewModel<OnboardingViewModel>(
                        factory = component.getOnboardingViewModelFactory()
                    )
                    OnboardingScreen(viewModel = viewModel, onFinished = { ... })
                }
            }
        }
    )
}
```

### ПОСЛЕ (Koin):
```kotlin
@Composable
fun OnboardingFeatureHost(route: AppRoute.Onboarding) {
    val appNavigator = LocalNavigator.current

    FeatureHost(
        route = route.instanceId,
        clearComponent = {},
        initialStack = { listOf(OnboardingFeatureRoute.Onboarding) },
        entryProviderFactory = {
            entryProvider<OnboardingFeatureRoute> {
                entry<OnboardingFeatureRoute.Onboarding> {
                    val factory = OnboardingComponentHolder.getViewModelFactory()
                    val viewModel = viewModel<OnboardingViewModel>(factory = factory)
                    OnboardingScreen(viewModel = viewModel, onFinished = { ... })
                }
            }
        }
    )
}
```

## 7. Activity

### ДО (Dagger):
```kotlin
class AppActivity : ComponentActivity() {

    @Inject
    lateinit var factory: MainActivityViewModelFactory
    private val viewModel: MainActivityViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as App).appComponent.inject(this)
        // ...
    }
}
```

### ПОСЛЕ (Koin):
```kotlin
class AppActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
    }
}
```

## Ключевые различия

| Аспект | Dagger | Koin |
|--------|--------|------|
| **Инициализация** | DaggerComponent.factory().create() | startKoin { modules(...) } |
| **Синглтоны** | @Singleton + @Provides | single { } |
| **Фабрики** | ComponentFactory интерфейсы | factory { } |
| **Внедрение** | @Inject аннотация | get() в модулях |
| **Квалификаторы** | @Named, кастомные @Qualifier | named() квалификатор |
| **Зависимости между модулями** | Component dependencies интерфейсы | includes(module1, module2, ...) |
| **Scope управление** | @Scope аннотации + Component holder | GlobalContext.get() / module { } |
| **Runtime | Кодогенерация (APT) | Прямое выполнение |

## Преимущества Koin

1. **Проще для понимания**: DSL синтаксис более интуитивна
2. **Меньше кода**: Нет нужды в интерфейсах и фабриках
3. **Быстрее запуск**: Нет кодогенерации
4. **Удобнее тестирование**: Легко переопределить provideर в тестах
5. **Гибче**: Динамическое создание модулей на runtime
6. **Лучше ошибки**: Ошибки выявляются в runtime, но более понятны
