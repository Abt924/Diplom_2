package stellar.model.pojo;

import lombok.Data;

@Data
public class UserCreated {
    private boolean success;
    private EmailName user;
    private String accessToken;
    private String refreshToken;
}
