package stellar.model;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import stellar.model.pojo.Credentials;
import stellar.model.pojo.User;
import stellar.model.pojo.UserCreated;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient{

    public static String REGISTER_PATH = "api/auth/register";
    public static String LOGIN_PATH = "api/auth/login";
    public static String LOGOUT_PATH = "api/auth/logout";
    public static String REFRESH_TOKEN_PATH = "api/auth/token";
    public static String USER_PATH = "api/auth/user";


    @Step("create User {user} ")
    public ValidatableResponse create(User user) {
        return given()
                .spec(spec)
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    @Step("login credentials {credentials} ")
    public ValidatableResponse login(Credentials credentials) {
        System.out.println(credentials);
        return given()
                .spec(spec)
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("logout ")
    public ValidatableResponse logout(String refreshToken) {
        String json = String.format("{ \"token\" : \"%s\" }", refreshToken);
        return given()
                .log().all()
                .spec(spec)
                .body(json)
                .when()
                .post(LOGOUT_PATH)
                .then()
                ;
    }

    @Step("refresh token with credentials {credentials} ")
    public ValidatableResponse refreshToken(Credentials credentials) {
        return given()
                .spec(spec)
                .body(credentials)
                .when()
                .post(REFRESH_TOKEN_PATH)
                .then();
    }

    @Step("delete user {userCreated} ")
    public ValidatableResponse deleteUser(UserCreated userCreated) {
        return given()
                .spec(spec)
                .header("Authorization", userCreated.getAccessToken())
                .when()
                .delete(USER_PATH)
                .then();
    }

}
