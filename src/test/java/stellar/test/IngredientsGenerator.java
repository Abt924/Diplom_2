package stellar.test;

import com.github.javafaker.Faker;
import com.github.javafaker.IdNumber;
import stellar.model.pojo.AllIngredients;
import stellar.model.pojo.Ingredient;
import stellar.model.pojo.Ingredients;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IngredientsGenerator extends BaseApiTest {

    private final AllIngredients allIngredients;
    public List<String> allIds = new ArrayList<String>();

    public IngredientsGenerator() {
        super.setUp();
        allIngredients = orderClient.getIngredients().extract().body().as(AllIngredients.class);
        for (Ingredient ingredient : allIngredients.getData()) {
            allIds.add(ingredient.get_id());
        }
    }

    public Ingredients empty() {
        return new Ingredients();
    }

    public Ingredients randomIngredients(int count) {
        Ingredients stack = new Ingredients();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int index = random.nextInt(allIds.size());
            stack.add(allIds.get(index));
        }
        return stack;
    }

    public boolean isValidHash(String hash) {
        return allIds.contains(hash);
    }

    public String createInvalidHash() {
        Faker faker = new Faker();
        IdNumber id = faker.idNumber();
        while (isValidHash(id.toString())) {
            id = faker.idNumber();
        }
        return id.toString();
    }

    public Ingredients stackWithOneInvalidId(int validCount) {
        Ingredients stack = randomIngredients(validCount);
        stack.add(createInvalidHash());
        return stack;
    }
}
