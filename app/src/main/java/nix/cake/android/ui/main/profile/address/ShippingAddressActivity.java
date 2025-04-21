package nix.cake.android.ui.main.profile.address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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
import nix.cake.android.ui.main.profile.address.adapter.AddressItemAdapter;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;
import nix.cake.android.ui.main.profile.address.detail.fragment.DistrictFragment;

public class ShippingAddressActivity extends BaseActivity<ActivityShippingAddressBinding, ShippingAddressViewModel> implements AddressItemAdapter.OnItemClickListener {
    public static MutableLiveData<List<AddressResponse>> ADDRESS_LIST = new MutableLiveData<>(new ArrayList<>());
    private AddressItemAdapter adapter;
    private boolean isLoading = false;
    private static int currentPage = 0;
    private boolean isLastPage = false;
    private static final int PAGE_SIZE = 20;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setupRecyclerView();
        setUpObservers();
    }
    private void setUpObservers() {
        ADDRESS_LIST.observe(this, addresses -> {
            if (adapter != null) {
                if (currentPage == 0) {
                    handleFirstPageData(addresses);
                } else {
                    handleNextPageData(addresses);
                }

                isLoading = false;
                isLastPage = addresses == null || addresses.size() < PAGE_SIZE;
            }
        });
    }

    private void handleFirstPageData(List<AddressResponse> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter.setData(addresses);
        }
    }

    private void handleNextPageData(List<AddressResponse> addresses) {
        if (addresses != null && !addresses.isEmpty()) {
            adapter.addData(addresses);
        }
    }

    private void showEmptyState() {
        if (viewBinding != null) {
            viewBinding.empty.empty.setVisibility(View.VISIBLE);
            viewBinding.rv.setVisibility(View.GONE);
        }
    }

    private void hideEmptyState() {
        if (viewBinding != null) {
            viewBinding.empty.empty.setVisibility(View.GONE);
            viewBinding.rv.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new AddressItemAdapter(this);

        viewBinding.rv.setLayoutManager(layoutManager);
        viewBinding.rv.setAdapter(adapter);
        viewBinding.rv.addOnScrollListener(new PaginationScrollListener(layoutManager));
    }

    private class PaginationScrollListener extends RecyclerView.OnScrollListener {
        private final LinearLayoutManager layoutManager;
        private static final int VISIBLE_THRESHOLD = 5;

        public PaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition + VISIBLE_THRESHOLD) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    loadMoreData();
                }
            }
        }
    }

    private void loadMoreData() {
        isLoading = true;
        currentPage++;
        getListAddress(currentPage);
    }
    public static void updateAddressList(List<AddressResponse> newList) {
        if (newList == null) {
            newList = new ArrayList<>();
        }

        List<AddressResponse> currentList = ADDRESS_LIST.getValue();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        if (currentPage == 0) {
            ADDRESS_LIST.setValue(newList);
        } else {
            currentList.addAll(newList);
            ADDRESS_LIST.setValue(currentList);
        }
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
                updateAddressList(object.getContent());
            }
            @Override
            public void doFail() {

            }
        }, page);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 0;
        getListAddress(currentPage);
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
        viewModel.getAddressDetail(new MainCalback<AddressResponse>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(AddressResponse object) {
                AddressDetailActivity.TYPE_HANDLE_ADDRESS = Constants.TYPE_EDIT;
                AddressDetailActivity.ADDRESS_DETAIL = object;
                AddressDetailActivity.PROVINCE = object.getProvince();
                AddressDetailActivity.DISTRICT = object.getDistrict();
                AddressDetailActivity.COMMUNE = object.getCommune();
                navigateToAddressDetail();
            }

            @Override
            public void doFail() {

            }
        }, address.getId());

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
                    }

                    @Override
                    public void onCancelClick() {
                    }
                })
                .show();
    }

    @Override
    public void onDefaultClick(AddressResponse address, int position) {
        viewModel.setAddressDefault(address.getId());
    }
}
