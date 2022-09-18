package stellar.model.pojo;

import lombok.Data;

@Data
public class SuccessOrder {
    private boolean success;
    private String name;
    private OrderCreated order;
}
