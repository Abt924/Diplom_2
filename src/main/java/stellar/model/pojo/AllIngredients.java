package stellar.model.pojo;

import lombok.Data;

import java.util.List;

@Data
public class AllIngredients {
    private boolean success;
    private List<Ingredient> data;
}
