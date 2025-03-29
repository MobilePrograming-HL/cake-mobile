package nix.cake.android.ui.main;

//import static android.os.Build.VERSION_CODES.R;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Objects;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.ActivityMainBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.cart.CartFragment;
import nix.cake.android.ui.main.home.HomeFragment;
import nix.cake.android.ui.main.login.LoginActivity;
import nix.cake.android.ui.main.profile.ProfileFragment;
import nix.cake.android.ui.main.profile.address.ShippingAddressActivity;
import nix.cake.android.ui.main.profile.order.MyOrdersActivity;
import nix.cake.android.ui.main.shop.ShopFragment;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    private Fragment active;
    private HomeFragment homeFragment;
    private ShopFragment shopFragment;
    private CartFragment cartFragment;
    private ProfileFragment profileFragment;
    private FragmentManager fm;
    private static final String HOME = "HOME";
    private static final String SHOP = "SHOP";
    private static final String CART = "CART";
    private static final String PROFILE = "PROFILE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        viewModel.setDeviceId(deviceId);

        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        active = homeFragment;
        fm.beginTransaction().add(R.id.nav_host_fragment,homeFragment,HOME).commitNow();

        viewBinding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    if (homeFragment == null){
                        homeFragment = new HomeFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, HOME).hide(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                    }
                    active = homeFragment;
                    return true;
                case R.id.shop:
                    if (shopFragment == null){
                        shopFragment = new ShopFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, shopFragment, SHOP).hide(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(shopFragment).commit();
                    }
                    active = shopFragment;
                    return true;
                case R.id.cart:
                    if (cartFragment == null){
                        cartFragment = new CartFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, cartFragment, CART).hide(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(cartFragment).commit();
                    }
                    active = cartFragment;
                    return true;
                case R.id.profile:
                    if (isLogin()) {
                        if (profileFragment == null) {
                            profileFragment = new ProfileFragment();
                            fm.beginTransaction().add(R.id.nav_host_fragment, profileFragment, PROFILE).hide(active).commit();
                        } else {
                            fm.beginTransaction().hide(active).show(profileFragment).commit();
                        }
                    } else {
                        viewModel.showLoading();
                        navigateToLogin();
                    }
                    return true;
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.hideLoading();

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }
    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }
    public void getMyOrders() {
        navigateToMyOrders();
    }
    public void getShippingAddress() {
        navigateToShippingAddress();
    }
    public void navigateToMyOrders() {
        Intent it = new Intent(this, MyOrdersActivity.class);
        startActivity(it);
    }

    public void navigateToShippingAddress() {
        Intent it = new Intent(this, ShippingAddressActivity.class);
        startActivity(it);
    }
    public void navigateToLogin() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }
}
