package stellar.model.pojo;

import lombok.Data;
import stellar.model.pojo.EmailName;

@Data
public class UserCreated {
    private boolean success;
    private EmailName user;
    private String accessToken;
    private String refreshToken;
}
