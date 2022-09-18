package stellar.test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellar.model.UserGenerator;
import stellar.model.pojo.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class OrderCreateTest extends BaseApiTest {

    private IngredientsGenerator ingredientsGenerator;
    private SuccessMessage successMessage;

    @Before
    public void setUp() {
        super.setUp();
        ingredientsGenerator = new IngredientsGenerator();

    }


    @Test
    @DisplayName("create Order without authorization")
    @Description("It should return OK, success, name and order number")
    public void createOrder() {
        Ingredients stack = ingredientsGenerator.randomIngredients(2);

        orderClient.createOrder(stack)
                .assertThat()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());

    }

    @Test
    @DisplayName("create Order by authorized user")
    @Description("It should return OK, success, name and order structure")
    public void createOrderAuthorized() {
        User user = UserGenerator.createRandom();
        userCreated = userClient.createUser(user);
        Authorized authorized = userClient.loginUser(user);

        Ingredients stack = ingredientsGenerator.randomIngredients(2);

        orderClient.createOrderAuthorized(stack, authorized)
                .assertThat()
                .statusCode(not(equalTo(429)))
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());

    }

    @Test
    @DisplayName("Check created order ingredients")
    @Description("create Order by authorized and check Ingredients Ids")
    public void checkCreatedOrderIngredients() {
        User user = UserGenerator.createRandom();
        userCreated = userClient.createUser(user);
        Authorized authorized = userClient.loginUser(user);

        Ingredients stack = ingredientsGenerator.randomIngredients(5);

        ValidatableResponse response = orderClient.createOrderAuthorized(stack, authorized);
        response.assertThat()
                .statusCode(not(equalTo(429)))
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());

        List<String> orderIngredientsIds = response.
                extract()
                .body()
                .as(SuccessOrder.class)
                .getOrder()
                .getIngredients()
                .stream()
                .map(ingredient -> ingredient.get_id())
                .collect(Collectors.toList());

        assertEquals("Ingredients ids in order should be the same",
                stack.getIngredients(), orderIngredientsIds);


    }

    @Test
    @DisplayName("Check created order owner")
    @Description("create Order by authorized user and check owner==user")
    public void checkCreatedOrderOwner() {
        User user = UserGenerator.createRandom();
        userCreated = userClient.createUser(user);
        Authorized authorized = userClient.loginUser(user);

        Ingredients stack = ingredientsGenerator.randomIngredients(1);

        ValidatableResponse response = orderClient.createOrderAuthorized(stack, authorized);
        response.assertThat()
                .statusCode(not(equalTo(429)))
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());

        UserInOrder userInOrder = response.
                extract()
                .body()
                .as(SuccessOrder.class)
                .getOrder()
                .getOwner();

        assertEquals(" Owner name should be as the user name ",
                user.getName(), userInOrder.getName());

        assertEquals(" Owner email should be as the user email ",
                user.getEmail(), userInOrder.getEmail());

    }

    @Test
    public void createEmpty() {
        Ingredients stack = ingredientsGenerator.empty();

        ValidatableResponse response = orderClient.createOrder(stack);
        assertEquals("Status code is not SC_BAD_REQUEST", SC_BAD_REQUEST, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected",
                "Ingredient ids must be provided", successMessage.getMessage());

    }

    @Test
    public void createWithInvalidId() {
        Ingredients stack = ingredientsGenerator.stackWithOneInvalidId(3);

        ValidatableResponse response = orderClient.createOrder(stack);
        assertEquals("Status code is not INTERNAL_SERVER_ERROR", SC_INTERNAL_SERVER_ERROR, response.extract().statusCode());
        assertTrue("Should contains Internal Server Error",
                response.extract().body().asString().contains("Internal Server Error"));

    }


}
