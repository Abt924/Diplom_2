package stellar.model;

import com.github.javafaker.Faker;
import stellar.model.pojo.User;

import java.util.Locale;

public class UserGenerator {
    public static User createDefault() {
        return new User("fedorff@fedor.ru", "fff", "Fedor");
    }

    public static User createRandom() {
        Faker faker = new Faker();
        return new User(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().name()
        );
    }

    public static User noEmailRandom() {
        User user = UserGenerator.createRandom();
        user.setEmail("");
        return user;
    }

    public static User noPasswordRandom() {
        User user = UserGenerator.createRandom();
        user.setPassword("");
        return user;
    }

    public static User noNameRandom() {
        User user = UserGenerator.createRandom();
        user.setName("");
        return user;
    }
}
