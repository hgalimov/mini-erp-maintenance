# 🏗 Архитектура проекта

Проект построен на основе **слоистой (Layered) архитектуры** с элементами чистой архитектуры. Код организован по принципу разделения ответственности и соответствует принципам **SOLID**.

---

## 📁 Структура исходного кода

```
mini-erp-maintenance/
├── .github/workflows/ci.yml # CI-пайплайн GitHub Actions
├── .gitignore
├── pom.xml # Конфигурация сборки (Maven)
├── README.md # Инструкция для пользователя
├── ARCHITECTURE.md # Этот файл
├── TESTING.md # Документация по тестированию
├── AI_GUIDELINES.md # Правила доработки через ИИ
└── src/
├── main/
│ ├── kotlin/com/minierp/
│ │ ├── MiniErpApplication.kt # Точка входа
│ │ ├── config/
│ │ │ └── DataInitializer.kt # Тестовые данные при старте
│ │ ├── controller/
│ │ │ ├── ErpController.kt # REST API (JSON)
│ │ │ └── WebController.kt # Веб-интерфейс (HTML через Thymeleaf)
│ │ ├── domain/
│ │ │ ├── Enums.kt # EquipmentStatus, WorkOrderStatus
│ │ │ ├── Equipment.kt # Сущность оборудования
│ │ │ ├── Technician.kt # Сущность мастера
│ │ │ └── WorkOrder.kt # Сущность наряд-заказа
│ │ ├── dto/
│ │ │ ├── Requests.kt # DTO входящих запросов
│ │ │ └── Responses.kt # DTO исходящих ответов
│ │ ├── exception/
│ │ │ └── GlobalExceptionHandler.kt # Глобальная обработка ошибок
│ │ ├── repository/
│ │ │ ├── EquipmentRepository.kt # Репозиторий оборудования
│ │ │ ├── TechnicianRepository.kt # Репозиторий мастеров
│ │ │ └── WorkOrderRepository.kt # Репозиторий наряд-заказов
│ │ └── service/
│ │ ├── EquipmentService.kt # Бизнес-логика оборудования
│ │ ├── TechnicianService.kt # Бизнес-логика мастеров
│ │ └── WorkOrderService.kt # Бизнес-логика наряд-заказов
│ └── resources/
│ ├── application.yml # Конфигурация Spring Boot
│ ├── templates/ # Thymeleaf HTML-шаблоны
│ │ ├── index.html # Главная страница
│ │ ├── equipment/
│ │ │ ├── list.html # Список оборудования
│ │ │ └── form.html # Форма создания/редактирования
│ │ ├── technician/
│ │ │ ├── list.html # Список мастеров
│ │ │ └── form.html # Форма создания
│ │ └── workorder/
│ │ ├── list.html # Список наряд-заказов
│ │ └── form.html # Форма создания
│ └── static/
│ └── css/
│ └── style.css # Единый стиль
└── test/kotlin/com/minierp/
├── controller/
│ └── EquipmentControllerTest.kt # Тесты контроллера
└── service/
└── WorkOrderServiceTest.kt # Тесты бизнес-логики
```

---

## 📐 Общая схема слоёв

