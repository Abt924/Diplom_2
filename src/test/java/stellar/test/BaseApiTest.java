package stellar.test;

import org.junit.After;
import org.junit.Before;
import stellar.model.OrderClient;
import stellar.model.UserClient;
import stellar.model.pojo.Authorized;
import stellar.model.pojo.User;
import stellar.model.pojo.UserCreated;


public class BaseApiTest {
    protected UserClient userClient;
    protected OrderClient orderClient;

    protected User user;
    protected User anotherUser;
    protected UserCreated userCreated;
    protected UserCreated anotherCreatedUser;
    protected Authorized authorized;


    public BaseApiTest() {
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }


    @After
    public void tearDown() {
        // logout
        if (authorized != null && authorized.isSuccess()) {
            userClient.logout(authorized.getRefreshToken());
        }
        // remove created users
        if (userCreated != null && userCreated.isSuccess()) {
            userClient.deleteUser(userCreated);
        }
        if (anotherCreatedUser != null && anotherCreatedUser.isSuccess()) {
            userClient.deleteUser(anotherCreatedUser);
        }
    }
}
