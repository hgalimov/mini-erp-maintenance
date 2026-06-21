# 🔧 Mini ERP: Техобслуживание и ремонт

Минималистичная ERP-система для управления техобслуживанием и ремонтом оборудования. Реализована на **Kotlin + Spring Boot 4.1.0 + Java 26 + Maven** с использованием шаблонизатора **Thymeleaf**.

## 🎯 Описание функциональности

### Основные сущности

- **Оборудование** — станки, машины, устройства с инвентарными номерами, локациями и статусами (`ACTIVE`, `BROKEN`, `MAINTENANCE`, `DECOMMISSIONED`)
- **Мастера** — специалисты с указанием специализации и активности
- **Наряд-заказы** — заявки на ремонт с привязкой к оборудованию и мастеру, жизненный цикл: `CREATED → IN_PROGRESS → COMPLETED / CANCELLED`

### Возможности системы

- ✅ **Полноценный веб-интерфейс (GUI)** на Thymeleaf с операциями CRUD
- ✅ **REST API** для интеграции с другими системами
- ✅ Учёт оборудования и мастеров
- ✅ Создание наряд-заказов с автоматической сменой статуса оборудования
- ✅ Управление жизненным циклом наряд-заказа
- ✅ Встроенная база данных H2 с веб-консолью
- ✅ Автоматическое заполнение тестовыми данными при первом запуске

### Два интерфейса работы

| Интерфейс | Назначение | URL-префикс |
|---|---|---|
| 🖥 **Веб-интерфейс (GUI)** | Работа через браузер с HTML-формами | `/web/*` |
| 🔌 **REST API** | Интеграция с другими системами (JSON) | `/api/v1/*` |

---

## 📋 Требования

Для запуска приложения вам понадобится:

