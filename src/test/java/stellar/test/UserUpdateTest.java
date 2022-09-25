package stellar.test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import stellar.model.UserGenerator;
import stellar.model.pojo.EmailName;
import stellar.model.pojo.SuccessMessage;
import stellar.model.pojo.SuccessUser;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserUpdateTest extends BaseApiTest {

    private SuccessMessage successMessage;
    private SuccessUser successUser;

    @Before
    public void setUp() {
        super.setUp();
        user = UserGenerator.createRandom();
        userCreated = userClient.createUser(user);

        anotherUser = UserGenerator.createRandom();
        anotherCreatedUser = userClient.createUser(anotherUser);
    }

    @Test
    @DisplayName("Get user test")
    @Description("Get user return user the same as created")
    public void getUser() {
        ValidatableResponse response = userClient.getUser(userCreated);

        assertEquals("Status code is not OK", SC_OK, response.extract().statusCode());
        successUser = response.extract().body().as(SuccessUser.class);
        assertTrue("Success field should be true", successUser.isSuccess());

        assertEquals(userCreated.getUser().getName(), successUser.getUser().getName());
        assertEquals(userCreated.getUser().getEmail(), successUser.getUser().getEmail());
    }

    @Test
    @DisplayName("successful Update Authorized User")
    @Description("successful Update authorized user return updated name and email")
    public void successfulUpdateUser() {
        //authorize
        authorized = userClient.loginUser(user);
        //new user data : new email and new name
        EmailName newEmailName = UserGenerator.emailNameRandom();
        //update
        ValidatableResponse response = userClient.updateUser(userCreated, newEmailName);

        assertEquals("Status code is not OK", SC_OK, response.extract().statusCode());
        successUser = response.extract().body().as(SuccessUser.class);
        assertTrue("Success field should be true", successUser.isSuccess());

        assertEquals(newEmailName.getName(), successUser.getUser().getName());
        assertEquals(newEmailName.getEmail(), successUser.getUser().getEmail());
    }

    @Test
    @DisplayName("Update unauthorized user")
    @Description("Update unauthorized user return UNAUTHORIZED and don't change user data")
    public void unauthorizedUserUpdate401() {
        //no authorization
        EmailName newEmailName = UserGenerator.emailNameRandom();
        //update
        ValidatableResponse response = userClient.updateUser(userCreated, newEmailName);

        assertEquals("Status code is not UNAUTHORIZED", SC_UNAUTHORIZED, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());
        assertEquals("Message is not the same as expected", "You should be authorised", successMessage.getMessage());
    }

    @Test
    @DisplayName("Unauthorized user Update should not change user data")
    @Description("User's email and name  should remain the same")
    public void unauthorizedUserUpdateNotChangeUserData() {
        //no authorize
        EmailName expectedEmailName = new EmailName(userCreated.getUser().getEmail(), userCreated.getUser().getName());
        EmailName newEmailName = UserGenerator.emailNameRandom();
        //update
        userClient.updateUser(userCreated, newEmailName);

        //get User and check that email and name remain the same
        ValidatableResponse response = userClient.getUser(userCreated);
        successUser = response.extract().body().as(SuccessUser.class);

        assertEquals("Unauthorized user Update should not change user email and name", expectedEmailName, successUser.getUser());
    }

    @Test
    @DisplayName("Update by existing email FORBIDDEN")
    @Description("Update by already existing email FORBIDDEN")
    public void updateByExistingEmail() {
        //authorize
        authorized = userClient.loginUser(user);

        // new user data with email of another existing user
        EmailName existingEmailNewName = UserGenerator.emailNameRandom();
        existingEmailNewName.setEmail(anotherCreatedUser.getUser().getEmail());
        //update
        ValidatableResponse response = userClient.updateUser(userCreated, existingEmailNewName);

        assertEquals("Status code is not FORBIDDEN", SC_FORBIDDEN, response.extract().statusCode());
        successMessage = response.extract().body().as(SuccessMessage.class);
        assertFalse("Success field should be false", successMessage.isSuccess());

        assertEquals("Message is not as expected", "User with such email already exists", successMessage.getMessage());
    }
}
