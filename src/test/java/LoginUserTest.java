import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import stellar.model.*;
import stellar.model.pojo.*;

public class LoginUserTest extends BaseApiTest {

    private User user;
    private UserCreated userCreated;
    private SuccessMessage successMessage;
    private Authorized authorized;

    @Before
    public void setUp() {
        super.setUp();
        user = UserGenerator.createRandom();
        ValidatableResponse response = userClient.create(user);
        userCreated = response.extract().body().as(UserCreated.class);
    }

    @Test
    @DisplayName("Successful user login")
    @Description("Successful user login return OK and successful=true")
    public void successfulLogin() {
        ValidatableResponse response = userClient.login(user.getCredentials());

        System.out.println(response.extract().body().asString());
        assertEquals("Status code is not OK", SC_OK, response.extract().statusCode());
        authorized = response.extract().body().as(Authorized.class);
        assertTrue("Success field should be true", authorized.isSuccess());
    }

    @Test
    @DisplayName("Login with wrong email")
    @Description("Login with wrong email return UNAUTHORIZED")
    public void loginWithWrongEmail() {
        ValidatableResponse response = userClient.login(new Credentials("wrong@e.mail", user.getPassword()));

        System.out.println(response.extract().body().asString());
        assertEquals("Status code is not UNAUTHORIZED", SC_UNAUTHORIZED, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected",
                successMessage.getMessage(), "email or password are incorrect");
    }

    @Test
    @DisplayName("Login with wrong password")
    @Description("Login with wrong passord return UNAUTHORIZED")
    public void loginWithWrongPassword() {
        ValidatableResponse response = userClient.login(new Credentials(user.getEmail(), "wrong password"));

        System.out.println(response.extract().body().asString());
        assertEquals("Status code is not UNAUTHORIZED", SC_UNAUTHORIZED, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected",
                successMessage.getMessage(), "email or password are incorrect");
    }

    @After
    public void tearDown() {
        super.tearDown();
        // logout
        if (authorized != null && authorized.isSuccess()) {
            ValidatableResponse res = userClient.logout(authorized.getRefreshToken());
        }
        // remove created user
        if (userCreated != null && userCreated.isSuccess()) {
            ValidatableResponse response = userClient.deleteUser(userCreated);
        }

    }

}
