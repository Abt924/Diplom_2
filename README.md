# Diplom_2
Тестирование API приложения StellarBurger
https://stellarburgers.nomoreparties.site

- stellar.model - модели api клиентов
- stellar.model.pojo - вспомогательные pojo классы

Тестовые данные
- stellar.model.UserGenerator
- stellar.model.IngredientGenerator
- stellar.model.OrderCreator

Тесты stellar.test:
- CreateUserTest - тесты регистрации пользователя
- LoginUserTest - тесты логина пользователя
- UserUpdateTest - тесты измениия профиля пользователя
- GetUserOrdersTest - тесты получения заказов пользователя
- OrderCreateTest - тесты создания заказа

target/allure-results - отчет Allure