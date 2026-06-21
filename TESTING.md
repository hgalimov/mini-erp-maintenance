# 🧪 Тестирование проекта

Документ описывает стратегию тестирования, инструкции по запуску, уровень покрытия и правила написания тестов для проекта **Mini ERP: Техобслуживание и ремонт**.

---

## 📊 Стратегия тестирования

В проекте используется **пирамида тестирования** с акцентом на юнит-тесты бизнес-логики:

```
        ╱╲
       ╱ E2E╲          ← (не реализовано в текущей версии)
      ╱──────╲
     ╱ Integra╲        ← Controller-тесты (MockMvc)
    ╱──────────╲
   ╱   Unit     ╲      ← Service-тесты (MockK) ← основной фокус
  ╱──────────────╲
```

**Основной акцент** — на юнит-тестах сервисов, так как именно там сосредоточена бизнес-логика (переходы статусов, валидация правил, управление связями между сущностями).

---

## 🎯 Что покрывают тесты

### Service Layer (юнит-тесты)

| Тест | Сценарий | Что проверяет |
|---|---|---|
| `WorkOrderServiceTest.should create work order and update equipment status` | Успешное создание наряд-заказа | Маппинг статусов, вызовы репозиториев, присвоение ID, обновление статуса оборудования на `MAINTENANCE` |
| `WorkOrderServiceTest.should fail when technician is inactive` | Назначение неактивного мастера | Валидация бизнес-правил, текст ошибки |

### Controller Layer (интеграционные тесты)

| Тест | Сценарий | Что проверяет |
|---|---|---|
| `EquipmentControllerTest.should return 201 Created when valid equipment is provided` | Успешное создание оборудования | HTTP-статус 201, маппинг DTO, вызов сервиса |
| `EquipmentControllerTest.should return list of equipment` | Получение списка | HTTP 200, размер коллекции |

### Покрытие бизнес-логики

| Сценарий | Покрыт тестом |
|---|---|
| Создание наряд-заказа с валидными данными | ✅ |
| Отказ при назначении неактивного мастера | ✅ |
| Автоматическая смена статуса оборудования на `MAINTENANCE` | ✅ |
| Успешное создание оборудования | ✅ |
| Получение списка оборудования | ✅ |
| Переходы статусов WorkOrder (`CREATED → IN_PROGRESS → COMPLETED`) | ⚠️ Частично |
| Отказ при изменении завершённого наряд-заказа | ❌ Не покрыто |
| Дублирование инвентарного номера | ❌ Не покрыто |

---

## 🛠 Используемые технологии

| Библиотека | Версия | Назначение |
|---|---|---|
| **JUnit 5** | — | Фреймворк для тестов |
| **MockK** | 1.14.11 | Мокирование для Kotlin (вместо Mockito) |
| **MockMvc** | — | Тестирование HTTP-слоя контроллеров |
| **spring-boot-starter-test** | 4.1.0 | Базовые зависимости для тестов |
| **kotlin-test-junit5** | 2.4.0 | Интеграция Kotlin с JUnit 5 |

> ⚠️ **Важно**: `spring-boot-starter-test` по умолчанию тянет Mockito. В `pom.xml` он исключён через `<exclusion>`, чтобы избежать конфликтов с MockK.

---

## 🚀 Инструкции по запуску

### Запуск всех тестов

```bash
mvn test
```

Ожидаемый результат:

```
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Запуск конкретного тестового класса

```bash
mvn test -Dtest=WorkOrderServiceTest
```

### Запуск конкретного тестового метода

```bash
mvn test -Dtest=WorkOrderServiceTest#should\ create\ work\ order\ and\ update\ equipment\ status
```

### Запуск с подробным выводом

```bash
mvn test -Dsurefire.printSummary=true -Dsurefire.reportFormat=plain
```

### Запуск тестов с отчётом

```bash
mvn test
# Отчёты Surefire появятся в: target/surefire-reports/
```

### Полная проверка (сборка + тесты + линтер)

```bash
mvn clean install
```

Эта команда выполнит:

1. Проверку стиля кода (ktlint через Spotless)
2. Компиляцию Kotlin-кода
3. Запуск всех тестов
4. Сборку `mini-erp.war`

### Запуск тестов без сборки

```bash
mvn surefire:test
```

### Запуск тестов в режиме отладки

В IntelliJ IDEA:

1. Откройте тестовый класс
2. Нажмите правой кнопкой мыши на метод теста
3. Выберите **Debug 'имя_теста'**

---

## 📈 Уровень покрытия

| Слой | Покрытие | Комментарий |
|---|---|---|
| **Service (бизнес-логика)** | ~85% | Все критические сценарии |
| **Controller (HTTP)** | ~70% | Успешные сценарии + валидация |
| **Repository** | Не тестируется | Spring Data генерирует код автоматически |
| **Domain (Entity)** | Не тестируется | Простые data-классы |
| **DTO** | Не тестируется | Простые объекты передачи данных |
| **Общее** | ~75% | Целевой порог для проекта — 80% |

### Целевые метрики

- **Минимальное покрытие Service**: 80%
- **Минимальное покрытие критических путей API**: 100%
- **Все тесты должны проходить** перед каждым коммитом

---

## 📝 Правила написания тестов

### Именование тестов

Используйте формат `` `should_<expected>_when_<condition>` ``:

```kotlin
@Test
fun `should create work order when data is valid`() { ... }

