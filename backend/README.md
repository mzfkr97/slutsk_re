# SlutskRe Backend

Этот модуль представляет собой серверную часть приложения **SlutskRe**, построенную на фреймворке **Ktor**. Он предназначен для предоставления актуального расписания автобусов и других данных через REST API.

## Технологический стек
- **Kotlin** (JVM)
- **Ktor Server** (Core, Netty)
- **Kotlinx Serialization** (JSON)
- **Logback** (Logging)

## Структура проекта
Проект организован по модульному принципу для удобства масштабирования:

- `com.romanzhurid.backend.models` — Data-классы и модели данных для API.
- `com.romanzhurid.backend.routing` — Логика обработки HTTP-запросов, разделенная по функциональным модулям.
  - `Routing.kt` — Главный конфигуратор, собирающий все маршруты.
  - `BusRoutes.kt` — Эндпоинты для работы с расписанием автобусов.
- `Application.kt` — Точка входа в приложение и настройка плагинов (Serialization, Logging).

## Как запустить локально

### Через Android Studio
1. Откройте файл `src/main/kotlin/com/romanzhurid/backend/Application.kt`.
2. Нажмите на иконку запуска (зеленый треугольник) рядом с функцией `main()`.
3. Сервер запустится на `http://0.0.0.0:8080`.

### Через терминал
```bash
./gradlew :backend:run
```

## API Endpoints

### Получение расписания
- `GET /` — Проверка статуса сервера.
- `GET /schedule` — Получение всех расписаний автобусов.
- `GET /schedule/{id}` — Получение информации о конкретном рейсе по ID.
- `GET /schedule/bus/{number}` — Получение расписаний для конкретного номера автобуса.
- `GET /schedule/station/{stationName}` — Получение расписаний для конкретной станции.

### Примеры запросов

```bash
# Все расписания
curl http://localhost:8080/schedule

# По ID
curl http://localhost:8080/schedule/1

# По номеру автобуса
curl http://localhost:8080/schedule/bus/2

# По названию станции
curl http://localhost:8080/schedule/station/Мясокомбинат
```

## Разработка

### Добавление новых маршрутов
Для добавления новой функциональности (например, "Новости"):
1. Создайте файл `routing/NewsRoutes.kt`.
2. Опишите маршруты в функции-расширении `fun Route.newsRoutes() { ... }`.
3. Зарегистрируйте их в `routing/Routing.kt` внутри блока `routing { ... }`.

## Подготовка к продакшену (Deployment)

### Сборка JAR-файла
Для создания исполняемого "толстого" JAR (Fat JAR), который содержит все зависимости:
```bash
./gradlew :backend:buildFatJar
```
Файл будет находиться в `backend/build/libs/`.

### Запуск на сервере
Для запуска на сервере достаточно установленной JRE 17+:
```bash
java -jar backend-all.jar
```

## База данных

### Структура backend модуля

**Основные компоненты:**
- `DatabaseFactory.kt` — инициализация и конфигурация БД
- `Application.kt` — главное приложение Ktor
- `models/BusSchedule.kt` — модель данных
- `routing/` — обработка API маршрутов

### Работа с базой данных

Файл базы данных SQLite `app_database.db` должен находиться в корневой директории модуля при локальном запуске или рядом с JAR-файлом на сервере.

#### Просмотр содержимого БД

1. **Через терминал:**
```bash
cd backend
sqlite3 app_database.db
```

2. **Основные команды SQLite:**
```sql
.tables           -- список таблиц
.schema           -- структура всех таблиц
.mode column      -- красивый вывод
SELECT * FROM table_name;  -- содержимое таблицы
.quit             -- выход
```

3. **Примеры:**
```bash
sqlite3 backend/app_database.db ".tables"
sqlite3 backend/app_database.db ".schema"
sqlite3 backend/app_database.db "SELECT * FROM bus_schedule LIMIT 10;"
```
