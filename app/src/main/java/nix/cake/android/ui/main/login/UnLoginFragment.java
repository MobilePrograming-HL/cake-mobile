package nix.cake.android.ui.main.login;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.FragmentUnLoginBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;

public class UnLoginFragment extends BaseFragment<FragmentUnLoginBinding, UnLoginViewModel> {

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_un_login;
    }
    public void onLoginClick() {
        ((MainActivity) requireActivity()).login();
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}
