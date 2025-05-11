package nix.cake.android.data.model.api.request.order;

import java.util.List;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private List<String> cartItemIds;
    private Integer shippingFee;
    private String note;
    private Integer paymentMethod;
    private String addressId;
}