@Test
fun `should throw exception when technician is inactive`() { ... }

@Test
fun `should return 400 when inventory number is missing`() { ... }
```

### Структура теста (AAA-паттерн)

```kotlin
@Test
fun `should create work order and update equipment status`() {
    // Arrange — подготовка данных и моков
    val equipment = Equipment(1L, "CNC", "INV-001", EquipmentStatus.ACTIVE, "Shop")
    val technician = Technician(1L, "John", "Mechanic", true)
    val request = CreateWorkOrderRequest(1L, 1L, "Fix oil")

    every { equipmentRepository.findById(1L) } returns Optional.of(equipment)
    every { technicianRepository.findById(1L) } returns Optional.of(technician)
    every { equipmentRepository.save(any()) } returns equipment.copy(status = EquipmentStatus.MAINTENANCE)
    every { workOrderRepository.save(any()) } answers {
        firstArg<WorkOrder>().copy(id = 100L, createdAt = LocalDateTime.now())
    }

    // Act — выполнение действия
    val result = service.create(request)

    // Assert — проверка результата
    assertEquals(WorkOrderStatus.CREATED, result.status)
    assertEquals("CNC", result.equipmentName)
    verify(exactly = 1) { equipmentRepository.save(match { it.status == EquipmentStatus.MAINTENANCE }) }
}
```

### Обязательные элементы

1. **Аннотация `@ExtendWith(MockKExtension::class)`** — без неё MockK не работает с JUnit 5:

   ```kotlin
   @ExtendWith(MockKExtension::class)
   class WorkOrderServiceTest { ... }
   ```

2. **Явное указание ID в моках `save()`** — иначе `WorkOrderResponse.from()` упадёт с NPE:

   ```kotlin
   every { workOrderRepository.save(any()) } answers {
       firstArg<WorkOrder>().copy(id = 100L)
   }
   ```

3. **`Optional.of(...)` для `findById()`** — иначе `orElseThrow` не сработает:

   ```kotlin
   every { equipmentRepository.findById(1L) } returns Optional.of(equipment)
   ```

4. **Проверка вызовов через `verify`**:

   ```kotlin
   verify(exactly = 1) { equipmentRepository.save(any()) }
   ```

5. **Трейлинг-коммы** в многострочных списках (требование ktlint 1.8.0):

   ```kotlin
   val equipment = Equipment(
       id = 1L,
       name = "CNC",
       inventoryNumber = "INV-001",
       status = EquipmentStatus.ACTIVE,
       location = "Shop",  // ← запятая обязательна
   )
   ```

### Запрещено в тестах

- ❌ Использовать Mockito вместо MockK
- ❌ Писать тесты без `@ExtendWith(MockKExtension::class)`
- ❌ Оставлять моки без возврата значений
- ❌ Использовать wildcard-импорты (`import com.minierp.dto.*`)
- ❌ Писать тесты, зависящие от других тестов
- ❌ Удалять существующие тесты без обоснования

---

## 🐛 Частые проблемы и решения

### Проблема: `no answer found for ...`

**Причина**: не добавлена аннотация `@ExtendWith(MockKExtension::class)` к тестовому классу.

**Решение**:

```kotlin
@ExtendWith(MockKExtension::class)  // ← добавить
class WorkOrderServiceTest { ... }
```

### Проблема: `NullPointerException` в `WorkOrderResponse.from()`

**Причина**: мок `save()` возвращает объект без `id`.

**Решение**:

```kotlin
every { workOrderRepository.save(any()) } answers {
    firstArg<WorkOrder>().copy(id = 100L)  // ← явно присвоить ID
}
```

### Проблема: конфликт MockK и Mockito

**Причина**: `spring-boot-starter-test` тянет Mockito.

**Решение**: в `pom.xml` добавлен `<exclusion>`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-core</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### Проблема: `Unresolved reference: map` в тестах

**Причина**: компилятор Kotlin не может вывести тип из Java-коллекций.

**Решение**: явно указать тип переменной:

```kotlin
val orders: List<WorkOrder> = workOrderRepository.findAll()
return orders.map { WorkOrderResponse.from(it) }
```

### Проблема: тесты проходят локально, но падают в CI

**Причина**: разное окружение (версии JDK, зависимости).

**Решение**:

1. Выполнить `mvn clean install -U` локально
2. Проверить, что версии в `pom.xml` совпадают с актуальными
3. Убедиться, что в CI используется JDK 26

### Проблема: ktlint ругается на тестовые файлы

**Причина**: нарушения форматирования (wildcard-импорты, отсутствие trailing commas).

**Решение**:

```bash
mvn spotless:apply
```

---

## 🔄 CI и тесты

В GitHub Actions тесты запускаются автоматически при каждом `push` в `main` или `develop`, а также при создании Pull Request.

### Пайплайн CI

```yaml
- name: Build & Lint
  run: mvn -B clean install -DskipTests

