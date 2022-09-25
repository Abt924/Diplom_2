package stellar.model.pojo;

import lombok.Data;

@Data
public class Authorized {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private EmailName user;
}
