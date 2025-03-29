package nix.cake.android.ui.main.profile;

import android.content.Intent;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.FragmentProfileBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.profile.order.MyOrdersActivity;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> {

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
    }
    public void onMyOrdersClick() {
        ((MainActivity) requireActivity()).getMyOrders();
    }
    public void onShippingAddressClick() {
        ((MainActivity) requireActivity()).getShippingAddress();
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}
