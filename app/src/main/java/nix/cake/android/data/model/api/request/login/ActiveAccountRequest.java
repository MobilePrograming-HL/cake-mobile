package nix.cake.android.data.model.api.request.login;

import lombok.Data;

@Data
public class ActiveAccountRequest {
    private String email;
    private String otpCode;
}
