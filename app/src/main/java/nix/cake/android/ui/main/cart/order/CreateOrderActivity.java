package nix.cake.android.ui.main.cart.order;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.cart.CartItemResponse;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.ActivityCreateOrderBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.ui.main.cart.order.adapter.OrderCreateItemAdapter;
import nix.cake.android.ui.main.profile.address.ShippingAddressActivity;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;
import nix.cake.android.utils.DisplayUtils;

public class CreateOrderActivity extends BaseActivity<ActivityCreateOrderBinding, CreateOrderViewModel> implements OrderCreateItemAdapter.OnItemClickListener {
    public static MutableLiveData<List<AddressResponse>> ADDRESS_LIST = new MutableLiveData<>();
    public static MutableLiveData<List<CartItemResponse>> ITEM_LIST = new MutableLiveData<>();
    public static MutableLiveData<Boolean> IS_EMPTY_ADDRESS = new MutableLiveData<>(false);
    public static MutableLiveData<Integer> TYPE_ORDER = new MutableLiveData<>(Constants.TYPE_ORDER_OTHER);
    public static AddressResponse addressSelected;
    private OrderCreateItemAdapter orderCreateItemAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setUpObserversAddress();
        setUpObserversItem();
        setUpAdapterItem();
        setUpShippingFee();
        setUpPaymentMethod();
    }

    public void setUpShippingFee() {
        viewBinding.setStandard(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.standard_fee.getValue())));
        viewBinding.setFast(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.fast_fee.getValue())));
        viewBinding.shippingFee.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.shipping_fee.getValue())));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH);

        Calendar fastStart = Calendar.getInstance();
        fastStart.add(Calendar.DATE, 2);
        Calendar fastEnd = Calendar.getInstance();
        fastEnd.add(Calendar.DATE, 3);
        String fastDay = sdf.format(fastStart.getTime()) + " - " + sdf.format(fastEnd.getTime());

        Calendar standardStart = Calendar.getInstance();
        standardStart.add(Calendar.DATE, 3);
        Calendar standardEnd = Calendar.getInstance();
        standardEnd.add(Calendar.DATE, 4);
        String standardDay = sdf.format(standardStart.getTime()) + " - " + sdf.format(standardEnd.getTime());

        viewModel.fast_day.setValue(fastDay);
        viewModel.standard_day.setValue(standardDay);

        viewBinding.setStandardDay(standardDay);
        viewBinding.setFastDay(fastDay);

        viewBinding.standardShipping.setOnClickListener(v -> {
            viewModel.total.setValue(viewModel.total.getValue() - viewModel.shipping_fee.getValue() + viewModel.standard_fee.getValue());
            viewBinding.setTotalPrice(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.total.getValue())));
            viewBinding.shippingFee.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.standard_fee.getValue())));
            viewModel.shipping_fee.setValue(viewModel.standard_fee.getValue());
            viewBinding.ivFast.setImageResource(R.drawable.unchecked);
            viewBinding.ivStandard.setImageResource(R.drawable.checked);
        });

        viewBinding.fastShipping.setOnClickListener(v -> {
            viewModel.total.setValue(viewModel.total.getValue() - viewModel.shipping_fee.getValue() + viewModel.fast_fee.getValue());
            viewBinding.setTotalPrice(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.total.getValue())));
            viewBinding.shippingFee.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.fast_fee.getValue())));
            viewModel.shipping_fee.setValue(viewModel.fast_fee.getValue());
            viewBinding.ivStandard.setImageResource(R.drawable.unchecked);
            viewBinding.ivFast.setImageResource(R.drawable.checked);
        });
    }

    public void setUpPaymentMethod() {
        viewBinding.cod.setOnClickListener(v -> {
            viewModel.paymentMethod.setValue(Constants.COD);
            viewBinding.codImg.setImageResource(R.drawable.checked);
            viewBinding.fiservImg.setImageResource(R.drawable.unchecked);
        });
        viewBinding.fiserv.setOnClickListener(v -> {
            viewModel.paymentMethod.setValue(Constants.FISERV);
            viewBinding.codImg.setImageResource(R.drawable.unchecked);
            viewBinding.fiservImg.setImageResource(R.drawable.checked);
        });
    }
    public void setUpObserversItem() {
        ITEM_LIST.observe(this, items -> {
            if (orderCreateItemAdapter != null) {
                if (items != null && !items.isEmpty()) {
                    Double total = 0.0;
                    orderCreateItemAdapter.setData(items);
                    List<String> cartItemIds = new ArrayList<>();
                    for (CartItemResponse item : items) {
                        total += item.getTotal();
                        cartItemIds.add(item.getId());
                    }
                    viewModel.createOrderRequest.setCartItemIds(cartItemIds);
                    viewModel.total.setValue(total + viewModel.shipping_fee.getValue());
                    viewBinding.setTotalPrice(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(Objects.requireNonNull(viewModel.total.getValue())));
                    viewBinding.retailPrice.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(total));
                }
            }
        });
    }
    public void setUpAdapterItem() {
        orderCreateItemAdapter = new OrderCreateItemAdapter(this);
        viewBinding.rvProduct.setLayoutManager(new LinearLayoutManager(this));
        viewBinding.rvProduct.setAdapter(orderCreateItemAdapter);
    }
    @SuppressLint("SetTextI18n")
    public void setUpObserversAddress() {
        ADDRESS_LIST.observe(this, addresses -> {
            if (addresses != null && !addresses.isEmpty()) {
                if (addressSelected != null && !TextUtils.isEmpty(addressSelected.getId())) {
                    for (AddressResponse address : addresses) {
                        address.setIsSelect(false);
                    }
                    for (AddressResponse address : addresses) {
                        if (address.getId().equals(addressSelected.getId())) {
                            address.setIsSelect(true);
                            viewBinding.name.setText(address.getFullName());
                            viewBinding.phone.setText(address.getPhoneNumber());
                            viewBinding.addressDetails.setText(address.getDetails());
                            viewBinding.address.setText(address.getCommune().getName() + ", " + address.getDistrict().getName()
                                    + ", " + address.getProvince().getName());
                            break;
                        }
                    }
                } else {
                    for (AddressResponse address : addresses) {
                        address.setIsSelect(false);
                    }
                    AddressResponse selectedAddress = addresses.stream()
                            .filter(a -> Boolean.TRUE.equals(a.getIsDefault()))
                            .findFirst()
                            .orElse(addresses.get(0));

                    viewBinding.name.setText(selectedAddress.getFullName());
                    viewBinding.phone.setText(selectedAddress.getPhoneNumber());
                    viewBinding.addressDetails.setText(selectedAddress.getDetails());
                    viewBinding.address.setText(selectedAddress.getCommune().getName() + ", " + selectedAddress.getDistrict().getName()
                            + ", " + selectedAddress.getProvince().getName());

                    addressSelected = selectedAddress;
                    for (AddressResponse address : addresses) {
                        if (address.getId().equals(selectedAddress.getId())) {
                            address.setIsSelect(true);
                            break;
                        }
                    }
                }

            }
        });

        IS_EMPTY_ADDRESS.observe(this, isEmpty -> {
            viewModel.isEmpty.set(isEmpty);
        });
    }
    public void onAddNewAddAddressClick() {
        AddressDetailActivity.TYPE_HANDLE_ADDRESS = Constants.TYPE_CREATE;
        Intent it = new Intent(this, AddressDetailActivity.class);
        startActivity(it);
    }
    public void onChangeAddAddressClick() {
        ShippingAddressActivity.ADDRESS_LIST.setValue(new ArrayList<>(Objects.requireNonNull(ADDRESS_LIST.getValue())));
        ShippingAddressActivity.TYPE.setValue(Constants.TYPE_CHOOSE_ADDRESS);
        Intent it = new Intent(this, ShippingAddressActivity.class);
        startActivity(it);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListAddressForCreateOrder(Constants.SIZE_ITEM);
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
                IS_EMPTY_ADDRESS.setValue(object.getContent().isEmpty());
                ADDRESS_LIST.setValue(object.getContent());
                setUpObserversAddress();
            }
            @Override
            public void doFail() {

            }
        }, size);
    }
    public void onOrderClick() {
        if (TYPE_ORDER.getValue() == Constants.TYPE_ORDER_BUY_NOW) {
            CartItemResponse cartItemResponse = Objects.requireNonNull(ITEM_LIST.getValue()).get(0);
            viewModel.buyNowOrderRequest.setProductId(cartItemResponse.getProduct().getId());
            viewModel.buyNowOrderRequest.setTagId(cartItemResponse.getTag().getId());
            viewModel.buyNowOrderRequest.setQuantity(cartItemResponse.getQuantity());
            viewModel.buyNowOrderRequest.setAddressId(addressSelected.getId());
            viewModel.buyNowOrderRequest.setShippingFee(Objects.requireNonNull(viewModel.shipping_fee.getValue()).intValue());
            viewModel.buyNowOrderRequest.setNote("Note");
            viewModel.buyNowOrderRequest.setPaymentMethod(viewModel.paymentMethod.getValue());

            viewModel.buyNowOrder(new MainCalback<OrderResponse>() {
                @Override
                public void doError(Throwable error) { }

                @Override
                public void doSuccess() { }

                @Override
                public void doSuccess(OrderResponse object) {
                    if (object.getPaymentMethod() == Constants.FISERV && object.getFiservInfo() != null
                            && object.getFiservInfo().getCheckout() != null) {
                        String url = object.getFiservInfo().getCheckout().getRedirectionUrl();
                        if (url != null && !url.isEmpty()) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                            finish();
                            return;
                        }
                    }

                    Intent it = new Intent(CreateOrderActivity.this, OrderSuccessActivity.class);
                    startActivity(it);
                }

                @Override
                public void doFail() { }
            }, viewModel.buyNowOrderRequest);

        } else {
            viewModel.createOrderRequest.setAddressId(addressSelected.getId());
            viewModel.createOrderRequest.setShippingFee(Objects.requireNonNull(viewModel.shipping_fee.getValue()).intValue());
            viewModel.createOrderRequest.setNote("Note");
            viewModel.createOrderRequest.setPaymentMethod(viewModel.paymentMethod.getValue());

            viewModel.createOrder(new MainCalback<OrderResponse>() {
                @Override
                public void doError(Throwable error) { }

                @Override
                public void doSuccess() { }

                @Override
                public void doSuccess(OrderResponse object) {
                    if (object.getPaymentMethod() == Constants.FISERV && object.getFiservInfo() != null
                            && object.getFiservInfo().getCheckout() != null) {
                        String url = object.getFiservInfo().getCheckout().getRedirectionUrl();
                        if (url != null && !url.isEmpty()) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                            finish();
                            return;
                        }
                    }

                    Intent it = new Intent(CreateOrderActivity.this, OrderSuccessActivity.class);
                    startActivity(it);
                }

                @Override
                public void doFail() { }
            }, viewModel.createOrderRequest);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_order;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ADDRESS_LIST = new MutableLiveData<>();
        IS_EMPTY_ADDRESS = new MutableLiveData<>(false);
        ITEM_LIST = new MutableLiveData<>();
        TYPE_ORDER = new MutableLiveData<>(Constants.TYPE_ORDER_OTHER);
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
        addressSelected = new AddressResponse();
        IS_EMPTY_ADDRESS = new MutableLiveData<>(false);
    }

    @Override
    public void onItemClick(CartItemResponse cartItemResponse) {

    }
}
