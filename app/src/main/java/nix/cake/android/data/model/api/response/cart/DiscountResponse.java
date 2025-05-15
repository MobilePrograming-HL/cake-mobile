package nix.cake.android.data.model.api.response.cart;

import lombok.Data;

@Data
public class DiscountResponse {
    private String id;
    private String code;
    private Integer discountPercentage;
}
