package nix.cake.android.ui.main.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.ActivitySplashBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.ui.main.home.HomeFragment;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, SplashViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.SplashTheme);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        getListCategoriesForHome();
        getListProductForHome(Constants.ASC, "");
        startActivity(new Intent(this, MainActivity.class));
        finish();

        if (isLogin()) {
            viewModel.doRefreshToken(viewModel.repository.getSharedPreferences().getToken());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    public void getListCategoriesForHome() {
        viewModel.getListCategories(new MainCalback<ResponseListObj<CategoryResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doFail() {

            }

            @Override
            public void doSuccess(ResponseListObj<CategoryResponse> object) {
                HomeFragment.CATEGORIES_LIST.postValue(object.getContent());
            }
        });
    }
    public void getListProductForHome(String createSort, String soldSort) {
        viewModel.getProductForHome(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doFail() {

            }

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                HomeFragment.PRODUCT_LIST.setValue(object.getContent());

            }
        }, Constants.SIZE_ITEM, createSort, soldSort);
    }
}

