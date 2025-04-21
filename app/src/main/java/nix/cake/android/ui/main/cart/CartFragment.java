package nix.cake.android.ui.main.cart;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.cart.CartResponse;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.databinding.FragmentCartBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.cart.adapter.CartItemAdapter;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;
import nix.cake.android.utils.DisplayUtils;

public class CartFragment extends BaseFragment<FragmentCartBinding, CartViewModel> implements CartItemAdapter.OnItemClickListener {

    private CartItemAdapter adapter;
    private MutableLiveData<Boolean> isChecked = new MutableLiveData<>(false);
    public static MutableLiveData<CartResponse> CART = new MutableLiveData<>();
    public LiveData<Boolean> getIsChecked() {
        return isChecked;
    }

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setupAdapter();
        setUpObservers();

    }
    private void setUpObservers() {
        CART.observe(getViewLifecycleOwner(), cart -> {
            if (cart != null && adapter != null) {
                adapter.setData(cart.getCartItems());
                binding.progress.progress.setVisibility(View.GONE);
                totalMoney();
            }
        });
    }

    private void setupAdapter(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new CartItemAdapter(this);
        binding.rv.setLayoutManager(layoutManager);
        binding.rv.setAdapter(adapter);
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
    public void onDelete(CartItemResponse cart) {
        ((MainActivity) requireActivity()).hideKeyboard();
    }

    @Override
    public void onSelectClick(CartItemResponse cartItem) {
        ((MainActivity) requireActivity()).hideKeyboard();
        Objects.requireNonNull(CART.getValue()).getCartItems().stream()
                .filter(item -> Objects.equals(item.getId(), cartItem.getId()))
                .findFirst()
                .ifPresent(item -> item.setIsSelect(cartItem.getIsSelect()));
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

}
