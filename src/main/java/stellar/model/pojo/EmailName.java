package stellar.model.pojo;

import lombok.Data;

@Data
public class EmailName {
    public EmailName(String email, String name) {
        this.email = email;
        this.name = name;
    }

    private String email;
    private String name;
}
