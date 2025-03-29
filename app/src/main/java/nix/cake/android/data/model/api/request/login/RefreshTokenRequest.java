package nix.cake.android.data.model.api.request.login;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String token;
}