- **JDK 26+** — [скачать с Adoptium](https://adoptium.net/)
- **Apache Tomcat 10.1+** — [скачать с официального сайта](https://tomcat.apache.org/download-10.cgi)

Проверка установки JDK в командной строке:

```bash
java -version
```

Должно показать версию 26 или выше.

---

## 📦 Запуск приложения через Apache Tomcat

Приложение распространяется в виде файла **`mini-erp.war`** — это готовое веб-приложение, которое развёртывается на сервере Apache Tomcat.

### Шаг 1. Установите JDK 26

1. Скачайте JDK с [Adoptium](https://adoptium.net/)
2. Установите, следуя инструкциям установщика
3. При установке **обязательно** поставьте галочку **"Set JAVA_HOME variable"**
4. Перезагрузите компьютер
5. Проверьте установку:

   ```bash
   java -version
   ```

### Шаг 2. Установите Apache Tomcat

1. Перейдите на [официальный сайт Tomcat](https://tomcat.apache.org/download-10.cgi)
2. В разделе **Core** скачайте файл **`apache-tomcat-10.1.x.zip`** (для Windows)
3. Распакуйте архив в удобную папку без пробелов и русских букв в пути, например:

   ```
   C:\tomcat\
   ```

> 💡 **Важно**: не распаковывайте Tomcat в `C:\Program Files\` — там могут возникнуть проблемы с правами доступа.

### Шаг 3. Разверните WAR-файл

1. Скопируйте файл **`mini-erp.war`** в папку:

   ```
   C:\tomcat\webapps\
   ```

2. **Рекомендуется**: переименуйте файл в `ROOT.war`, чтобы приложение открывалось по адресу `http://localhost:8080/` без дополнительного пути:

   ```
   C:\tomcat\webapps\ROOT.war
   ```

   Если оставить имя `mini-erp.war`, приложение будет доступно по адресу:

   ```
   http://localhost:8080/mini-erp/
   ```

### Шаг 4. Запустите Tomcat

**Для Windows:**

1. Откройте папку `C:\tomcat\bin\`
2. Запустите файл **`startup.bat`** двойным щелчком
3. Откроется новое окно с логами сервера — **не закрывайте его**, пока приложение работает

**Для Linux / macOS:**

```bash
cd /path/to/tomcat/bin
chmod +x *.sh
./startup.sh
```

Дождитесь появления в логах строки:

```
Server startup in [X] milliseconds
```

### Шаг 5. Проверьте работу

Откройте браузер и перейдите по адресу:

- Если переименовали в `ROOT.war`: **http://localhost:8080/web/home**
- Если оставили `mini-erp.war`: **http://localhost:8080/mini-erp/web/home**

Вы должны увидеть главное меню приложения с тремя разделами:

- ⚙️ **Оборудование**
- 👷 **Мастера**
- 📋 **Наряд-заказы**

> 💡 При первом запуске приложение автоматически создаёт тестовые данные: 2 единицы оборудования, 2 мастера и 1 наряд-заказ.

---

## 🛑 Остановка сервера

**Для Windows:**

Запустите файл **`shutdown.bat`** в папке `C:\tomcat\bin\` или закройте окно с логами Tomcat.

**Для Linux / macOS:**

```bash
cd /path/to/tomcat/bin
./shutdown.sh
```

---

## 🖥 Веб-интерфейс (GUI)

Приложение предоставляет полноценный веб-интерфейс на базе **Thymeleaf** с операциями CRUD (создание, чтение, изменение) через HTML-формы.

### Основные страницы

| Ресурс | URL (если `ROOT.war`) | URL (если `mini-erp.war`) |
|---|---|---|
| 🏠 Главная страница | http://localhost:8080/web/home | http://localhost:8080/mini-erp/web/home |
| ⚙️ Список оборудования | http://localhost:8080/web/equipment | http://localhost:8080/mini-erp/web/equipment |
| ⚙️ Создать оборудование | http://localhost:8080/web/equipment/new | http://localhost:8080/mini-erp/web/equipment/new |
| 👷 Список мастеров | http://localhost:8080/web/technicians | http://localhost:8080/mini-erp/web/technicians |
| 👷 Создать мастера | http://localhost:8080/web/technicians/new | http://localhost:8080/mini-erp/web/technicians/new |
| 📋 Список наряд-заказов | http://localhost:8080/web/work-orders | http://localhost:8080/mini-erp/web/work-orders |
| 📋 Создать наряд-заказ | http://localhost:8080/web/work-orders/new | http://localhost:8080/mini-erp/web/work-orders/new |
| 🗄 Консоль базы данных H2 | http://localhost:8080/h2-console | http://localhost:8080/mini-erp/h2-console |

### Возможности GUI

- **Оборудование**: просмотр списка, создание нового, изменение статуса
- **Мастера**: просмотр списка, создание нового, активация/деактивация
- **Наряд-заказы**: просмотр списка, создание нового, смена статуса (`В работу` / `Завершить` / `Отменить`)

---

## 📡 REST API

REST API предназначен для интеграции с другими системами и возвращает данные в формате JSON.

### Equipment (Оборудование)

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/v1/equipment` | Список всего оборудования |
| GET | `/api/v1/equipment/{id}` | Оборудование по ID |
| POST | `/api/v1/equipment` | Создать оборудование |
| PATCH | `/api/v1/equipment/{id}/status?status=...` | Изменить статус |

### Technician (Мастера)

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/v1/technicians` | Список мастеров |
| POST | `/api/v1/technicians` | Создать мастера |
| PATCH | `/api/v1/technicians/{id}/toggle-active` | Активировать/деактивировать |

### WorkOrder (Наряд-заказы)

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/v1/work-orders` | Все наряд-заказы |
| GET | `/api/v1/work-orders?status=CREATED` | Фильтр по статусу |
| POST | `/api/v1/work-orders` | Создать наряд-заказ |
| PATCH | `/api/v1/work-orders/{id}/status` | Изменить статус |

### Примеры запросов через curl

> 💡 **Важно**: если WAR-файл **не** переименован в `ROOT.war`, добавьте префикс `/mini-erp` ко всем URL ниже. Например: `http://localhost:8080/mini-erp/api/v1/equipment`.

**Создать оборудование:**

```bash
curl -X POST http://localhost:8080/api/v1/equipment \
  -H "Content-Type: application/json" \
  -d '{"name":"Станок ЧПУ","inventoryNumber":"CNC-001","status":"ACTIVE","location":"Цех 1"}'
```

**Создать мастера:**

```bash
curl -X POST http://localhost:8080/api/v1/technicians \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Иван Петров","specialization":"Механик","isActive":true}'
```

**Создать наряд-заказ:**

```bash
curl -X POST http://localhost:8080/api/v1/work-orders \
  -H "Content-Type: application/json" \
  -d '{"equipmentId":1,"technicianId":1,"description":"Замена масла"}'
```

**Получить список оборудования:**

```bash
curl http://localhost:8080/api/v1/equipment
```

**Отфильтровать наряд-заказы по статусу:**

```bash
curl "http://localhost:8080/api/v1/work-orders?status=CREATED"
```

---

## 🗄 Консоль базы данных H2

Для прямого просмотра данных в базе откройте консоль H2:

- **URL**: `http://localhost:8080/h2-console` (или `http://localhost:8080/mini-erp/h2-console`)
- **JDBC URL**: `jdbc:h2:mem:erpdb`
- **User Name**: `sa`
- **Password**: *(оставьте пустым)*

---

## 🔄 Обновление приложения

Для обновления на новую версию:

**Для Windows:**

```cmd
C:\tomcat\bin\shutdown.bat
del C:\tomcat\webapps\mini-erp.war
rmdir /s /q C:\tomcat\webapps\mini-erp
copy mini-erp.war C:\tomcat\webapps\
C:\tomcat\bin\startup.bat
```

**Для Linux / macOS:**

```bash
./shutdown.sh
rm -f /path/to/tomcat/webapps/mini-erp.war
rm -rf /path/to/tomcat/webapps/mini-erp
cp mini-erp.war /path/to/tomcat/webapps/
./startup.sh
```

---

## 🔄 CI/CD

При каждом `push` в ветки `main` или `develop`, а также при создании Pull Request, GitHub Actions автоматически:

1. Поднимает виртуальную машину Ubuntu
2. Устанавливает JDK 26 и Maven
3. Запускает сборку проекта с проверкой стиля кода
4. Запускает все тесты

> 🐧 **Примечание**: CI запускается на Ubuntu, даже если вы работаете на Windows. Это нормально — GitHub Actions использует облачные виртуальные машины.

---

## 📄 Лицензия

MIT License. Свободное использование в учебных и коммерческих целях.

---

## 👤 Автор

Проект реализован на стеке **Kotlin 2.4.0 + Spring Boot 4.1.0 + Java 26 + Thymeleaf + Maven**.