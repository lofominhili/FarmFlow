# FarmFlow

## Обзор

Сервис является RESTful API позволяющий автоматизировать учет продукции для небольшой фермы.
Согласно ТЗ реализованы все основные требования и все дополнительные задания.

### Cервис реализует (основные требования):

* регистрация нового пользователя;

* Вход пользователя в систему;

* Блокировка пользователя;

* регистрация нового продукта;

* Добавление собранных продуктов в общую статистику;

* Получение статистики по произведенным товарам (за конкретный день, неделю или месяц) по конкретному человеку;

* Получение статистики по произведенным товарам (за конкретный день, неделю или месяц) по фирме в целом;

### Cервис реализует (дополнительные задания):

* Получение отчета на почту по собранным товарам;

* Выставление оценки работникам;

* Задать норму сбора урожая;

### Использованные технологии

* Java 17

* Spring Boot

* Spring Web

* Spring Data Jpa

* Spring Security

* Spring Validation

* Spring Mail

* Postgresgl

* Liquibase

* Swagger

* Mapstruct

* Docker

* Maven

## Конфигурация

Для корректной работы приложения необходимо настроить Spring Mail.
Для этого нужно передать environment параметры:

* `MAIL_HOST`

* `MAIL_PORT`

* `MAIL_USERNAME`

* `MAIL_PASSWORD`

* `STARTTLS_ENABLE`

Также нужно передать параметры:

* `ADMIN_EMAIL` - адрес, на который будут приходить отчеты.

* `SECRET_KEY` - Ключ для подписи токенов.

## Запуск

Склонировать репозиторий, выполнив команду: `git clone https://github.com/lofominhili/FarmFlow`

Добавить environment переменные в сервисе app файла `compose.yaml`.

Написать в терминале команду `mvn clean package`

Написать в терминале команду `docker-compose up`

## Endpoints

Все параметры на вход Rest-запросов и ответы этих запросов можно посмотреть в swagger

### Регистрация нового пользователя

` POST /auth/register-user `

---

### Вход пользователя в систему

` POST /auth/sign-in `

---

### Блокировка выбранного пользователя

` POST /admin/block/{email} `

---

### Выставление оценки работнику

` POST /admin/rate `

---

### Задать норму сбора урожая

` POST /admin/set-harvest-rate `

---

### Регистрация нового продукта

` POST /product/register-product `

---

### Добавление собранных продуктов в общую статистику;

` POST /product/add-collected-product`

---

### Получение статистики по произведенным товарам (за конкретный день, неделю или месяц) по конкретному человеку

` GET /admin/get-statistics-by-user`

---

### Получение статистики по произведенным товарам (за конкретный день, неделю или месяц) по фирме в целом;

` GET /admin/get-statistics-by-farm`

---

### Swagger ui

` GET /swagger-ui/index.html`