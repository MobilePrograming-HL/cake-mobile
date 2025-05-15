package nix.cake.android.ui.main.profile.address;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.custom.CustomDialog;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.databinding.ActivityShippingAddressBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.ui.main.cart.order.CreateOrderActivity;
import nix.cake.android.ui.main.profile.address.adapter.AddressItemAdapter;
import nix.cake.android.ui.main.profile.address.adapter.AddressSelectItemAdapter;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;

public class ShippingAddressActivity extends BaseActivity<ActivityShippingAddressBinding, ShippingAddressViewModel> implements AddressItemAdapter.OnItemClickListener, AddressSelectItemAdapter.OnItemClickListener {

    public static MutableLiveData<List<AddressResponse>> ADDRESS_LIST = new MutableLiveData<>(new ArrayList<>());
    public static MutableLiveData<Boolean> IS_EMPTY = new MutableLiveData<>(false);
    public static MutableLiveData<String> TYPE = new MutableLiveData<>();
    private AddressItemAdapter adapter;
    private AddressSelectItemAdapter addressSelectItemAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setupRecyclerView();
        setUpObservers();
        startFakeLoading(viewBinding.progressLoadingFirst.progressBar);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        if (TYPE.getValue() != null && TYPE.getValue().equals(Constants.TYPE_CHOOSE_ADDRESS)) {
            addressSelectItemAdapter = new AddressSelectItemAdapter(this);
            viewBinding.rv.setLayoutManager(layoutManager);
            viewBinding.rv.setAdapter(addressSelectItemAdapter);
        } else {
            adapter = new AddressItemAdapter(this);
            viewBinding.rv.setLayoutManager(layoutManager);
            viewBinding.rv.setAdapter(adapter);
        }
    }

    private void setUpObservers() {
        ADDRESS_LIST.observe(this, addresses -> {
            if (adapter != null || addressSelectItemAdapter != null) {
                if (fakeProgressAnimator != null && fakeProgressAnimator.isRunning()) {
                    fakeProgressAnimator.cancel();
                }

                ValueAnimator completeAnimator = ValueAnimator.ofInt(currentProgress, 100);
                completeAnimator.setDuration(300);
                completeAnimator.setInterpolator(new DecelerateInterpolator());
                completeAnimator.addUpdateListener(anim -> {
                    int value = (int) anim.getAnimatedValue();
                    viewBinding.progressLoadingFirst.progressBar.setProgress(value);
                });

                completeAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewBinding.progressLoadingFirst.getRoot().setVisibility(View.GONE);
                        if (Boolean.FALSE.equals(IS_EMPTY.getValue())) {
                            new android.os.Handler(android.os.Looper.getMainLooper()).post(() -> {
                                viewModel.isEmpty.set(false);
                                if (TYPE.getValue() != null && TYPE.getValue().equals(Constants.TYPE_CHOOSE_ADDRESS)) {
                                    addressSelectItemAdapter.setData(addresses != null ? addresses : new ArrayList<>());
                                } else {
                                    adapter.setData(addresses != null ? addresses : new ArrayList<>());
                                }
                            });
                        } else {
                            viewModel.isEmpty.set(true);
                        }
                    }
                });

                completeAnimator.start();
            }
        });
    }

    public void getListAddress(int size) {
        viewModel.getListAddresses(new MainCalback<ResponseListObj<AddressResponse>>() {
            @Override
            public void doError(Throwable error) {
                viewModel.hideLoading();
            }

            @Override
            public void doSuccess() { }

            @Override
            public void doSuccess(ResponseListObj<AddressResponse> object) {
                updateAddressList(object.getContent());

            }

            @Override
            public void doFail() { }
        }, size);
    }

    public static void updateAddressList(List<AddressResponse> newList) {
        if (TYPE.getValue() != null && TYPE.getValue().equals(Constants.TYPE_CHOOSE_ADDRESS)) {
            List<AddressResponse> oldList = ADDRESS_LIST.getValue();
            if (oldList != null) {
                for (AddressResponse oldAddress : oldList) {
                    if (Boolean.TRUE.equals(oldAddress.getIsSelect())) {
                        for (AddressResponse newAddress : newList) {
                            if (oldAddress.getId() != null && oldAddress.getId().equals(newAddress.getId())) {
                                newAddress.setIsSelect(true);
                                break;
                            }
                        }
                    }
                }
            }
            ADDRESS_LIST.setValue(newList);
        }
        else {
            ADDRESS_LIST.setValue(newList);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getListAddress(Constants.SIZE_ITEM);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shipping_address;
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
        ADDRESS_LIST = new MutableLiveData<>();
        IS_EMPTY = new MutableLiveData<>(false);
        TYPE = new MutableLiveData<>();
    }

    public void navigateToAddressDetail() {
        Intent it = new Intent(this, AddressDetailActivity.class);
        startActivity(it);
    }

    public void onCreateAddressClick() {
        AddressDetailActivity.TYPE_HANDLE_ADDRESS = Constants.TYPE_CREATE;
        navigateToAddressDetail();
    }

    @Override
    public void onEditClick(AddressResponse address) {
        navigateToAddressDetail();
        viewModel.getAddressDetail(new MainCalback<AddressResponse>() {
            @Override
            public void doError(Throwable error) { }

            @Override
            public void doSuccess() { }

            @Override
            public void doSuccess(AddressResponse object) {
                AddressDetailActivity.TYPE_HANDLE_ADDRESS = Constants.TYPE_EDIT;
                AddressDetailActivity.ADDRESS_DETAIL = object;
                AddressDetailActivity.PROVINCE = object.getProvince();
                AddressDetailActivity.DISTRICT = object.getDistrict();
                AddressDetailActivity.COMMUNE = object.getCommune();
            }

            @Override
            public void doFail() { }
        }, address.getId());
    }

    @Override
    public void onSelectClick(AddressResponse address) {
        address.setIsSelect(true);
        CreateOrderActivity.addressSelected = address;
        this.finish();
    }

    @Override
    public void onDeleteClick(AddressResponse address, int position) {
        new CustomDialog(this)
                .setTitle(getString(R.string.are_you_sure_to_delete_this_item))
                .setOnClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onDeleteClick() {
                        viewModel.deleteAddress(address.getId());
                        adapter.removeAddress(position);
                        viewModel.isEmpty.set(adapter.isEmptyData());
                    }

                    @Override
                    public void onCancelClick() { }
                })
                .show();
    }

    @Override
    public void onDefaultClick(AddressResponse address, int position) {
        viewModel.setAddressDefault(address.getId());
    }
}
