package nix.cake.android.ui.main.profile.order.detail;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;

public class OrderDetailViewModel extends BaseViewModel {
    List<OrderResponse> ordersList;

    public OrderDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        initSampleOrders();
    }
    private void initSampleOrders() {
        ordersList = new ArrayList<>();

        ordersList.add(new OrderResponse(1L, 2, 150000.0, "2025-03-01", "Delivered"));
        ordersList.add(new OrderResponse(2L, 1, 75000.0, "2025-03-02", "Waiting"));
        ordersList.add(new OrderResponse(3L, 3, 225000.0, "2025-03-03", "Processing"));
    }
}
