package nix.cake.android.ui.main.profile.order.detail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.ActivityOrderDetailBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.profile.order.adapter.OrderItemAdapter;
import nix.cake.android.ui.main.profile.order.detail.adapter.OrderDetailItemAdapter;

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding, OrderDetailViewModel> implements OrderDetailItemAdapter.OnItemClickListener {
    private OrderDetailItemAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setUpAdapter();
    }
    public void setUpAdapter() {
        adapter = new OrderDetailItemAdapter(this);
        viewBinding.rvOrderDetail.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rvOrderDetail.setAdapter(adapter);
        adapter.setData(viewModel.ordersList);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }
}
