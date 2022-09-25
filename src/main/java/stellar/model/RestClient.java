package stellar.model;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestClient {
    public static final String URL = "https://stellarburgers.nomoreparties.site/";
    public static final RequestSpecification spec = given()
            .baseUri(URL)
            .header("Content-type", "application/json");
}
