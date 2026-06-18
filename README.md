# Mini ERP: Техобслуживание и ремонт

Минималистичная ERP-система с **веб-интерфейсом** для управления техобслуживанием и ремонтом оборудования.

## Функциональность
- 🖥️ **Веб-интерфейс** на Thymeleaf + Bootstrap 5
- ⚙️ CRUD оборудования и мастеров
- 📋 Наряд-заказы с автоматической сменой статусов
- 🔌 **REST API** (`/api/v1/*`) для интеграций
- 🗄️ Встроенная H2 БД с веб-консолью

## Технологии
- Java 26 · Kotlin 2.1 · Spring Boot 3.4
- Thymeleaf + Bootstrap 5 (GUI)
- JPA + H2 (БД)
- Maven, ktlint (Spotless), JUnit 5 + MockK
- GitHub Actions CI

## Быстрый старт (для разработчика)

### Требования
- JDK 26+ (рекомендуется [Eclipse Temurin](https://adoptium.net/))
- Apache Maven 3.9+

### Запуск в режиме разработки
```bash
git clone <repo> && cd mini-erp-maintenance
mvn clean install          # сборка + линтер + тесты
mvn spring-boot:run        # запуск