package stellar.test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import stellar.model.UserGenerator;
import stellar.model.pojo.Authorized;
import stellar.model.pojo.Credentials;
import stellar.model.pojo.SuccessMessage;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class LoginUserTest extends BaseApiTest {

    private SuccessMessage successMessage;

    @Before
    public void setUp() {
        super.setUp();
        user = UserGenerator.createRandom();
        userCreated = userClient.createUser(user);
    }

    @Test
    @DisplayName("Successful user login")
    @Description("Successful user login return OK and successful=true")
    public void successfulLogin() {
        ValidatableResponse response = userClient.login(user.getCredentials());

        assertEquals("Status code is not OK", SC_OK, response.extract().statusCode());
        authorized = response.extract().body().as(Authorized.class);
        assertTrue("Success field should be true", authorized.isSuccess());
    }

    @Test
    @DisplayName("Login with wrong email")
    @Description("Login with wrong email return UNAUTHORIZED")
    public void loginWithWrongEmail() {
        ValidatableResponse response = userClient.login(new Credentials("wrong@e.mail", user.getPassword()));

        assertEquals("Status code is not UNAUTHORIZED", SC_UNAUTHORIZED, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected", successMessage.getMessage(), "email or password are incorrect");
    }

    @Test
    @DisplayName("Login with wrong password")
    @Description("Login with wrong password return UNAUTHORIZED")
    public void loginWithWrongPassword() {
        ValidatableResponse response = userClient.login(new Credentials(user.getEmail(), "wrong password"));

        assertEquals("Status code is not UNAUTHORIZED", SC_UNAUTHORIZED, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected", successMessage.getMessage(), "email or password are incorrect");
    }
}
