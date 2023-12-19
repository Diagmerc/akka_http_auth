# API AKKA-HTTP Authetification

**Реализация api для регистрации и авторизации пользователя в сервисе.**

Для тестирования приложения возможно использовать команды:

_1. Код 200 и пустой body в случае успеха.
Если уже есть аккаунт с таким email возращается Код 422 и body c ошибкой {  "error": "session.errors.emailAlreadyRegistered" }_

+ curl --request POST --location 'http://localhost:8080/api_v1/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "mshevelevich@gmail.com",
    "password": "himmih1234",
    "name": "Миша"
}'

_2. В случае успешной авторизации возвращается пусто с кодом 200 c пустым body
В случае не успешной авторизации возвращается status: 422 и json-body с ошибкой._

+ curl --request POST --location 'http://localhost:8080/api_v1/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "mshevelevich@gmail.com",
    "password": "himmih1234"
}'


_3. В случае авторизованного пользователя возвращается код 200 и body:
{
  "id": реальное значение,    //  id пользователя
  "email": "mshevelevich@gmail.com", // email пользователя
  "created": дата создания,          //  дата создания
  "name": "Миша"    //  имя пользователя
}В случае неавторизованного пользователя код 401 и пустым body, для определения пользователя необходима передача токена в header_

+ curl --location --request GET 'http://localhost:8080/api_v1/me' \
--header 'Authorization: Basic bXNoZXZlbGV2aWNoQGdtYWlsLmNvbTpoaW1taWgxMjM0' 

_4. Всегда возвращает 200 и пустой body. Для определения пользователя необходима передача токена в header_

+ curl --location --request PUT 'http://localhost:8080/api_v1/logout' \
--header 'Authorization: Basic bXNoZXZlbGV2aWNoQGdtYWlsLmNvbTpoaW1taWgxMjM0'

## Тесты
![image](https://github.com/Diagmerc/akka_http_auth/assets/91744556/6dc40d04-f8cc-40c9-a1be-45ebcc3fca7d)
