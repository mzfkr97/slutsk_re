# Shared Element Transition для просмотра постеров фильмов

## Цель

Сделать анимацию перехода от постера внутри `CinemaCard` к полноэкранному `ImageViewer`.

Эффект:

- Пользователь нажимает на постер фильма.
- Постер плавно увеличивается до полноэкранного изображения.
- При закрытии изображение анимированно возвращается обратно в карточку.

---

## Текущее ограничение

Сейчас используется:

```kotlin
if (showViewer) {
    ImageViewer(...)
}
```

а внутри `ImageViewer` используется:

```kotlin
Dialog(...)
```

Такой подход несовместим с Shared Element Transition.

Причина:

`Dialog` отображается в отдельном окне Android и находится вне Compose-дерева, поэтому `SharedTransitionLayout` не может связать исходный и целевой элементы.

---

## Требуемые изменения

### 1. Поднять состояние открытия изображения на уровень экрана

Не хранить:

```kotlin
var showViewer by rememberSaveable { mutableStateOf(false) }
```

внутри `CinemaCard`.

Вместо этого хранить на уровне `CinemaScreen`.

Пример:

```kotlin
var selectedFilm by remember {
    mutableStateOf<CinemaUiItem.CinemaUi?>(null)
}
```

---

### 2. Передавать событие из карточки

Добавить callback:

```kotlin
@Composable
fun CinemaCard(
    item: CinemaUiItem.CinemaUi,
    onImageClick: (CinemaUiItem.CinemaUi) -> Unit
)
```

Нажатие:

```kotlin
.clickable {
    onImageClick(item)
}
```

---

### 3. Обернуть экран в SharedTransitionLayout

На уровне `CinemaScreen`:

```kotlin
SharedTransitionLayout {
    AnimatedContent(...)
}
```

Все элементы, участвующие в анимации, должны находиться внутри этого контейнера.

---

### 4. Использовать один и тот же Shared Key

Для карточки:

```kotlin
sharedContentState = rememberSharedContentState(
    key = item.imageUrl
)
```

Для полноэкранного Viewer:

```kotlin
sharedContentState = rememberSharedContentState(
    key = selectedFilm.imageUrl
)
```

Ключи должны совпадать.

---

### 5. Отказаться от Dialog

Viewer должен отображаться поверх списка внутри того же Compose-дерева.

Например:

```kotlin
Box(
    modifier = Modifier.fillMaxSize()
) {
    LazyColumn(...)

    selectedFilm?.let {
        FullscreenViewer(...)
    }
}
```

---

## Карточка

Постер внутри списка должен иметь:

```kotlin
AsyncImage(
    modifier = Modifier.sharedElement(
        sharedContentState = rememberSharedContentState(
            key = item.imageUrl
        ),
        animatedVisibilityScope = animatedVisibilityScope
    ),
    ...
)
```

---

## Fullscreen Viewer

Полноэкранное изображение также должно иметь:

```kotlin
AsyncImage(
    modifier = Modifier.sharedElement(
        sharedContentState = rememberSharedContentState(
            key = imageUrl
        ),
        animatedVisibilityScope = animatedVisibilityScope
    ),
    ...
)
```

---

## Дополнительные эффекты

Поверх Shared Element рекомендуется добавить:

### Затемнение фона

```kotlin
fadeIn()
fadeOut()
```

### Масштабирование

```kotlin
scaleIn()
scaleOut()
```

### Пружинную анимацию

```kotlin
spring(
    dampingRatio = Spring.DampingRatioLowBouncy
)
```

---

## Ожидаемый результат

При нажатии на постер:

```text
┌───────┐
│ POSTER│
└───────┘
      ↓
      ↓
      ↓
 ┌───────────────┐
 │               │
 │    POSTER     │
 │               │
 └───────────────┘
```

При закрытии:

```text
 ┌───────────────┐
 │               │
 │    POSTER     │
 │               │
 └───────────────┘
      ↑
      ↑
      ↑
┌───────┐
│ POSTER│
└───────┘
```

---

## Важно

Для корректной работы:

- Не использовать `Dialog`.
- Не использовать отдельный экран вне `SharedTransitionLayout`.
- Исходный и целевой элементы должны одновременно существовать внутри одного Compose дерева.
- Использовать одинаковый `key` для карточки и Viewer.