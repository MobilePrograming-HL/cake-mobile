package nix.cake.android.ui.main.profile.address.detail.fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.databinding.FragmentSelectProvinceBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;
import nix.cake.android.ui.main.profile.address.detail.fragment.adapter.AddressProvinceAdapter;

public class ProvinceFragment extends BaseFragment<FragmentSelectProvinceBinding, ProvinceViewModel> implements AddressProvinceAdapter.OnItemClickListener {
    public static MutableLiveData<List<NationResponse>> PROVINCE_LIST = new MutableLiveData<>(new ArrayList<>());
    private AddressProvinceAdapter adapter;
    private boolean isLoading = false;
    private static int currentPage = 0;
    private boolean isLastPage = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int PAGE_SIZE = 20;
    private static final int TYPE_PROVINCE = 0;
    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setUpObservers();
        setupRecyclerView();
    }

    private void setUpObservers() {
        PROVINCE_LIST.observe(getViewLifecycleOwner(), provinces -> {
            if (adapter != null) {
                if (currentPage == 0) {
                    adapter.setData(provinces);
                } else {
                    adapter.addData(provinces);
                }

                isLoading = false;
                isLastPage = provinces.size() < PAGE_SIZE;
            }
        });
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
        ((AddressDetailActivity) requireActivity()).getListProvince(currentPage);
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

        List<NationResponse> currentList = PROVINCE_LIST.getValue();
        for (NationResponse nation : newList) {
            if (AddressDetailActivity.ADDRESS_DETAIL != null &&
                    Objects.equals(nation.getId(), AddressDetailActivity.ADDRESS_DETAIL.getProvince().getId())) {
                nation.setSelected(true);
            }
        }
        if (currentList == null) {
            currentList = new ArrayList<>();
        }

        if (currentPage == 0) {
            PROVINCE_LIST.setValue(newList);
        } else {
            currentList.addAll(newList);
            PROVINCE_LIST.setValue(currentList);
        }
    }
    @Override
    public void onItemClick(NationResponse address) {
        ((AddressDetailActivity) requireActivity()).updateAddress(address, TYPE_PROVINCE);
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_province;
    }

    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

//    private void setupSearchView() {
//        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                resetPagination(); // Reset pagination when searching
//                adapter.filter(newText);
//                return true;
//            }
//        });
//    }