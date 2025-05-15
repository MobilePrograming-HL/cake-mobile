package nix.cake.android.data.model.api.response.profile.order;

import java.util.List;

import lombok.Data;
import nix.cake.android.constant.Constants;

@Data
public class OrderResponse {
    private String id;
    private String code;
    private CustomerResponse customer;
    private List<OrderItemResponse> orderItems;
    private Integer shippingFee;
    private Integer paymentMethod;
    private Double totalAmount;
    private Double totalDiscount;
    private OrderStatusResponse status;
    private String note;
    private AddressResponse address;
    private String createdAt;
    private FiservCreateCheckoutResponse fiservInfo;

    public String getTypeShipping() {
        if (shippingFee == 25000) {
            return Constants.STANDARD_SHIPPING;
        } else {
            return Constants.FAST_SHIPPING;
        }
    }

}
