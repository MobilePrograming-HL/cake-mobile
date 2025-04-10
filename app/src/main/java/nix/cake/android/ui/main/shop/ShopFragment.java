package nix.cake.android.ui.main.shop;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.FragmentShopBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;
import nix.cake.android.ui.main.product.find.FindProductActivity;
import nix.cake.android.ui.main.shop.adapter.CategoryTagItemAdapter;

public class ShopFragment extends BaseFragment<FragmentShopBinding, ShopViewModel> {
    public static List<CategoryResponse> CATEGORIES_LIST;
    public static List<ProductResponse> PRODUCT_LIST;
    private CategoryTagItemAdapter adapter;

    private ProductItemAdapter productAdapter;

    private BottomSheetBehavior sheetBehavior;
    private ArrayAdapter<String> sortAdapter;
    private int selectedPosition = 3;
    private String[] sortOptions = {
            "Popular",
            "Newest",
            "Customer Review",
            "Price: Lowest to Highest",
            "Price: Highest to Lowest"
    };
    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        viewModel.categoriesList = CATEGORIES_LIST;
        viewModel.productList = PRODUCT_LIST;
        setUpBottomSheet();
        setUpAdapterCategory();
    }

    public void setUpAdapterCategory() {
        if (viewModel.categoriesList == null) {
            return;
        }
        adapter = new CategoryTagItemAdapter();
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategory.setAdapter(adapter);
        adapter.setData(viewModel.categoriesList);

        if (viewModel.productList == null) {
            return;
        }
        productAdapter = new ProductItemAdapter();
        binding.rvProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvProduct.setAdapter(productAdapter);
        productAdapter.setData(viewModel.productList);
    }
    public void setUpAdapterProduct() {
        if (viewModel.productList == null) {
            return;
        }
        productAdapter = new ProductItemAdapter();
        binding.rvProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvProduct.setAdapter(productAdapter);
        productAdapter.setData(viewModel.productList);
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
                    textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.textWarning));
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
            selectedPosition = position;

            String selectedOption = sortOptions[position];
            switch (position) {
                case 0: // Popular
                    // Xử lý sort theo popular
                    break;
                case 1: // Newest
                    // Xử lý sort theo newest
                    break;
                case 2: // Customer Review
                    // Xử lý sort theo review
                    break;
                case 3: // Price: Lowest to Highest
                    // Xử lý sort giá thấp đến cao
                    break;
                case 4: // Price: Highest to Lowest
                    // Xử lý sort giá cao đến thấp
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
        Intent it = new Intent(getContext(), FindProductActivity.class);
        startActivity(it);
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
}