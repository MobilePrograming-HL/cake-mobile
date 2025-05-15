package nix.cake.android.data.model.api.request.order;

import lombok.Data;

@Data
public class BuyNowOrderRequest {

    private String productId;

    private String tagId;

    private int quantity;

    private int shippingFee;

    private String note;

    private Integer paymentMethod;

    private String addressId;

}

