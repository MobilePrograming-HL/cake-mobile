package nix.cake.android.data.model.api.response.cart;

import lombok.Data;
import nix.cake.android.data.model.api.response.product.TagResponse;

@Data
public class CartItemResponse {
    private String id;
    private ProductResponse product;
    private TagResponse tag;
    private Integer quantity;
    private Boolean isSelect = false;

    public Double getTotal(){
        return product.getPriceOfProduct() * quantity;
    }
}
