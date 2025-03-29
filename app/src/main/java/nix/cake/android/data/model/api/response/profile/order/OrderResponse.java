package nix.cake.android.data.model.api.response.profile.order;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private int quantity;
    private Double total;
    private String dateOrder;
    private String status;

    public OrderResponse(Long id, int quantity, Double total, String dateOrder, String status) {
        this.id = id;
        this.quantity = quantity;
        this.total = total;
        this.dateOrder = dateOrder;
        this.status = status;
    }
}
