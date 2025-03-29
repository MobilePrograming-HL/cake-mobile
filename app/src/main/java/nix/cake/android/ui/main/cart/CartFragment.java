package nix.cake.android.ui.main.cart;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.FragmentCartBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;

public class CartFragment extends BaseFragment<FragmentCartBinding, CartViewModel> {

    private MutableLiveData<Boolean> isChecked = new MutableLiveData<>(false);

    public LiveData<Boolean> getIsChecked() {
        return isChecked;
    }

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
    }

    @SuppressLint("ResourceAsColor")
    public void onAllClick() {
        if (isChecked.getValue() != null && isChecked.getValue()) {
            isChecked.setValue(false);
            binding.checked.setImageResource(R.drawable.unchecked);
            binding.chooseAll.setTextColor(R.color.text_second);
        } else {
            isChecked.setValue(true);
            binding.checked.setImageResource(R.drawable.checked);
            binding.chooseAll.setTextColor(R.color.text_all);
        }
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cart;
    }
    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}
