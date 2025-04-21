package nix.cake.android.data.model.api.request.cart;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private String cartItemId;
    private Integer quantity;
}
