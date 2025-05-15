package nix.cake.android.data.model.api.response.product;

import lombok.Data;

@Data
public class DiscountResponse {
    private String id;
    private String code;
    private int discountPercentage;
}
