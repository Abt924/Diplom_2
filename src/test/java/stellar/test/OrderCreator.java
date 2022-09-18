package stellar.test;

import stellar.model.OrderClient;
import stellar.model.pojo.Authorized;
import stellar.model.pojo.Ingredients;

import java.util.Random;

import static org.apache.http.HttpStatus.SC_OK;

public class OrderCreator extends BaseApiTest {

    OrderClient orderClient;
    IngredientsGenerator generator;
    Authorized auth;

    public OrderCreator(OrderClient client, IngredientsGenerator generator, Authorized auth) {
        this.orderClient = client;
        this.generator = generator;
        this.auth = auth;
    }

    public boolean createRandomOrder() {
        Ingredients stack = generator.randomIngredients(new Random().nextInt(12) + 1);
        return orderClient
                 .createOrderAuthorized(stack, auth)
                 .extract()
                 .statusCode() == SC_OK;
    }

    public int createFewOrders(int count) {
        int createdCount = 0;
        for (int i = 0; i < count; i++) {
            if (createRandomOrder()) createdCount++ ;
        }
        return createdCount;
    }

}
