import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stellar.model.*;


public class BaseApiTest {
    protected UserClient userClient;


    public BaseApiTest() {}

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
    }

}
