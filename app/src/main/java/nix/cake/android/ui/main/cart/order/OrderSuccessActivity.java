package nix.cake.android.ui.main.cart.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.ActivityOrderSuccessBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.profile.order.PaymentRedirectActivity;

public class OrderSuccessActivity extends BaseActivity<ActivityOrderSuccessBinding, OrderSuccessViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        Uri uri = getIntent().getData();
        if (uri != null) {
            boolean isSuccess = uri.getPath().contains("success");

            // Lấy lại orderId từ SharedPreferences
            SharedPreferences prefs = getSharedPreferences("payment", MODE_PRIVATE);
            String orderId = prefs.getString(PaymentRedirectActivity.KEY_ORDER_ID, null);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("orderId", orderId);
            resultIntent.putExtra("success", isSuccess);
            setResult(RESULT_OK, resultIntent);
            finish();
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
