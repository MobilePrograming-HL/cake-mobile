package nix.cake.android.ui.main.profile.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ActivityMyOrdersBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.profile.order.adapter.OrderItemAdapter;
import nix.cake.android.ui.main.profile.order.detail.OrderDetailActivity;

public class MyOrdersActivity extends BaseActivity<ActivityMyOrdersBinding, MyOrdersViewModel> implements OrderItemAdapter.OnItemClickListener {

    OrderItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setUpAdapter();
    }
    public void setUpAdapter() {
        adapter = new OrderItemAdapter(this);
        viewBinding.rvOrder.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rvOrder.setAdapter(adapter);
        adapter.setData(viewModel.ordersList);
    }
    public void onFilterClicked(String filter) {
        viewBinding.statusOrder.setText(filter);
        switch (filter) {
            case "All":
                visibleAllTick();
                viewBinding.tickAll.setVisibility(View.VISIBLE);
                break;
            case "Waiting":
                visibleAllTick();
                viewBinding.tickWaitForConfirmation.setVisibility(View.VISIBLE);
                break;
            case "Processing":
                visibleAllTick();
                viewBinding.tickProcessing.setVisibility(View.VISIBLE);
                break;
            case "Delivered":
                visibleAllTick();
                viewBinding.tickDelivered.setVisibility(View.VISIBLE);
                break;
            case "Cancelled":
                visibleAllTick();
                viewBinding.tickCancelled.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        viewBinding.lMyOrder.closeDrawer(viewBinding.menuFilter, true);

    }

    public void showRightMenu(){
        viewBinding.lMyOrder.openDrawer(viewBinding.menuFilter);
    }
    public void closeRightMenu(){
        viewBinding.lMyOrder.closeDrawer(viewBinding.menuFilter, true);
    }
    public void visibleAllTick() {
        viewBinding.tickAll.setVisibility(View.GONE);
        viewBinding.tickWaitForConfirmation.setVisibility(View.GONE);
        viewBinding.tickProcessing.setVisibility(View.GONE);
        viewBinding.tickDelivered.setVisibility(View.GONE);
        viewBinding.tickCancelled.setVisibility(View.GONE);
    }
    @Override
    public void onItemClick(OrderResponse order) {
        Intent it = new Intent(this, OrderDetailActivity.class);
        startActivity(it);
    }

    @Override
    public void onCancelClick(OrderResponse order) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_orders;
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
