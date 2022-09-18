package stellar.model.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ingredients {
    private List<String> ingredients = new ArrayList<>();

    public void add(String hash){
        ingredients.add(hash);
    }
}
