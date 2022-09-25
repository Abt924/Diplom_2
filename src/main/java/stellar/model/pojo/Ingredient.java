package stellar.model.pojo;

import lombok.Data;

@Data
public class Ingredient {
    private String _id;
    private String name;
    private String type;
    private Float proteins;
    private Float fat;
    private Float carbohydrates;
    private Float calories;
    private Float price;
    private String image;
    private String image_mobile;
    private String image_large;
    private Integer __v;
}
