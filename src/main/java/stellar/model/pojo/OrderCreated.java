package stellar.model.pojo;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreated {
    private List<Ingredient> ingredients;
    private String _id;
    private UserInOrder owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private Integer number;
    private Float price;
}
