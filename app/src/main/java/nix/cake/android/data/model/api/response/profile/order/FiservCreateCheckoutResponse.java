package nix.cake.android.data.model.api.response.profile.order;

import lombok.Data;

@Data
public class FiservCreateCheckoutResponse {
    private String description;
    private FiservResponse checkout;
}

