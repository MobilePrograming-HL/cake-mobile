package nix.cake.android.ui.main.shop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.FragmentShopBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;
import nix.cake.android.ui.main.product.find.FindProductActivity;
import nix.cake.android.ui.main.shop.adapter.CategoryTagItemAdapter;

public class ShopFragment extends BaseFragment<FragmentShopBinding, ShopViewModel> implements CategoryTagItemAdapter.OnItemClickListener, ProductItemAdapter.OnItemClickListener {
    public static MutableLiveData<List<CategoryResponse>> CATEGORIES_LIST = new MutableLiveData<>();
    public static MutableLiveData<List<ProductResponse>> PRODUCT_LIST = new MutableLiveData<>();
    private CategoryTagItemAdapter adapter;
    private ProductItemAdapter productAdapter;
    private BottomSheetBehavior sheetBehavior;
    private ArrayAdapter<String> sortAdapter;
    private int selectedPosition = 2;
    private boolean isLoading = false;
    private boolean isSort = false;

    private int currentPage = 0;
    private String selectedCategoryId = "";
    private Handler handler = new Handler(Looper.getMainLooper());
    private String[] sortOptions = {
            "Popular",
            "Newest",
            "Price: Lowest to Highest",
            "Price: Highest to Lowest"
    };

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        viewModel.productList = PRODUCT_LIST;

        setUpObservers();
        setUpBottomSheet();
        setUpAdapterCategory();
        setUpAdapterProduct();
    }

    private void setUpObservers() {
        PRODUCT_LIST.observe(this, products -> {
            if (productAdapter != null) {
                binding.progress.progress.setVisibility(View.INVISIBLE);
                productAdapter.setData(products);
                isLoading = false;
                if (products.isEmpty() && currentPage > 0) {
                    currentPage--;
                }
            }
        });
        CATEGORIES_LIST.observe(this, categories -> {
            if (productAdapter != null) {
                binding.progressCate.progressBar.setVisibility(View.INVISIBLE);
                adapter.setData(categories);
            }
        });
    }
    @Override
    public void onItemClick(CategoryResponse category) {
        isSort = false;
        selectedCategoryId = category.getId();
        currentPage = 0;
        PRODUCT_LIST.setValue(new ArrayList<>());
        binding.progress.progress.setVisibility(View.VISIBLE);
        ((MainActivity) requireActivity()).getListProduct(selectedCategoryId, currentPage);
    }
    public void setUpAdapterProduct() {
        productAdapter = new ProductItemAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        binding.rvProduct.setLayoutManager(gridLayoutManager);
        binding.rvProduct.setAdapter(productAdapter);

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
        if (!isSort) {
            if (isLoading) return;
            isLoading = true;
            currentPage++;
            ((MainActivity) requireActivity()).getListProduct(selectedCategoryId, currentPage);
        }
    }
    public void setUpAdapterCategory() {
        adapter = new CategoryTagItemAdapter(this);
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategory.setAdapter(adapter);
    }
    public void setUpBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.menuSort);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetBehavior.setHideable(true);
        sheetBehavior.setPeekHeight(0);

        sortAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.item_sort_menu, R.id.sort_option, sortOptions) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(R.id.sort_option);

                if (position == selectedPosition) {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    textView.setTypeface(null, Typeface.NORMAL);
                    textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.text));
                } else {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text));
                    textView.setTypeface(null, Typeface.NORMAL);
                    textView.setBackgroundColor(Color.TRANSPARENT);
                }
                return view;
            }
        };
        binding.sortList.setAdapter(sortAdapter);

        binding.sortList.setOnItemClickListener((parent, view, position, id) -> {
            isSort = true;
            selectedPosition = position;
            currentPage = 0;
            PRODUCT_LIST.setValue(new ArrayList<>());
            binding.progress.progress.setVisibility(View.VISIBLE);
            switch (position) {
                case 0: // Popular
                    binding.sortByText.setText(R.string.popular);
                    ((MainActivity) requireActivity()).getListProductSortForShop(selectedCategoryId, "", Constants.DESC, "");
                    break;
                case 1: // Newest
                    binding.sortByText.setText(R.string.newest);
                    ((MainActivity) requireActivity()).getListProductSortForShop(selectedCategoryId, Constants.DESC,"", "");

                    break;
                case 2: // Price: Lowest to Highest
                    binding.sortByText.setText(R.string.price_lowest_to_highest);
                    ((MainActivity) requireActivity()).getListProductSortForShop(selectedCategoryId, "", "", Constants.ASC);
                    break;
                case 3: // Price: Highest to Lowest
                    binding.sortByText.setText(R.string.price_highest_to_lowest);
                    ((MainActivity) requireActivity()).getListProductSortForShop(selectedCategoryId, "", "", Constants.DESC);
                    break;
            }
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            sortAdapter.notifyDataSetChanged();
        });

        // Xử lý khi trạng thái BottomSheet thay đổi
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.overlay.setVisibility(View.GONE);
                    ((MainActivity) requireActivity()).showBottomNav();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.overlay.setAlpha(slideOffset);
                binding.overlay.setVisibility(slideOffset > 0 ? View.VISIBLE : View.GONE);
                if (slideOffset == 0) {
                    ((MainActivity) requireActivity()).showBottomNav();
                }
            }
        });

        binding.overlay.setOnClickListener(v -> {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        });
    }
    public void onSortByClick() {
        ((MainActivity) requireActivity()).hideBottomNav();
        binding.overlay.setVisibility(View.VISIBLE);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void onSearchClick() {
        ((MainActivity) requireActivity()).navigateToSearchFragment();
    };
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shop;
    }
    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
    @Override
    public void onItemClick(ProductResponse product) {
        ((MainActivity) requireActivity()).getProductDetail(product.getId());
    }
}