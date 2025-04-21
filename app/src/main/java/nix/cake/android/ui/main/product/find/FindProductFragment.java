package nix.cake.android.ui.main.product.find;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.FragmentFindProductBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;

public class FindProductFragment extends BaseFragment<FragmentFindProductBinding, FindProductFragmentViewModel> {
    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.search.requestFocus();
        showKeyboard(binding.search);
    }

    private void showKeyboard(View view) {
        view.post(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
    public void onBackClick() {
        ((MainActivity) requireActivity()).navigateToShopFragment();
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find_product;
    }


    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}
