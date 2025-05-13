package nix.cake.android.ui.main.cart.order;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.ActivityOrderSuccessBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;

public class OrderSuccessActivity extends BaseActivity<ActivityOrderSuccessBinding, OrderSuccessViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        Uri uri = getIntent().getData();
        if (uri != null) {
            String path = uri.getPath(); // "/success" hoặc "/fail"
            String orderId = uri.getQueryParameter("orderId");

//            if ("/success".equals(path)) {
//            } else if ("/fail".equals(path)) {
//            }
        }
    }
    public void goToShopNow() {
        this.finish();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_success;
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
