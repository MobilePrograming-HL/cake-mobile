package nix.cake.android.ui.main.profile.address.detail.fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.databinding.FragmentSelectDistrictBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;
import nix.cake.android.ui.main.profile.address.detail.fragment.adapter.AddressProvinceAdapter;

public class DistrictFragment extends BaseFragment<FragmentSelectDistrictBinding, DistrictViewModel> implements AddressProvinceAdapter.OnItemClickListener {
    public static MutableLiveData<List<NationResponse>> DISTRICT_LIST = new MutableLiveData<>(new ArrayList<>());
    private AddressProvinceAdapter adapter;
    private boolean isLoading = false;
    private static int currentPage = 0;
    private boolean isLastPage = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int PAGE_SIZE = 20;
    private static final int TYPE_DISTRICT = 1;
    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setUpObservers();
        setupRecyclerView();
    }

    private void setUpObservers() {
        DISTRICT_LIST.observe(getViewLifecycleOwner(), districts -> {
            if (adapter != null) {
                if (currentPage == 0) {
                    handleFirstPageData(districts);
                } else {
                    handleNextPageData(districts);
                }

                isLoading = false;
                isLastPage = districts == null || districts.size() < PAGE_SIZE;
            }
        });
    }

    private void handleFirstPageData(List<NationResponse> districts) {
        if (districts == null || districts.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
            adapter.setData(districts);
        }
    }

    private void handleNextPageData(List<NationResponse> districts) {
        if (districts != null && !districts.isEmpty()) {
            adapter.addData(districts);
        }
    }

    private void showEmptyState() {
        if (binding != null) {
            binding.empty.empty.setVisibility(View.VISIBLE);
            binding.rv.setVisibility(View.GONE);
        }
    }

    private void hideEmptyState() {
        if (binding != null) {
            binding.empty.empty.setVisibility(View.GONE);
            binding.rv.setVisibility(View.VISIBLE);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new AddressProvinceAdapter(this);

        binding.rv.setLayoutManager(layoutManager);
        binding.rv.setAdapter(adapter);
        binding.rv.addOnScrollListener(new PaginationScrollListener(layoutManager));
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
        ((AddressDetailActivity) requireActivity()).getListDistrict(AddressDetailActivity.PROVINCE.getParentId(),currentPage);
    }

    public void resetPagination() {
        currentPage = 0;
        isLastPage = false;
        isLoading = false;
    }

    public static void updateNationList(List<NationResponse> newList) {
        if (newList == null) {
            newList = new ArrayList<>();
        }

        List<NationResponse> currentList = DISTRICT_LIST.getValue();
        for (NationResponse nation : newList) {
            if (AddressDetailActivity.ADDRESS_DETAIL != null &&
                    Objects.equals(nation.getId(), AddressDetailActivity.ADDRESS_DETAIL.getDistrict().getId())) {
                nation.setSelected(true);
            }
        }
        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        if (currentPage == 0) {
            DISTRICT_LIST.setValue(newList);
        } else {
            currentList.addAll(newList);
            DISTRICT_LIST.setValue(currentList);
        }
    }
    @Override
    public void onItemClick(NationResponse address) {
        ((AddressDetailActivity) requireActivity()).updateAddress(address, TYPE_DISTRICT);
    }
    
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_district;
    }
    

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
}
