package nix.cake.android.ui.main.profile.order.detail;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ActivityOrderDetailBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.profile.order.detail.adapter.OrderDetailItemAdapter;
import nix.cake.android.utils.DisplayUtils;

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding, OrderDetailViewModel> implements OrderDetailItemAdapter.OnItemClickListener {
    private OrderDetailItemAdapter adapter;
    public static MutableLiveData<OrderResponse> ORDER = new MutableLiveData<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setUpAdapter();
        setUpObserversOrder();
    }

    @SuppressLint("SetTextI18n")
    private void setUpObserversOrder() {
        ORDER.observe(this, order -> {
            if (order != null && adapter != null) {
                adapter.setData(order.getOrderItems(), order.getStatus().getStatus());
                viewBinding.name.setText(order.getAddress().getFullName());
                viewBinding.phone.setText(order.getAddress().getPhoneNumber());
                viewBinding.addressDetails.setText(order.getAddress().getDetails());
                viewBinding.address.setText(order.getAddress().getCommune().getName() + ", " + order.getAddress().getDistrict().getName()
                        + ", " + order.getAddress().getProvince().getName());

                viewBinding.retailPrice.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(order.getTotalAmount() - Double.valueOf(order.getShippingFee())));
                viewBinding.shippingFee.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Double.valueOf(order.getShippingFee())));
                viewBinding.total.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(order.getTotalAmount()));

                viewBinding.shippingMethod.setText(order.getTypeShipping());
            }
        });


    }
    public void setUpAdapter() {
        adapter = new OrderDetailItemAdapter(this);
        viewBinding.rvProduct.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rvProduct.setAdapter(adapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ORDER = new MutableLiveData<>();
    }
}
