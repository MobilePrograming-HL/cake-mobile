package nix.cake.android.ui.main.profile.address;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.databinding.ActivityShippingAddressBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.profile.address.adapter.AddressItemAdapter;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;
import nix.cake.android.ui.main.profile.order.adapter.OrderItemAdapter;

public class ShippingAddressActivity extends BaseActivity<ActivityShippingAddressBinding, ShippingAddressViewModel> implements AddressItemAdapter.OnItemClickListener {

    private AddressItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setUpAdapter();
    }

    public void setUpAdapter() {
        adapter = new AddressItemAdapter(this);
        viewBinding.rvAddress.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rvAddress.setAdapter(adapter);
        adapter.setData(viewModel.addressList);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shipping_address;
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
    public void onEditClick(AddressResponse address) {
        Intent it = new Intent(this, AddressDetailActivity.class);
        startActivity(it);
    }

    @Override
    public void onDeleteClick(AddressResponse address) {
        viewModel.showNormalMessage("Delete address");
    }
}
