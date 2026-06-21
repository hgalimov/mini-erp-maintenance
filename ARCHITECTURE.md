# 🏗 Архитектура проекта

Проект построен на основе **слоистой (Layered) архитектуры** с элементами чистой архитектуры. Код организован по принципу разделения ответственности и соответствует принципам **SOLID**.

---

## 📁 Структура исходного кода

```
mini-erp-maintenance/
├── .github/workflows/ci.yml   # CI-пайплайн GitHub Actions
├── .gitignore
├── pom.xml                    # Конфигурация сборки (Maven)
├── README.md                  # Инструкция для пользователя
├── ARCHITECTURE.md            # Этот файл
├── TESTING.md                 # Документация по тестированию
├── AI_GUIDELINES.md           # Правила доработки через ИИ
└── src/
    ├── main/
    │   ├── kotlin/com/minierp/
    │   │   ├── MiniErpApplication.kt          # Точка входа
    │   │   ├── config/
    │   │   │   └── DataInitializer.kt         # Тестовые данные при старте
    │   │   ├── controller/
    │   │   │   └── ErpController.kt           # REST-контроллеры
    │   │   ├── domain/
    │   │   │   ├── Enums.kt                   # EquipmentStatus, WorkOrderStatus
    │   │   │   ├── Equipment.kt               # Сущность оборудования
    │   │   │   ├── Technician.kt              # Сущность мастера
    │   │   │   └── WorkOrder.kt               # Сущность наряд-заказа
    │   │   ├── dto/
    │   │   │   ├── Requests.kt                # DTO входящих запросов
    │   │   │   └── Responses.kt               # DTO исходящих ответов
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.kt  # Глобальная обработка ошибок
    │   │   ├── repository/
    │   │   │   ├── EquipmentRepository.kt     # Репозиторий оборудования
    │   │   │   ├── TechnicianRepository.kt    # Репозиторий мастеров
    │   │   │   └── WorkOrderRepository.kt     # Репозиторий наряд-заказов
    │   │   └── service/
    │   │       ├── EquipmentService.kt        # Бизнес-логика оборудования
    │   │       ├── TechnicianService.kt       # Бизнес-логика мастеров
    │   │       └── WorkOrderService.kt        # Бизнес-логика наряд-заказов
    │   └── resources/
    │       ├── application.yml                # Конфигурация Spring Boot
    │       └── static/                        # Веб-интерфейс (HTML/CSS)
    │           ├── index.html                 # Главная страница
    │           ├── equipment.html             # Страница оборудования
    │           ├── technicians.html           # Страница мастеров
    │           ├── work-orders.html           # Страница наряд-заказов
    │           └── style.css                  # Единый стиль
    └── test/kotlin/com/minierp/
        ├── controller/
        │   └── EquipmentControllerTest.kt     # Тесты контроллера
        └── service/
            └── WorkOrderServiceTest.kt        # Тесты бизнес-логики
```

---

## 📐 Общая схема слоёв

```
┌─────────────────────────────────────────────┐
│            Клиент (браузер / curl)           │
└──────────────────┬──────────────────────────┘
                   │ HTTP (JSON)
                   ▼
┌─────────────────────────────────────────────┐
│       Controller Layer (@RestController)     │
│       Приём запросов, валидация, маршрутизация│
└──────────────────┬──────────────────────────┘
                   │ DTO (Request / Response)
                   ▼
┌─────────────────────────────────────────────┐
│          Service Layer (@Service)            │
│          Бизнес-логика, транзакции           │
└──────────────────┬──────────────────────────┘
                   │ Domain Entities
                   ▼
┌─────────────────────────────────────────────┐
│       Repository Layer (JpaRepository)       │
│       Абстракция доступа к БД                │
└──────────────────┬──────────────────────────┘
                   │ SQL / JPQL
                   ▼
┌─────────────────────────────────────────────┐
│       Database (H2 in-memory)                │
└─────────────────────────────────────────────┘
```

---

## 📦 Модули и их роли

### 1. Domain (`com.minierp.domain`)

Ядро бизнес-модели. Содержит JPA-сущности и enum-ы. Не зависит от фреймворков, кроме JPA-аннотаций.

| Файл | Описание |
|---|---|
| `Enums.kt` | `EquipmentStatus`, `WorkOrderStatus` |
| `Equipment.kt` | Сущность оборудования |
| `Technician.kt` | Сущность мастера |
| `WorkOrder.kt` | Сущность наряд-заказа |

### 2. Repository (`com.minierp.repository`)

Интерфейсы, наследующие `JpaRepository`. Spring Data автоматически генерирует реализации.

| Файл | Кастомные методы |
|---|---|
| `EquipmentRepository` | `existsByInventoryNumber()` |
| `TechnicianRepository` | Стандартные CRUD |
| `WorkOrderRepository` | `findAllByStatus()` |

### 3. Service (`com.minierp.service`)

Бизнес-логика: валидация правил, управление статусами, транзакции.

| Файл | Ответственность |
|---|---|
| `EquipmentService` | CRUD оборудования, смена статусов |
| `TechnicianService` | CRUD мастеров, активация/деактивация |
| `WorkOrderService` | Создание наряд-заказов, переходы статусов |

### 4. Controller (`com.minierp.controller`)

HTTP-шлюз. Принимает запросы, делегирует сервисам, возвращает ответы. Не содержит бизнес-логики.

### 5. DTO (`com.minierp.dto`)

Изоляция внутренней модели от внешнего API.

| Файл | Содержимое |
|---|---|
| `Requests.kt` | `CreateEquipmentRequest`, `CreateTechnicianRequest`, `CreateWorkOrderRequest`, `UpdateWorkOrderStatusRequest` |
| `Responses.kt` | `EquipmentResponse`, `TechnicianResponse`, `WorkOrderResponse` |

### 6. Exception (`com.minierp.exception`)

Глобальная обработка ошибок через `@RestControllerAdvice`. Возвращает единый формат `ErrorResponse`.

### 7. Config (`com.minierp.config`)

`DataInitializer` — заполняет БД тестовыми данными при первом запуске.

### 8. Static Resources (`src/main/resources/static/`)

Веб-интерфейс на чистом HTML/CSS. Spring Boot автоматически раздаёт эти файлы по корневому URL.

---

## 🔄 Жизненный цикл наряд-заказа

```
   ┌──────────┐     ┌─────────────┐     ┌───────────┐
   │ CREATED  │────▶│ IN_PROGRESS │────▶│ COMPLETED │
   └──────────┘     └─────────────┘     └───────────┘
        │
        │              ┌───────────┐
        └─────────────▶│ CANCELLED │
                       └───────────┘
```

- При создании наряд-заказа оборудование переходит в статус `MAINTENANCE`
- При завершении (`COMPLETED`) оборудование возвращается в `ACTIVE`
- Завершённые и отменённые наряд-заказы нельзя изменить

---

## 🧱 Принципы SOLID

| Принцип | Где применён |
|---|---|
| **S** — Single Responsibility | Каждый класс отвечает за одну задачу |
| **O** — Open/Closed | Новые endpoints без изменения существующих сервисов |
| **L** — Liskov Substitution | Репозитории заменяются моками в тестах |
| **I** — Interface Segregation | Репозитории разделены по сущностям |
| **D** — Dependency Inversion | Сервисы зависят от интерфейсов репозиториев |