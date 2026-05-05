# Курсовой проект «Автоматизация тестирования»

Проект содержит автотесты для сервиса покупки тура `aqa-shop.jar`.

## Используемые технологии

- Java 11
- Gradle
- JUnit 5
- Selenide
- JDBC
- MySQL / PostgreSQL
- Docker Compose
- Allure

## Подготовка

1. Склонировать репозиторий:

```bash
git clone <URL_ВАШЕГО_РЕПОЗИТОРИЯ>
cd aqa-shop-course
```

2. Положить файл `aqa-shop.jar` в корень проекта.

3. Запустить БД. Например, MySQL:

```bash
docker compose up -d mysql
```

Или PostgreSQL:

```bash
docker compose up -d postgres
```

4. Подготовить `application.properties`.

Для MySQL:

```bash
cp application-mysql.properties application.properties
```

Для PostgreSQL:

```bash
cp application-postgres.properties application.properties
```

5. Запустить приложение:

```bash
java -jar aqa-shop.jar
```

Приложение должно быть доступно по адресу `http://localhost:8080`.

## Запуск тестов с MySQL

```bash
./gradlew clean test \
  -Ddb.url=jdbc:mysql://localhost:3306/app \
  -Ddb.user=app \
  -Ddb.password=pass \
  -Ddb.type=mysql
```

## Запуск тестов с PostgreSQL

```bash
./gradlew clean test \
  -Ddb.url=jdbc:postgresql://localhost:5432/app \
  -Ddb.user=app \
  -Ddb.password=pass \
  -Ddb.type=postgres
```

## Allure-отчёт

После запуска тестов сформировать отчёт:

```bash
./gradlew allureReport
```

Открыть отчёт:

```bash
./gradlew allureServe
```

