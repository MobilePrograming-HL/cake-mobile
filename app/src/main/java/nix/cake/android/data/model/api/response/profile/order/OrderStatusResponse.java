package nix.cake.android.data.model.api.response.profile.order;

import lombok.Data;
import nix.cake.android.constant.Constants;

@Data
public class OrderStatusResponse {
    private String id;
    private Integer status;
    private String date;

    public String getOrderStatus(Integer status) {
        if (status == null) return "Unknown";

        switch (status) {
            case Constants.ORDER_STATUS_PENDING:
                return "Pending";
            case Constants.ORDER_STATUS_PROCESSING:
                return "Processing";
            case Constants.ORDER_STATUS_SHIPPING:
                return "Shipping";
            case Constants.ORDER_STATUS_DELIVERED:
                return "Delivered";
            case Constants.ORDER_STATUS_CANCELED:
                return "Canceled";
            default:
                return "Unknown";
        }
    }
}
