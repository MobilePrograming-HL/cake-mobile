package nix.cake.android.ui.main;

//import static android.os.Build.VERSION_CODES.R;

import static com.google.android.material.internal.ContextUtils.getActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.cart.UpdateCartItemRequest;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.cart.CartResponse;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.databinding.ActivityMainBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.cart.CartFragment;
import nix.cake.android.ui.main.home.HomeFragment;
import nix.cake.android.ui.main.login.LoginActivity;
import nix.cake.android.ui.main.login.UnLoginFragment;
import nix.cake.android.ui.main.product.detail.ProductDetailActivity;
import nix.cake.android.ui.main.product.find.FindProductFragment;
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
    private UnLoginFragment unLoginFragment;
    private FindProductFragment findProductFragment;
    private FragmentManager fm;
    private static final String HOME = "HOME";
    private static final String SHOP = "SHOP";
    private static final String CART = "CART";
    private static final String PROFILE = "PROFILE";
    private static final String UNLOGIN = "UNLOGIN";
    private static final String SEARCH = "SEARCH";


    private Integer currentFragment = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding.setA(this);
        viewBinding.setVm(viewModel);

        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        active = homeFragment;
        fm.beginTransaction().add(R.id.nav_host_fragment,homeFragment,HOME).commitNow();

        viewBinding.bottomNav.setOnItemSelectedListener(item -> {
            currentFragment = item.getItemId();
            switch (currentFragment) {
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
                    getListCategories();
                    return true;
                case R.id.cart:
                    if (isLogin()) {
                        if (cartFragment == null){
                            getCart();
                            cartFragment = new CartFragment();
                            fm.beginTransaction().add(R.id.nav_host_fragment, cartFragment, CART).hide(active).commit();
                        } else {
                            getCart();
                            fm.beginTransaction().hide(active).show(cartFragment).commit();
                        }
                        active = cartFragment;
                        return true;
                    } else {
                        if (unLoginFragment == null) {
                            unLoginFragment = new UnLoginFragment();
                            fm.beginTransaction().add(R.id.nav_host_fragment, unLoginFragment, UNLOGIN).hide(active).commit();
                        } else {
                            fm.beginTransaction().hide(active).show(unLoginFragment).commit();
                        }
                        active = unLoginFragment;
                        return true;
                    }
                case R.id.profile:
                    if (isLogin()) {
                        if (profileFragment == null) {
                            profileFragment = new ProfileFragment();
                            fm.beginTransaction().add(R.id.nav_host_fragment, profileFragment, PROFILE).hide(active).commit();
                        } else {
                            fm.beginTransaction().hide(active).show(profileFragment).commit();
                        }
                        active = profileFragment;
                        return true;
                    } else {
                        if (unLoginFragment == null) {
                            unLoginFragment = new UnLoginFragment();
                            fm.beginTransaction().add(R.id.nav_host_fragment, unLoginFragment, UNLOGIN).hide(active).commit();
                        } else {
                            fm.beginTransaction().hide(active).show(unLoginFragment).commit();
                        }
                        active = unLoginFragment;
                        return true;
                    }
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigateToCurrentFragment();
    }

    public void navigateToCurrentFragment() {
        switch (currentFragment) {
            case R.id.cart:
                if (cartFragment == null){
                    getCart();
                    cartFragment = new CartFragment();
                    fm.beginTransaction().add(R.id.nav_host_fragment, cartFragment, CART).hide(active).commit();
                } else {
                    getCart();
                    fm.beginTransaction().hide(active).show(cartFragment).commit();
                }
                active = cartFragment;
                break;
            case R.id.profile:
                if (isLogin()) {
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, profileFragment, PROFILE).hide(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(profileFragment).commit();
                    }
                    active = profileFragment;
                    break;
                } else {
                    if (unLoginFragment == null) {
                        unLoginFragment = new UnLoginFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, unLoginFragment, UNLOGIN).hide(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(unLoginFragment).commit();
                    }
                    active = unLoginFragment;
                    break;
                }
            default:
                break;
        }
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

    public void hideBottomNav() {
        viewModel.hideBottomNav();
    }

    public void showBottomNav() {
        viewModel.showBottomNav();
    }
    public void getMyOrders() {
        navigateToMyOrders();
    }
    public void navigateToMyOrders() {
        Intent it = new Intent(this, MyOrdersActivity.class);
        startActivity(it);
    }
    public void navigateToLogin() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }
    public void logout() {
//        viewModel.logout(new MainCalback<String>() {
//            @Override
//            public void doError(Throwable error) {
//
//            }
//
//            @Override
//            public void doSuccess() {
//
//            }
//
//            @Override
//            public void doFail() {
//
//            }
//
//            @Override
//            public void doSuccess(String object) {
//                navigateToLogin();
//            }
//        });
        viewModel.showLoading();
        viewModel.getRepository().getSharedPreferences().setToken("");
        navigateToLogin();
    }

    public void login() {
        viewModel.showLoading();
        navigateToLogin();
    }
    public void getListCategories() {
        shopFragment = new ShopFragment();
        fm.beginTransaction().add(R.id.nav_host_fragment, shopFragment, CART).hide(active).commit();
        active = shopFragment;
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
                ShopFragment.CATEGORIES_LIST.postValue(object.getContent());
                viewModel.getListProducts(new MainCalback<ResponseListObj<ProductResponse>>() {
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
                        ShopFragment.PRODUCT_LIST.setValue(object.getContent());
                    }
                }, null, 0);
            }
        });
    }
    public void getListProduct(String categoryId, Integer page) {
        viewModel.getListProducts(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {}

            @Override
            public void doSuccess() {}

            @Override
            public void doFail() {}

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                List<ProductResponse> currentList = ShopFragment.PRODUCT_LIST.getValue();
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                currentList.addAll(object.getContent());
                ShopFragment.PRODUCT_LIST.setValue(currentList);
            }
        }, categoryId, page);
    }
    public void getProductDetail(String id) {
        viewModel.getProductDetail(new MainCalback<ProductResponse>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(ProductResponse object) {
                ProductDetailActivity.PRODUCT_DETAIL = object;
                Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
                startActivity(intent);
            }
            @Override
            public void doFail() {

            }
        }, id);
    }
    public void navigateToSearchFragment() {
        findProductFragment = new FindProductFragment();
        fm.beginTransaction().add(R.id.nav_host_fragment, findProductFragment, SEARCH).hide(active).commit();
        active = findProductFragment;
        hideBottomNav();
    }
    public void navigateToShopFragment() {
        hideKeyboard();
        if (shopFragment == null){
            shopFragment = new ShopFragment();
            fm.beginTransaction().add(R.id.nav_host_fragment, shopFragment, SEARCH).hide(active).commit();
        } else {
            fm.beginTransaction().hide(active).show(shopFragment).commit();
        }
        active = shopFragment;
        showBottomNav();
    }
    public void getListAddress(int page) {
        viewModel.getListAddresses(new MainCalback<ResponseListObj<AddressResponse>>() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(ResponseListObj<AddressResponse> object) {
                ShippingAddressActivity.updateAddressList(object.getContent());
                navigateToShippingAddress();
            }
            @Override
            public void doFail() {

            }
        }, page);
    }
    public void navigateToShippingAddress() {
        Intent it = new Intent(this, ShippingAddressActivity.class);
        startActivity(it);
    }

    public void getCart() {
        viewModel.getCart(new MainCalback<CartResponse>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(CartResponse object) {
                CartFragment.updateCartItemList(object);
            }
            @Override
            public void doFail() {

            }
        });
    }

    public void updateCartItem(CartItemResponse item) {
        UpdateCartItemRequest updateCartItemRequest = new UpdateCartItemRequest();

        updateCartItemRequest.setCartItemId(item.getId());
        updateCartItemRequest.setQuantity(item.getQuantity());

        viewModel.updateCartItem(updateCartItemRequest);
    }
}
