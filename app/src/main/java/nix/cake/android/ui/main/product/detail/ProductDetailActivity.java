package nix.cake.android.ui.main.product.detail;

import static nix.cake.android.utils.DisplayUtils.convertDoubleTwoDecimalsHasCurrency;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.cart.CartRequest;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.product.TagResponse;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.databinding.ActivityProductDetailBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.ui.main.cart.order.CreateOrderActivity;
import nix.cake.android.ui.main.login.LoginActivity;
import nix.cake.android.ui.main.product.detail.adapter.ImageSliderAdapter;
import nix.cake.android.ui.main.product.detail.adapter.ProductTagItemAdapter;
public class ProductDetailActivity extends BaseActivity<ActivityProductDetailBinding, ProductDetailViewModel> implements ProductTagItemAdapter.OnItemClickListener, View.OnClickListener {

    public static ProductResponse PRODUCT_DETAIL;
    private ImageSliderAdapter imageSliderAdapter;
    private ProductTagItemAdapter productTagItemAdapter;
    private BottomSheetBehavior sheetBehavior;
    private Integer TYPE_HANDLE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        viewModel.productDetail = PRODUCT_DETAIL;
        setUpProductDetail();
        setUpCartOption();
        setUpBottomSheet();
    }

    @SuppressLint("SetTextI18n")
    public void setUpProductDetail() {
        viewBinding.setItem(viewModel.productDetail);

        imageSliderAdapter = new ImageSliderAdapter(this, viewModel.productDetail.getImages());
        viewBinding.img.setAdapter(imageSliderAdapter);
        viewBinding.setSold(viewModel.productDetail.getTotalSold().toString());
        viewBinding.img.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String imageCount = (position + 1) + "/" + viewModel.productDetail.getImages().size();
                viewBinding.tvImageCount.setText(imageCount);
            }
        });

        viewBinding.tvImageCount.setText("1/" + viewModel.productDetail.getImages().size());

        if (viewModel.productDetail.getDiscount() == null) {
            viewBinding.priceAfterSale.setVisibility(View.GONE);
            viewBinding.price.setText(convertDoubleTwoDecimalsHasCurrency(viewModel.productDetail.getPrice()));
            viewBinding.priceBuy.setText(convertDoubleTwoDecimalsHasCurrency(viewModel.productDetail.getPrice()));

        } else {
            viewBinding.priceAfterSale.setVisibility(View.VISIBLE);
            viewBinding.price.setText(convertDoubleTwoDecimalsHasCurrency(viewModel.productDetail.getPrice()));
            viewBinding.price.setPaintFlags(viewBinding.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            double discountedPrice = viewModel.productDetail.getPrice() * (1 - viewModel.productDetail.getDiscount().getDiscountPercentage() / 100.0);
            viewBinding.priceAfterSale.setText(convertDoubleTwoDecimalsHasCurrency(discountedPrice));
            viewBinding.priceBuy.setText(convertDoubleTwoDecimalsHasCurrency(discountedPrice));
        }

        Map<Integer, Long> reviewCountByRate = viewModel.productDetail.getReviewStats().getReviewCountByRate();

        for (int i = 1; i <= 5; i++) {
            Long count = reviewCountByRate.getOrDefault(i, 0L);
            String text = String.valueOf(count);

            switch (i) {
                case 1:
                    viewBinding.rating1.setText(text);
                    break;
                case 2:
                    viewBinding.rating2.setText(text);
                    break;
                case 3:
                    viewBinding.rating3.setText(text);
                    break;
                case 4:
                    viewBinding.rating4.setText(text);
                    break;
                case 5:
                    viewBinding.rating5.setText(text);
                    break;
            }
        }
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    public void setUpCartOption() {
        Glide.with(this)
                .load(viewModel.productDetail.getImages().get(0))
                .placeholder(R.drawable.cake_default)
                .error(R.drawable.cake_default)
                .into(viewBinding.imageCart);

        if (viewModel.productDetail.getDiscount() == null) {
            viewBinding.priceCart.setText(convertDoubleTwoDecimalsHasCurrency(viewModel.productDetail.getPrice()));
        } else {
            double discountedPrice = viewModel.productDetail.getPrice() * (1 - viewModel.productDetail.getDiscount().getDiscountPercentage() / 100.0);
            viewBinding.priceCart.setText(convertDoubleTwoDecimalsHasCurrency(discountedPrice));
        }

        productTagItemAdapter = new ProductTagItemAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        viewBinding.rvProductTag.setLayoutManager(gridLayoutManager);
        viewBinding.rvProductTag.setAdapter(productTagItemAdapter);
        productTagItemAdapter.setData(viewModel.productDetail.getTags());

        viewBinding.countItemCart.setText(viewModel.quantity.toString());
        viewBinding.countItemCart.setSelection(viewBinding.countItemCart.getText().length());

        viewBinding.countItemCart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (!input.isEmpty()) {
                    try {
                        int value = Integer.parseInt(input);
                        if (value < 1) {
                            viewBinding.countItemCart.setText("1");
                            viewBinding.countItemCart.setSelection(viewBinding.countItemCart.getText().length());
                            viewModel.quantity = 1;
                        } else if (value > viewModel.productDetail.getQuantity()) {
                            viewBinding.countItemCart.setText(viewModel.productDetail.getQuantity().toString());
                            viewBinding.countItemCart.setSelection(viewBinding.countItemCart.getText().length());
                            viewModel.quantity = Integer.parseInt(viewModel.productDetail.getQuantity().toString());
                        } else {
                            viewModel.quantity = value;
                        }
                    } catch (NumberFormatException e) {
                        viewBinding.countItemCart.setText("1");
                        viewBinding.countItemCart.setSelection(viewBinding.countItemCart.getText().length());
                        viewModel.quantity = 1;
                    }
                }
            }
        });

        viewBinding.countItemCart.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {

                String input = viewBinding.countItemCart.getText().toString();
                if (input.isEmpty()) {
                    viewBinding.countItemCart.setText("1");
                    viewBinding.countItemCart.setSelection(viewBinding.countItemCart.getText().length());
                    viewModel.quantity = 1;
                }
            }
        });
    }

    public void setUpBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(viewBinding.addToCartOption);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setHideable(true);
        sheetBehavior.setPeekHeight(0);

        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    viewBinding.overlay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                viewBinding.overlay.setAlpha(slideOffset);
                viewBinding.overlay.setVisibility(slideOffset > 0 ? View.VISIBLE : View.GONE);
            }
        });

        viewBinding.overlay.setOnClickListener(v -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
    }

    public void onBuyNowOrAddToCartClick() {
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        hideKeyboard();
        if (TYPE_HANDLE == R.string.add_to_cart) {
            handleAddToCart();
        } else {
            handleBuyNow();
        }

    }

    private void handleBuyNow() {
        List<CartItemResponse> cartItems = new ArrayList<>();
        CartItemResponse cartItem = new CartItemResponse();
        nix.cake.android.data.model.api.response.cart.ProductResponse product = new nix.cake.android.data.model.api.response.cart.ProductResponse();
        product.setId(viewModel.productDetail.getId());
        product.setName(viewModel.productDetail.getName());
        product.setPrice(viewModel.productDetail.getPriceOfProduct());
        product.setImage(viewModel.productDetail.getImages().get(0));
        product.setImages(viewModel.productDetail.getImages());
        cartItem.setProduct(product);
        cartItem.setQuantity(viewModel.quantity);
        cartItem.setTag(viewModel.tag);

        if (viewModel.tag != null) {
            cartItem.setTag(viewModel.tag);
            cartItems.add(cartItem);
            CreateOrderActivity.ITEM_LIST.setValue(cartItems);
            CreateOrderActivity.TYPE_ORDER.setValue(Constants.TYPE_ORDER_BUY_NOW);
            startActivity(new Intent(this, CreateOrderActivity.class));
            getListAddressForCreateOrder(Constants.SIZE_ITEM);

        } else {
            viewModel.showNormalMessage("Please select flavor!");
        }


    }

    private void handleAddToCart() {
        CartRequest request = new CartRequest();
        request.setProductId(viewModel.productDetail.getId());
        request.setQuantity(viewModel.quantity);

        if (viewModel.tag != null) {
            request.setTagId(viewModel.tag.getId());
            viewModel.addCartItem(request);
        } else {
            viewModel.showNormalMessage("Please select flavor!");
        }

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
    public void onBuyNowClick() {
        if (isLogin()) {
            TYPE_HANDLE = R.string.buy_now;
            viewBinding.typeHandle.setText(TYPE_HANDLE);

            viewBinding.overlay.setVisibility(View.VISIBLE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            showLoginDialog();
        }
    }

    public void onAddToCartClick() {
        if (isLogin()) {
            TYPE_HANDLE = R.string.add_to_cart;
            viewBinding.typeHandle.setText(TYPE_HANDLE);
            viewBinding.overlay.setVisibility(View.VISIBLE);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            showLoginDialog();
        }

    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.productDetail = null;
        PRODUCT_DETAIL = null;
    }

    @Override
    public void onItemClick(TagResponse tag) {
        hideKeyboard();
        viewModel.tag = tag;
    }

    @SuppressLint("SetTextI18n")
    public void onRemoveClick() {
        if (viewModel.quantity != 1) {
            viewModel.quantity--;
            viewBinding.countItemCart.setText(viewModel.quantity.toString());
        }
        hideKeyboard();
    }

    @SuppressLint("SetTextI18n")
    public void onAddClick() {
        if (viewModel.quantity < viewModel.productDetail.getQuantity()) {
            viewModel.quantity++;
            viewBinding.countItemCart.setText(viewModel.quantity.toString());
        }
        hideKeyboard();
    }

    @Override
    public void onClick(View view) {
        hideKeyboard();
    }

    public void navigateToLogin() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }
    private void showLoginDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Required")
                .setMessage("You need to be logged in to perform this action. Would you like to log in now?")
                .setPositiveButton("Yes", (dialog, which) -> navigateToLogin())
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

}