```
┌─────────────────────────────────────────────────────┐
│ Клиент (браузер / curl / Postman) │
└──────────────────────┬──────────────────────────────┘
│
┌──────────────┴──────────────┐
│ │
▼ ▼
┌───────────────────┐ ┌───────────────────┐
│ WebController │ │ ErpController │
│ (HTML + Thymeleaf)│ │ (JSON REST API) │
│ /web/* │ │ /api/v1/* │
└─────────┬─────────┘ └─────────┬─────────┘
│ │
└─────────────┬─────────────┘
│ DTO / Model
▼
┌───────────────────────────────┐
│ Service Layer │
│ Бизнес-логика, транзакции │
└──────────────┬────────────────┘
│ Domain Entities
▼
┌───────────────────────────────┐
│ Repository Layer (JPA) │
│ Абстракция доступа к БД │
└──────────────┬────────────────┘
│ SQL / JPQL
▼
┌───────────────────────────────┐
│ Database (H2 in-memory) │
└───────────────────────────────┘
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

Два контроллера с чётким разделением ответственности:

| Контроллер | Тип | URL-префикс | Назначение |
|---|---|---|---|
| `ErpController` | `@RestController` | `/api/v1/*` | REST API, возвращает JSON |
| `WebController` | `@Controller` | `/web/*` | Веб-интерфейс, возвращает HTML через Thymeleaf |

**Важно**: оба контроллера используют одни и те же сервисы, но представляют данные по-разному.

### 5. DTO (`com.minierp.dto`)

Изоляция внутренней модели от внешнего API.

| Файл | Содержимое |
|---|---|
| `Requests.kt` | `CreateEquipmentRequest`, `CreateTechnicianRequest`, `CreateWorkOrderRequest`, `UpdateWorkOrderStatusRequest` |
| `Responses.kt` | `EquipmentResponse`, `TechnicianResponse`, `WorkOrderResponse` |

### 6. Exception (`com.minierp.exception`)

Глобальная обработка ошибок через `@RestControllerAdvice`. Возвращает единый формат `ErrorResponse` для API.

### 7. Config (`com.minierp.config`)

`DataInitializer` — заполняет БД тестовыми данными при первом запуске.

### 8. Templates (`src/main/resources/templates/`)

HTML-шаблоны Thymeleaf. Структурированы по сущностям:

| Путь | Назначение |
|---|---|
| `index.html` | Главная страница с меню |
| `equipment/list.html` | Таблица оборудования |
| `equipment/form.html` | Форма создания / редактирования статуса |
| `technician/list.html` | Таблица мастеров |
| `technician/form.html` | Форма создания мастера |
| `workorder/list.html` | Таблица наряд-заказов с кнопками смены статуса |
| `workorder/form.html` | Форма создания наряд-заказа |

### 9. Static Resources (`src/main/resources/static/`)

Статические файлы (CSS, изображения, JS). Spring Boot автоматически раздаёт их по корневым URL.

| Путь | Назначение |
|---|---|
| `css/style.css` | Единый стиль для всех страниц |

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

-
- При создании наряд-заказа оборудование переходит в статус `MAINTENANCE`
- При завершении (`COMPLETED`) оборудование возвращается в `ACTIVE`
- Завершённые и отменённые наряд-заказы нельзя изменить

---

## 🧱 Принципы SOLID

| Принцип | Где применён |
|---|---|
| **S** — Single Responsibility | Каждый класс отвечает за одну задачу. `ErpController` — API, `WebController` — GUI |
| **O** — Open/Closed | Новые endpoints без изменения существующих сервисов |
| **L** — Liskov Substitution | Репозитории заменяются моками в тестах |
| **I** — Interface Segregation | Репозитории разделены по сущностям |
| **D** — Dependency Inversion | Сервисы зависят от интерфейсов репозиториев |

---

## 🌐 URL-маршруты

### Веб-интерфейс (GUI)

| Метод | URL | Действие | Шаблон |
|---|---|---|---|
| GET | `/web/home` | Главная страница | `index.html` |
| GET | `/web/equipment` | Список оборудования | `equipment/list.html` |
| GET | `/web/equipment/new` | Форма создания | `equipment/form.html` |
| POST | `/web/equipment` | Создать оборудование | — (redirect) |
| GET | `/web/equipment/{id}/edit` | Форма редактирования | `equipment/form.html` |
| POST | `/web/equipment/{id}` | Обновить статус | — (redirect) |
| GET | `/web/technicians` | Список мастеров | `technician/list.html` |
| GET | `/web/technicians/new` | Форма создания | `technician/form.html` |
| POST | `/web/technicians` | Создать мастера | — (redirect) |
| POST | `/web/technicians/{id}/toggle` | Активировать/деактивировать | — (redirect) |
| GET | `/web/work-orders` | Список наряд-заказов | `workorder/list.html` |
| GET | `/web/work-orders/new` | Форма создания | `workorder/form.html` |
| POST | `/web/work-orders` | Создать наряд-заказ | — (redirect) |
| POST | `/web/work-orders/{id}/status` | Изменить статус | — (redirect) |

### REST API

| Метод | URL | Действие |
|---|---|---|
| GET | `/api/v1/equipment` | Список оборудования (JSON) |
| GET | `/api/v1/equipment/{id}` | Оборудование по ID (JSON) |
| POST | `/api/v1/equipment` | Создать оборудование (JSON) |
| PATCH | `/api/v1/equipment/{id}/status` | Изменить статус (JSON) |
| GET | `/api/v1/technicians` | Список мастеров (JSON) |
| POST | `/api/v1/technicians` | Создать мастера (JSON) |
| PATCH | `/api/v1/technicians/{id}/toggle-active` | Активировать/деактивировать (JSON) |
| GET | `/api/v1/work-orders` | Все наряд-заказы (JSON) |
| GET | `/api/v1/work-orders?status=CREATED` | Фильтр по статусу (JSON) |
| POST | `/api/v1/work-orders` | Создать наряд-заказ (JSON) |
| PATCH | `/api/v1/work-orders/{id}/status` | Изменить статус (JSON) |