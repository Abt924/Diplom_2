package stellar.model.pojo;

import lombok.Data;

import java.util.List;

@Data
public class UserOrders {
    private boolean success;
    private List<Order> orders;
    private Integer total;
    private Integer totalToday;
}
