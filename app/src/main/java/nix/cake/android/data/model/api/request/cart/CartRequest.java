package nix.cake.android.data.model.api.request.cart;

import lombok.Data;

@Data
public class CartRequest {
    private String productId;
    private Integer quantity;
    private String tagId;
}
