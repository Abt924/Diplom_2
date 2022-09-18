package stellar.test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellar.model.UserGenerator;
import stellar.model.pojo.*;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetUserOrdersTest extends BaseApiTest {

    private IngredientsGenerator ingredientsGenerator;

    private SuccessMessage successMessage;


    @Before
    public void setUp() {
        super.setUp();
        ingredientsGenerator = new IngredientsGenerator();

    }

    @Test
    @DisplayName("get Orders Unauthorized")
    @Description("It should return UNAUTHORIZED")
    public void getOrdersUnauthorized() {

        ValidatableResponse response = orderClient.getUnauthorizedUserOrders();

        assertEquals("Status code is not UNAUTHORIZED", SC_UNAUTHORIZED, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());

        assertEquals("Message is not the same as expected",
                "You should be authorised", successMessage.getMessage());

    }

    @Test
    @DisplayName("get User Orders")
    @Description("Should return Ok, success and user Orders")
    public void getUserOrders() {
        //create user and login
        user = UserGenerator.createRandom();
        userCreated = userClient.createUser(user);
        authorized = userClient.loginUser(user);
        //create orders
        int orderCount = 1;
        new OrderCreator(orderClient, ingredientsGenerator, authorized).createFewOrders(orderCount);

        ValidatableResponse rsp = orderClient.getAuthorizedUserOrders(authorized);

        assertEquals("Status code is not OK", SC_OK, rsp.extract().statusCode());
        UserOrders userOrders = rsp.extract().body().as(UserOrders.class);
        assertTrue("Success field should be true", userOrders.isSuccess());
        assertTrue("Orders expected", userOrders.getOrders() != null);

    }

    @Test
    @DisplayName("check User Orders")
    @Description("Orders list size, total, total today should be equal to created count")
    public void checkUserOrdersCount() {
        //create user and login
        user = UserGenerator.createRandom();
        userCreated = userClient.createUser(user);
        authorized = userClient.loginUser(user);
        //try to create orders
        int orderCount = 5;
        int createdCount = new OrderCreator(orderClient, ingredientsGenerator, authorized)
                .createFewOrders(orderCount);

        UserOrders userOrders = orderClient
                .getAuthorizedUserOrders(authorized)
                .extract()
                .body()
                .as(UserOrders.class);

        assertEquals("Size of orders list not equals to created orders count",
                createdCount, userOrders.getOrders().size());
        assertEquals("Orders counter total not equals to created orders count",
                Integer.valueOf(createdCount), userOrders.getTotal());
        assertEquals("Orders counter totalToday not equals to created orders count",
                Integer.valueOf(createdCount), userOrders.getTotalToday());

    }

}
