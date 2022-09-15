
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import stellar.model.*;
import stellar.model.pojo.SuccessMessage;
import stellar.model.pojo.User;
import stellar.model.pojo.UserCreated;


public class CreateUserTest extends BaseApiTest {
    private User user;
    private UserCreated userCreated;
    private SuccessMessage successMessage;

    @Before
    public void setUp() {
        super.setUp();
        user = UserGenerator.createRandom();

    }

    @Test
    @DisplayName("Unique user can be created")
    @Description("Random unique user can be created")
    public void successfulCreateUser() {
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not CREATED", SC_OK, statusCode);
        userCreated = response.extract().body().as(UserCreated.class);
        assertTrue("Success field should be true", userCreated.isSuccess());

        System.out.println(userCreated.toString());
    }

    @Test
    @DisplayName("Dublicate user creating Forbidden")
    public void duplicateUserCreatingForbidden() {
        ValidatableResponse response = userClient.create(user);
        userCreated = response.extract().body().as(UserCreated.class);
        //Create dublicate
        response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not FORBIDDEN", SC_FORBIDDEN, statusCode);
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected",
                "User already exists", successMessage.getMessage());
    }

    @Test
    @DisplayName("user creating without email Forbidden")
    public void creatingWithoutEmailForbidden() {
        User withoutEmail = UserGenerator.noEmailRandom();
        ValidatableResponse response = userClient.create(withoutEmail);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not FORBIDDEN", SC_FORBIDDEN, statusCode);
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected",
                "Email, password and name are required fields", successMessage.getMessage());
    }

    @Test
    @DisplayName("user creating without password Forbidden")
    public void creatingWithoutPasswordForbidden() {
        User withoutPassword = UserGenerator.noPasswordRandom();
        ValidatableResponse response = userClient.create(withoutPassword);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not FORBIDDEN", SC_FORBIDDEN, statusCode);
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected",
                "Email, password and name are required fields", successMessage.getMessage());
    }

    @Test
    @DisplayName("user creating without name Forbidden")
    public void creatingWithoutNameForbidden() {
        User withoutName = UserGenerator.noNameRandom();
        ValidatableResponse response = userClient.create(withoutName);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is not FORBIDDEN", SC_FORBIDDEN, statusCode);
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected",
                "Email, password and name are required fields", successMessage.getMessage());
    }

    @After
    public void tearDown() {
        super.tearDown();
        // remove created user
        if (userCreated != null) {
            ValidatableResponse response = userClient.deleteUser(userCreated);
            System.out.println("Delete user status " + response.extract().statusCode());
            System.out.println(response.extract().body().asString());
        }

    }


}
