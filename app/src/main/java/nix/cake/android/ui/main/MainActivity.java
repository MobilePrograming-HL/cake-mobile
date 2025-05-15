package nix.cake.android.ui.main;

//import static android.os.Build.VERSION_CODES.R;

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
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.cart.UpdateCartItemRequest;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.cart.CartResponse;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.CustomerResponse;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ActivityMainBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.cart.CartFragment;
import nix.cake.android.ui.main.cart.order.CreateOrderActivity;
import nix.cake.android.ui.main.home.HomeFragment;
import nix.cake.android.ui.main.login.LoginActivity;
import nix.cake.android.ui.main.login.UnLoginFragment;
import nix.cake.android.ui.main.product.detail.ProductDetailActivity;
import nix.cake.android.ui.main.product.find.FindProductActivity;
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
    public static final String SHOP = "SHOP";
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
        getProfile();
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
                        getCart();
                        cartFragment = new CartFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, cartFragment, CART).hide(active).commit();
                        active = cartFragment;
                        return true;
                    } else {
                        if (unLoginFragment == null) {
                            getListProductForUnLogin(null, "", "asc");
                            unLoginFragment = new UnLoginFragment();
                            fm.beginTransaction().add(R.id.nav_host_fragment, unLoginFragment, UNLOGIN).hide(active).commit();
                        } else {
                            getListProductForUnLogin(null, "", "asc");
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
                            getListProductForUnLogin(null, "", "asc");
                            unLoginFragment = new UnLoginFragment();
                            fm.beginTransaction().add(R.id.nav_host_fragment, unLoginFragment, UNLOGIN).hide(active).commit();
                        } else {
                            getListProductForUnLogin(null, "", "asc");
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
        getProfile();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void navigateToCurrentFragment() {
        switch (currentFragment) {
            case R.id.cart:
                if (isLogin()) {
                    getCart();
                    cartFragment = new CartFragment();
                    fm.beginTransaction().add(R.id.nav_host_fragment, cartFragment, CART).hide(active).commit();
                    active = cartFragment;
                    break;
                } else {
                    if (unLoginFragment == null) {
                        getListProductForUnLogin(null, "", "asc");
                        unLoginFragment = new UnLoginFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, unLoginFragment, UNLOGIN).hide(active).commit();
                    } else {
                        getListProductForUnLogin(null, "", "asc");
                        fm.beginTransaction().hide(active).show(unLoginFragment).commit();
                    }
                    active = unLoginFragment;
                    break;
                }

            case R.id.profile:
                if (isLogin()) {
                    if (profileFragment == null) {
                        getProfile();
                        profileFragment = new ProfileFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, profileFragment, PROFILE).hide(active).commit();
                    } else {
                        fm.beginTransaction().hide(active).show(profileFragment).commit();
                    }
                    active = profileFragment;
                    break;
                } else {
                    if (unLoginFragment == null) {
                        getListProductForUnLogin(null, "", "asc");
                        unLoginFragment = new UnLoginFragment();
                        fm.beginTransaction().add(R.id.nav_host_fragment, unLoginFragment, UNLOGIN).hide(active).commit();
                    } else {
                        getListProductForUnLogin(null, "", "asc");
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
        viewModel.getListOrder(new MainCalback<ResponseListObj<OrderResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }
            @Override
            public void doSuccess(ResponseListObj<OrderResponse> object) {
                MyOrdersActivity.IS_EMPTY.setValue(object.getContent().isEmpty());
                MyOrdersActivity.ORDER_LIST.postValue(object.getContent());
                getListProductForOrder(null, Constants.PAGE_START);
            }
            @Override
            public void doFail() {

            }
        }, Constants.SIZE_ITEM);
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
                FindProductFragment.CATEGORIES_LIST.postValue(object.getContent());
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
                }, null, Constants.PAGE_START);
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

    public void getListProductSortForShop(String categoryId, String createdSort, String soldSort, String priceSort) {
        viewModel.getProductSortForShop(new MainCalback<ResponseListObj<ProductResponse>>() {
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
        },Constants.SIZE_ITEM, categoryId, createdSort, soldSort, priceSort);
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
    public void getListAddress(int size) {
        navigateToShippingAddress();
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
                ShippingAddressActivity.IS_EMPTY.setValue(object.getContent().isEmpty());
                ShippingAddressActivity.updateAddressList(object.getContent());
            }
            @Override
            public void doFail() {

            }
        }, size);
    }
    public void navigateToShippingAddress() {
        Intent it = new Intent(this, ShippingAddressActivity.class);
        startActivity(it);
    }
    public void getCart() {
        CartFragment.PRODUCT_LIST.setValue(new ArrayList<>());
        viewModel.getCart(new MainCalback<CartResponse>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(CartResponse object) {
                CartFragment.IS_EMPTY.setValue(object.getCartItems().isEmpty());
                CartFragment.updateCartItemList(object);
                CartFragment.currentPage = 0;
                getListProductForCart(null, Constants.PAGE_START);
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
    public void deleteCartItem(CartItemResponse item) {
        viewModel.deleteCartItem(item.getId());
    }
    public void getListProductForCart(String categoryId, Integer page) {
        viewModel.getListProducts(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {}

            @Override
            public void doSuccess() {}

            @Override
            public void doFail() {}

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                List<ProductResponse> currentList = CartFragment.PRODUCT_LIST.getValue();
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                currentList.addAll(object.getContent());
                CartFragment.PRODUCT_LIST.setValue(currentList);
            }
        }, categoryId, page);
    }
    public void getListProductForOrder(String categoryId, Integer page) {
        viewModel.getListProducts(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {}

            @Override
            public void doSuccess() {}

            @Override
            public void doFail() {}

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                List<ProductResponse> currentList = MyOrdersActivity.PRODUCT_LIST.getValue();
                if (currentList == null) {
                    currentList = new ArrayList<>();
                }
                currentList.addAll(object.getContent());
                MyOrdersActivity.PRODUCT_LIST.setValue(currentList);
            }
        }, categoryId, page);
    }

    public void getListProductForSearch(String categoryId, String name, String sort) {
        viewModel.searchProduct(new MainCalback<ResponseListObj<ProductResponse>>() {
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
                if (object.getContent() == null || object.getContent().isEmpty()) {
                    viewModel.getListProduct(new MainCalback<ResponseListObj<ProductResponse>>() {
                        @Override
                        public void doError(Throwable error) {

                        }

                        @Override
                        public void doSuccess() {

                        }
                        @Override
                        public void doSuccess(ResponseListObj<ProductResponse> object) {
                            FindProductActivity.SEARCH_KEY.postValue(name);
                            FindProductActivity.PRODUCT_LIST.postValue(object.getContent());
                            FindProductActivity.CATE_ID.postValue(categoryId);
                        }
                        @Override
                        public void doFail() {

                        }
                    }, null, Constants.SIZE_ITEM, Constants.PAGE_START);
                } else {
                    FindProductActivity.SEARCH_KEY.postValue(name);
                    FindProductActivity.PRODUCT_LIST.postValue(object.getContent());
                    FindProductActivity.CATE_ID.postValue(categoryId);
                }

            }
        }, categoryId, Constants.SIZE_ITEM, name, sort);
    }
    public void getListCategoriesForSearch() {
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
                FindProductActivity.CATEGORY_LIST.postValue(object.getContent());
            }
        });
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
    public void getListAddressForCreateOrder(int size) {
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
                CreateOrderActivity.IS_EMPTY_ADDRESS.setValue(object.getContent().isEmpty());
                CreateOrderActivity.ADDRESS_LIST.setValue(object.getContent());
            }
            @Override
            public void doFail() {

            }
        }, size);
    }
    public void getListProductForUnLogin(String categoryId, String name, String sort) {
        viewModel.searchProduct(new MainCalback<ResponseListObj<ProductResponse>>() {
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
                UnLoginFragment.PRODUCT_LIST.setValue(new ArrayList<>());
                UnLoginFragment.PRODUCT_LIST.postValue(object.getContent());
            }
        }, categoryId, Constants.SIZE_ITEM, name, sort);
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
        },Constants.SIZE_ITEM, createSort, soldSort);
    }
    public void getProfile() {
        viewModel.getProfile(new MainCalback<CustomerResponse>() {
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
            public void doSuccess(CustomerResponse object) {
                ProfileFragment.CUSTOMER = new MutableLiveData<>();
                ProfileFragment.CUSTOMER.setValue(object);
            }
        });
    }
}
