package nix.cake.android.data.model.api.response.profile.order;

import lombok.Data;

@Data
public class FiservResponse {
    private String storeId;
    private String checkoutId;
    private String redirectionUrl;
}