package nix.cake.android.data.model.api.response.profile.order;

import lombok.Data;

@Data
public class OrderItemResponse {
    private String id;
    private ProductResponse product;
    private Integer quantity;
    private Double price;
    private Integer discountPercentage;
    private Double totalPrice;
}
