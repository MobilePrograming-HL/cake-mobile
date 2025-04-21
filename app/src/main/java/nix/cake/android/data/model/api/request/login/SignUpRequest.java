package nix.cake.android.data.model.api.request.login;

import lombok.Data;

@Data
public class SignUpRequest {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private boolean termsAccepted;
}
