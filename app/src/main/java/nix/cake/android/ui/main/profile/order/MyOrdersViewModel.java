package nix.cake.android.ui.main.profile.order;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;

public class MyOrdersViewModel extends BaseViewModel {
    List<OrderResponse> ordersList;
    public MyOrdersViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        initSampleOrders();
    }

    private void initSampleOrders() {
        ordersList = new ArrayList<>();

        ordersList.add(new OrderResponse(1L, 2, 150000.0, "2025-03-01", "Delivered"));
        ordersList.add(new OrderResponse(2L, 1, 75000.0, "2025-03-02", "Waiting"));
        ordersList.add(new OrderResponse(3L, 3, 225000.0, "2025-03-03", "Processing"));
        ordersList.add(new OrderResponse(4L, 4, 300000.0, "2025-03-04", "Cancelled"));
        ordersList.add(new OrderResponse(5L, 2, 180000.0, "2025-03-05", "Delivered"));
        ordersList.add(new OrderResponse(6L, 1, 90000.0, "2025-03-06", "Waiting"));
        ordersList.add(new OrderResponse(7L, 5, 375000.0, "2025-03-07", "Processing"));
        ordersList.add(new OrderResponse(8L, 3, 270000.0, "2025-03-08", "Delivered"));
        ordersList.add(new OrderResponse(9L, 2, 120000.0, "2025-03-09", "Cancelled"));
        ordersList.add(new OrderResponse(10L, 1, 60000.0, "2025-03-10", "Waiting"));
        ordersList.add(new OrderResponse(11L, 4, 240000.0, "2025-03-11", "Processing"));
        ordersList.add(new OrderResponse(12L, 2, 150000.0, "2025-03-12", "Delivered"));
        ordersList.add(new OrderResponse(13L, 3, 195000.0, "2025-03-13", "Waiting"));
        ordersList.add(new OrderResponse(14L, 1, 85000.0, "2025-03-14", "Cancelled"));
        ordersList.add(new OrderResponse(15L, 5, 400000.0, "2025-03-15", "Processing"));
        ordersList.add(new OrderResponse(16L, 2, 140000.0, "2025-03-16", "Delivered"));
        ordersList.add(new OrderResponse(17L, 3, 210000.0, "2025-03-17", "Waiting"));
        ordersList.add(new OrderResponse(18L, 4, 320000.0, "2025-03-18", "Processing"));
        ordersList.add(new OrderResponse(19L, 1, 95000.0, "2025-03-19", "Cancelled"));
        ordersList.add(new OrderResponse(20L, 2, 160000.0, "2025-03-20", "Delivered"));
    }
}