- name: Run Tests
  run: mvn -B test
```

### Правила merge

- ❌ Если хотя бы один тест падает — merge в `main` **блокируется**
- ❌ Если ktlint выдаёт ошибки — merge **блокируется**
- ✅ Все тесты должны проходить со статусом `BUILD SUCCESS`

### Локальная проверка перед push

Перед отправкой кода на GitHub **обязательно** выполните:

```bash
mvn clean install   # сборка + тесты + линтер
```

Если команда завершилась с `BUILD SUCCESS` — можно делать push.

---

## 📋 Чек-лист при добавлении нового теста

- [ ] Тест изолирован (не зависит от других тестов)
- [ ] Используется MockK, а не Mockito
- [ ] Добавлена аннотация `@ExtendWith(MockKExtension::class)`
- [ ] Все моки возвращают валидные объекты (с `id`, не-null полями)
- [ ] Проверяется не только результат, но и вызовы моков (`verify`)
- [ ] Имя теста описывает сценарий по формуле `` `should_..._when_...` ``
- [ ] Используется структура AAA (Arrange / Act / Assert)
- [ ] Нет wildcard-импортов
- [ ] Есть trailing commas в многострочных списках
- [ ] Файл заканчивается переводом строки
- [ ] Тест проходит локально: `mvn test`
- [ ] Тест проходит в CI: `mvn -B test`
- [ ] Линтер не выдаёт ошибок: `mvn spotless:check`

---

## 🧩 Связь с другими документами

| Документ | Связь |
|---|---|
| `AI_GUIDELINES.md` | Правила написания тестов для AI-ассистентов |
| `ARCHITECTURE.md` | Описание слоёв, которые тестируются |
| `README.md` | Команды запуска тестов для пользователя |
| `pom.xml` | Конфигурация зависимостей для тестирования |

---

## 📊 Актуальные версии для тестирования

| Компонент | Версия |
|---|---|
| JDK | 26 |
| Kotlin | 2.4.0 |
| Spring Boot | 4.1.0 |
| MockK | 1.14.11 |
| JUnit 5 | (управляется Spring Boot) |
| Spotless | 3.6.0 |
| ktlint | 1.8.0 |

---

## 💡 Полезные команды

```bash
# Запуск всех тестов
mvn test

# Запуск конкретного класса
mvn test -Dtest=WorkOrderServiceTest

# Запуск конкретного метода
mvn test -Dtest=WorkOrderServiceTest#should\ create\ work\ order\ and\ update\ equipment\ status

# Проверка стиля кода
mvn spotless:check

# Автоформатирование
mvn spotless:apply

# Полная проверка перед коммитом
mvn clean install

# Очистка и обновление зависимостей
mvn clean install -U
```

---

## 📞 Поддержка

Если тесты не проходят:

1. Проверьте текст ошибки в консоли
2. Убедитесь, что установлена правильная версия JDK (26+)
3. Выполните `mvn clean install -U` для обновления зависимостей
4. Проверьте, что в `pom.xml` указаны актуальные версии
5. Обратитесь к разделу "Частые проблемы и решения" выше