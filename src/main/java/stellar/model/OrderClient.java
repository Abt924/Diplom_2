package stellar.model;

import io.qameta.allure.Step;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.ValidatableResponse;
import stellar.model.pojo.*;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient{

    public static String ORDERS_PATH = "api/orders";
    public static String ALL_ORDERS_PATH = "api/orders/all";
    public static String INGREDIENTS_PATH = "api/ingredients";


    @Step("get all ingredients ")
    public ValidatableResponse getIngredients() {
        return given()
                .spec(spec)
                .when()
                .get(INGREDIENTS_PATH)
                .then();
    }


    @Step("Create Order, burger ingredients {ingredients} ")
    public ValidatableResponse createOrder(Ingredients ingredients) {
        return given()
                .spec(spec)
                .body(ingredients)
                .when()
                .post(ORDERS_PATH)
                .then();
    }
    @Step("Create Order, burger ingredients {ingredients} {auth}")
    public ValidatableResponse createOrderAuthorized(Ingredients ingredients, Authorized auth) {
        return given()
                .spec(spec)
                .header("Authorization", auth.getAccessToken())
                .body(ingredients)
                .when()
                .post(ORDERS_PATH)
                .then();
    }

    @Step("get orders of user {auth} ")
    public ValidatableResponse getAuthorizedUserOrders(Authorized auth) {
        return given()
                .spec(spec)
                .header("Authorization", auth.getAccessToken())
                .when()
                .get(ORDERS_PATH)
                .then();
    }

    @Step("get orders, unauthorized  ")
    public ValidatableResponse getUnauthorizedUserOrders() {
        return given()
                .spec(spec)
                .header("Authorization", "")
                .when()
                .get(ORDERS_PATH)
                .then();
    }

}
