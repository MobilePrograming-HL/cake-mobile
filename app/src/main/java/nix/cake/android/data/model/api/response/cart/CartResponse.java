package nix.cake.android.data.model.api.response.cart;

import java.util.List;

import lombok.Data;

@Data
public class CartResponse {
    private String id;
    private List<CartItemResponse> cartItems;
}
