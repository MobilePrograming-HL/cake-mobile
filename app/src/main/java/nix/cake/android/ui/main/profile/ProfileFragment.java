package nix.cake.android.ui.main.profile;

import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.response.profile.CustomerResponse;
import nix.cake.android.databinding.FragmentProfileBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> {

    public static MutableLiveData<CustomerResponse> CUSTOMER = new MutableLiveData<>();

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setUpObserversCustomer();
    }
    public void setUpObserversCustomer() {
        CUSTOMER.observe(this, customerResponse -> {
            if (customerResponse == null) return;
            Glide.with(this)
                    .load(customerResponse.getAvatarPath())
                    .placeholder(R.color.img_default)
                    .error(R.color.img_default)
                    .into(binding.avatarImage);
            binding.name.setText(customerResponse.getUsername());
            binding.email.setText(customerResponse.getEmail());

        });
    }
    public void onMyOrdersClick() {
        ((MainActivity) requireActivity()).getMyOrders();
    }
    public void onShippingAddressClick() {
        ((MainActivity) requireActivity()).getListAddress(Constants.SIZE_ITEM);
    }
    public void onSignOutClick() {
        ((MainActivity) requireActivity()).logout();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        CUSTOMER = new MutableLiveData<>();
    }
}
