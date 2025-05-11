package nix.cake.android.ui.main.cart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.custom.CustomDialog;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.cart.CartResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.FragmentCartBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.cart.adapter.CartItemAdapter;
import nix.cake.android.ui.main.cart.order.CreateOrderActivity;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;
import nix.cake.android.utils.DisplayUtils;

public class CartFragment extends BaseFragment<FragmentCartBinding, CartViewModel> implements CartItemAdapter.OnItemClickListener, ProductItemAdapter.OnItemClickListener{

    private CartItemAdapter adapter;
    private ProductItemAdapter productAdapter;
    private MutableLiveData<Boolean> isChecked = new MutableLiveData<>(false);
    public static MutableLiveData<CartResponse> CART = new MutableLiveData<>();
    public static MutableLiveData<List<ProductResponse>> PRODUCT_LIST = new MutableLiveData<>();
    public static MutableLiveData<Boolean> IS_EMPTY= new MutableLiveData<>(false);
    private boolean isLoading = false;
    public static int currentPage = 0;
    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setupAdapter();
        setUpObservers();
        setUpAdapterProduct();
        setUpObserversProduct();

        startFakeLoading(binding.progressLoadingFirst.progressBar);
    }
    private void setUpObservers() {
        CART.observe(getViewLifecycleOwner(), cart -> {
            if (cart != null && adapter != null) {
                List<CartItemResponse> cartItems = cart.getCartItems();
                adapter.setData(cart.getCartItems());
                if (cart.getCartItems().isEmpty()) {
                    viewModel.isEmpty.set(true);
                } else {
                    viewModel.isEmpty.set(false);
                }
                if (viewModel.isCheckAll.get()) {
                    boolean allSelected = cartItems.stream().allMatch(CartItemResponse::getIsSelect);
                    if (!allSelected) {
                        viewModel.isCheckAll.set(false);
                        binding.checked.setImageResource(R.drawable.unchecked);
                    }
                }
                totalMoney();
            }
        });
        IS_EMPTY.observe(getViewLifecycleOwner(), isEmpty -> {
            if (isEmpty) {
                viewModel.isEmpty.set(true);
            }
        });

    }
    private void setUpObserversProduct() {
        PRODUCT_LIST.observe(this, products -> {
            if (productAdapter != null && currentPage > 0 && !products.isEmpty()) {
                productAdapter.setData(products);
                isLoading = false;
                if (products.isEmpty() && currentPage > 0) {
                    currentPage--;
                }

            }

            if (productAdapter != null && currentPage == 0 && !products.isEmpty()) {
                if (fakeProgressAnimator != null && fakeProgressAnimator.isRunning()) {
                    fakeProgressAnimator.cancel();
                }

                ValueAnimator completeAnimator = ValueAnimator.ofInt(currentProgress, 100);
                completeAnimator.setDuration(300);
                completeAnimator.setInterpolator(new DecelerateInterpolator());
                completeAnimator.addUpdateListener(anim -> {
                    int value = (int) anim.getAnimatedValue();
                    binding.progressLoadingFirst.progressBar.setProgress(value);
                });

                completeAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.progressLoadingFirst.getRoot().setVisibility(View.GONE);
                        isLoading = false;

                        List<ProductResponse> shortList = products.size() > 10 ? products.subList(0, 10) : products;

                        new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                            productAdapter.setData(shortList);
                        });
                    }
                });

                completeAnimator.start();
            }
        });
    }
    private void setupAdapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new CartItemAdapter(this);
        binding.rv.setLayoutManager(layoutManager);
        binding.rv.setAdapter(adapter);
    }

    public void setUpAdapterProduct() {
        productAdapter = new ProductItemAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        binding.rvProduct.setLayoutManager(gridLayoutManager);
        binding.rvProduct.setAdapter(productAdapter);

        // ⚡ Tăng hiệu suất scroll
        binding.rvProduct.setItemViewCacheSize(10);
        binding.rvProduct.setHasFixedSize(true);

        binding.rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        });
    }
    private void loadMoreData() {
        if (isLoading) return;
        isLoading = true;
        currentPage++;
        ((MainActivity) requireActivity()).getListProductForCart(null, currentPage);
    }
    @SuppressLint("ResourceAsColor")
    public void onAllClick() {
        if (isChecked.getValue() != null && isChecked.getValue()) {
            isChecked.setValue(false);
            binding.checked.setImageResource(R.drawable.unchecked);
        } else {
            isChecked.setValue(true);
            binding.checked.setImageResource(R.drawable.checked);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public static void updateCartItemList(CartResponse cart) {
        if (CART == null) {
            CART.setValue(cart);
        } else {
            CartResponse currentCart = CART.getValue();
            if (currentCart != null && currentCart.getCartItems() != null && cart.getCartItems() != null) {
                for (CartItemResponse newItem : cart.getCartItems()) {
                    CartItemResponse currentItem = currentCart.getCartItems().stream()
                            .filter(item -> item.getId().equals(newItem.getId()))
                            .findFirst()
                            .orElse(null);
                    if (currentItem != null && currentItem.getIsSelect()) {
                        newItem.setIsSelect(true);
                    }
                }
            }
            CART.setValue(cart);

        }
    }

    @Override
    public void onItemClick(CartItemResponse cart) {
        ((MainActivity) requireActivity()).hideKeyboard();
        ((MainActivity) requireActivity()).getProductDetail(cart.getProduct().getId());
    }

    @Override
    public void onPlusItem(CartItemResponse cartItem) {
        ((MainActivity) requireActivity()).hideKeyboard();
        Objects.requireNonNull(CART.getValue()).getCartItems().stream()
                .filter(item -> Objects.equals(item.getId(), cartItem.getId()))
                .findFirst()
                .ifPresent(item -> item.setQuantity(cartItem.getQuantity()));
        ((MainActivity) requireActivity()).updateCartItem(cartItem);
        totalMoney();
    }

    @Override
    public void onMinusItem(CartItemResponse cartItem) {
        ((MainActivity) requireActivity()).hideKeyboard();
        Objects.requireNonNull(CART.getValue()).getCartItems().stream()
                .filter(item -> Objects.equals(item.getId(), cartItem.getId()))
                .findFirst()
                .ifPresent(item -> item.setQuantity(cartItem.getQuantity()));
        ((MainActivity) requireActivity()).updateCartItem(cartItem);
        totalMoney();
    }

    @Override
    public void onTextCountItemChange(CartItemResponse cartItem) {
        Objects.requireNonNull(CART.getValue()).getCartItems().stream()
                .filter(item -> Objects.equals(item.getId(), cartItem.getId()))
                .findFirst()
                .ifPresent(item -> item.setQuantity(cartItem.getQuantity()));
        ((MainActivity) requireActivity()).updateCartItem(cartItem);
        totalMoney();
    }

    @Override
    public void onDelete(CartItemResponse item, int position) {
        ((MainActivity) requireActivity()).hideKeyboard();
        new CustomDialog(getContext())
                .setTitle(getString(R.string.are_you_sure_to_delete_this_item))
                .setOnClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onDeleteClick() {
                        adapter.removeCartItem(position);
                        ((MainActivity) requireActivity()).deleteCartItem(item);
                        viewModel.isEmpty.set(adapter.isEmptyData());
                        totalMoney();
                    }

                    @Override
                    public void onCancelClick() {
                    }
                })
                .show();
    }

    @Override
    public void onSelectClick(CartItemResponse cartItem) {
        ((MainActivity) requireActivity()).hideKeyboard();

        List<CartItemResponse> cartItems = Objects.requireNonNull(CART.getValue()).getCartItems();

        cartItems.stream()
                .filter(item -> Objects.equals(item.getId(), cartItem.getId()))
                .findFirst()
                .ifPresent(item -> item.setIsSelect(cartItem.getIsSelect()));

        boolean allSelected = cartItems.stream().allMatch(CartItemResponse::getIsSelect);

        if (allSelected) {
            viewModel.isCheckAll.set(true);
            binding.checked.setImageResource(R.drawable.checked);
        } else {
            viewModel.isCheckAll.set(false);
            binding.checked.setImageResource(R.drawable.unchecked);
        }

        totalMoney();
    }

    public void totalMoney() {
        double total = 0.0;
        total = Objects.requireNonNull(CART.getValue()).getCartItems().stream()
                .filter(CartItemResponse::getIsSelect)
                .mapToDouble(CartItemResponse::getTotal)
                .sum();
        viewModel.total.postValue(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(total));

    }
    public void onCheckAllClick() {
        if (CART == null || CART.getValue() == null) {
            return;
        }
        boolean isAllChecked = viewModel.isCheckAll.get();
        viewModel.isCheckAll.set(!isAllChecked);
        binding.checked.setImageResource(isAllChecked ? R.drawable.unchecked : R.drawable.checked);

        List<CartItemResponse> cartItems = CART.getValue().getCartItems();
        for (CartItemResponse item : cartItems) {
            item.setIsSelect(!isAllChecked);
        }
        adapter.notifyDataSetChanged();
        totalMoney();
    }

    public void onCheckOutClick() {
        List<CartItemResponse> cartItems = Objects.requireNonNull(CART.getValue()).getCartItems()
                .stream()
                .filter(CartItemResponse::getIsSelect)
                .collect(Collectors.toList());

        if (cartItems.isEmpty()) {
            viewModel.showNormalMessage("Please select at least one item");
            return;
        }

        CreateOrderActivity.ITEM_LIST.setValue(cartItems);
        startActivity(new Intent(getContext(), CreateOrderActivity.class));
        ((MainActivity) requireActivity()).getListAddressForCreateOrder(Constants.SIZE_ITEM);
    }

    public void onGoToShopClick() {
        ((MainActivity) requireActivity()).getListCategories();
    }

    @Override
    public void onItemClick(ProductResponse product) {
        ((MainActivity) requireActivity()).getProductDetail(product.getId());
    }
}
