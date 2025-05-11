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
    }
}
