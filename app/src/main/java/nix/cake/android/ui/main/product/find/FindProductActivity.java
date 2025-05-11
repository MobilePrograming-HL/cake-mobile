package nix.cake.android.ui.main.product.find;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.ActivityFindProductBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.ui.main.home.adapter.ItemCategoryHomeAdapter;
import nix.cake.android.ui.main.product.adapter.CategoryFilterItemAdapter;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;
import nix.cake.android.ui.main.product.detail.ProductDetailActivity;
import nix.cake.android.utils.DisplayUtils;

public class FindProductActivity extends BaseActivity<ActivityFindProductBinding, FindProductViewModel> implements ProductItemAdapter.OnItemClickListener, CategoryFilterItemAdapter.OnItemClickListener, ItemCategoryHomeAdapter.OnItemClickListener {
    public static MutableLiveData<List<CategoryResponse>> CATEGORY_LIST = new MutableLiveData<>();
    public static MutableLiveData<List<ProductResponse>> PRODUCT_LIST = new MutableLiveData<>();
    public static MutableLiveData<String> SEARCH_KEY = new MutableLiveData<>();
    public static MutableLiveData<String> CATE_ID = new MutableLiveData<>();
    public static MutableLiveData<String> SORT = new MutableLiveData<>("asc");
    public ItemCategoryHomeAdapter categoryTagItemAdapter;
    private ProductItemAdapter productAdapter;
    private CategoryFilterItemAdapter categoryFilterItemAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setUpProductAdapter();
        setUpObserversProduct();
        viewBinding.search.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                viewModel.isSearch.set(true);
            }
        });

        viewBinding.sort.setOnClickListener(v -> {
            if (Objects.equals(SORT.getValue(), "asc")) {
                SORT.postValue("desc");
                viewBinding.tvSort.setText(getString(R.string.price_highest_to_lowest));
                getSortProduct(CATE_ID.getValue(), viewBinding.search.getText().toString(), viewModel.startPrice.getValue(), viewModel.endPrice.getValue(), "desc");
            } else {
                SORT.postValue("asc");
                viewBinding.tvSort.setText(getString(R.string.price_lowest_to_highest));
                getSortProduct(CATE_ID.getValue(), viewBinding.search.getText().toString(), viewModel.startPrice.getValue(), viewModel.endPrice.getValue(), "asc");
            }
        });
    }
    public void setUpObserversProduct() {
        PRODUCT_LIST.observe(this, productList -> {
            if (productAdapter == null && productList == null) return;

            viewModel.isEmpty.set(productList.isEmpty());
            viewModel.isLoading.set(false);
            productAdapter.setData(productList);
            if (Objects.equals(SORT.getValue(), "asc")) {
                viewBinding.tvSort.setText(getString(R.string.price_lowest_to_highest));
            } else {
                viewBinding.tvSort.setText(getString(R.string.price_highest_to_lowest));
            }

            for (CategoryResponse category : Objects.requireNonNull(CATEGORY_LIST.getValue())) {
                category.setIsSelected(category.getId().equals(CATE_ID.getValue()));
            }

            double minPrice = productList.stream()
                    .filter(p -> p.getPrice() != null)
                    .mapToDouble(ProductResponse::getPrice)
                    .min()
                    .orElse(Double.MAX_VALUE);

            double maxPrice = productList.stream()
                    .filter(p -> p.getPrice() != null)
                    .mapToDouble(ProductResponse::getPrice)
                    .max()
                    .orElse(Double.MIN_VALUE);

            viewModel.minPrice.setValue(minPrice != Double.MAX_VALUE ? minPrice : null);
            viewModel.maxPrice.setValue(maxPrice != Double.MIN_VALUE ? maxPrice : null);
            viewModel.startPrice.setValue(minPrice != Double.MAX_VALUE ? minPrice : null);
            viewModel.endPrice.setValue(maxPrice != Double.MIN_VALUE ? maxPrice : null);
            if (!productList.isEmpty()) {
                setUpFilter();
            }

        });
    }
    public void setUpObserversCategory() {
        CATEGORY_LIST.observe(this, categoryResponses -> {
            if (categoryResponses != null) {
                setUpCategoryAdapter();
                setUpAdapterCategorySearch();

                categoryFilterItemAdapter.setData(categoryResponses);
                categoryTagItemAdapter.setData(categoryResponses);
            }
        });
    }
    public void setUpAdapterCategorySearch() {
        categoryTagItemAdapter = new ItemCategoryHomeAdapter(this);
        viewBinding.rvCategorySearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        viewBinding.rvCategorySearch.setAdapter(categoryTagItemAdapter);
    }
    public void setUpProductAdapter() {
        productAdapter = new ProductItemAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columns
        viewBinding.rvProduct.setLayoutManager(gridLayoutManager);
        viewBinding.rvProduct.setAdapter(productAdapter);
    }

    public void setUpCategoryAdapter() {
        categoryFilterItemAdapter = new CategoryFilterItemAdapter(this);
        FlexboxLayoutManager flexboxLayoutManager  = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

        viewBinding.rvCategory.setLayoutManager(flexboxLayoutManager);
        viewBinding.rvCategory.setAdapter(categoryFilterItemAdapter);
    }

    public void setUpFilter() {
        viewBinding.priceRangeSlider.setValueFrom(Float.parseFloat(String.valueOf(viewModel.minPrice.getValue())));
        viewBinding.priceRangeSlider.setValueTo(Float.parseFloat(String.valueOf(viewModel.maxPrice.getValue())));
        viewBinding.priceRangeSlider.setValues(Objects.requireNonNull(viewModel.minPrice.getValue()).floatValue(), Objects.requireNonNull(viewModel.maxPrice.getValue()).floatValue());

        List<Float> initialValues = viewBinding.priceRangeSlider.getValues();
        viewBinding.start.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(initialValues.get(0).doubleValue()));
        viewBinding.end.setText(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(initialValues.get(1).doubleValue()));

        viewBinding.priceRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            List<Float> values = slider.getValues();
            viewModel.startPrice.setValue(values.get(0).doubleValue());
            viewModel.endPrice.setValue(values.get(1).doubleValue());
            String startValue = DisplayUtils.convertDoubleTwoDecimalsHasCurrency(values.get(0).doubleValue());
            String endValue = DisplayUtils.convertDoubleTwoDecimalsHasCurrency(values.get(1).doubleValue());

            viewBinding.start.setText(startValue);
            viewBinding.end.setText(endValue);
        });

        setUpObserversCategory();
    }
    public void onSearchClick() {
        viewModel.isSearch.set(false);
        SEARCH_KEY.postValue(viewBinding.search.getText().toString());
        getSearchProduct(null, viewBinding.search.getText().toString(), "asc");
        hideKeyboard();
    }

    public void onFilterClick() {
        closeRightMenu();
        viewModel.isSearch.set(false);
        SEARCH_KEY.postValue(viewBinding.search.getText().toString());
        getFilterProduct(CATE_ID.getValue(), viewBinding.search.getText().toString(), viewModel.startPrice.getValue(), viewModel.endPrice.getValue(), "asc");
        hideKeyboard();
    }
    public void getSearchProduct(String categoryId, String name, String sort) {
        viewModel.isLoading.set(true);
        viewModel.searchProduct(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doFail() {

            }

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                PRODUCT_LIST = new MutableLiveData<>();
                PRODUCT_LIST.postValue(object.getContent());
                SORT.postValue(sort);
                setUpObserversProduct();
            }
        }, categoryId, Constants.SIZE_ITEM, name, sort);
    }
    public void getSortProduct(String categoryId, String name, Double fromPrice, Double toPrice, String sort) {
        viewModel.isLoading.set(true);
        viewModel.filterProduct(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doFail() {

            }

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                PRODUCT_LIST = new MutableLiveData<>();
                PRODUCT_LIST.postValue(object.getContent());
                SORT.postValue(sort);
                setUpObserversProduct();
            }
        }, categoryId, Constants.SIZE_ITEM, name, fromPrice, toPrice, sort);
    }
    public void getFilterProduct(String categoryId, String name, Double fromPrice, Double toPrice, String sort) {
        viewModel.isLoading.set(true);
        viewModel.filterProduct(new MainCalback<ResponseListObj<ProductResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doFail() {

            }

            @Override
            public void doSuccess(ResponseListObj<ProductResponse> object) {
                PRODUCT_LIST = new MutableLiveData<>();
                PRODUCT_LIST.postValue(object.getContent());
                SORT.postValue(sort);
                setUpObserversProduct();
            }
        }, categoryId, Constants.SIZE_ITEM, name, fromPrice, toPrice, sort);
    }
    public void getProductDetail(String id) {
        viewModel.getProductDetail(new MainCalback<ProductResponse>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(ProductResponse object) {
                ProductDetailActivity.PRODUCT_DETAIL = object;
                Intent intent = new Intent(FindProductActivity.this, ProductDetailActivity.class);
                startActivity(intent);
            }
            @Override
            public void doFail() {

            }
        }, id);
    }
    public void showRightMenu(){
        hideKeyboard();
        viewBinding.lFindProduct.openDrawer(viewBinding.menuFilter);
    }
    public void closeRightMenu(){
        viewBinding.lFindProduct.closeDrawer(viewBinding.menuFilter, true);
    }

    public void onBackClick() {
        if (viewModel.isSearch.get()) {
            viewModel.isSearch.set(false);
            viewBinding.search.setText("");
            hideKeyboard();
        } else {
            hideKeyboard();
            finish();
        }
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_find_product;
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
        CATEGORY_LIST = new MutableLiveData<>();
        PRODUCT_LIST = new MutableLiveData<>();
        SEARCH_KEY = new MutableLiveData<>();
        CATE_ID = new MutableLiveData<>();
        SORT = new MutableLiveData<>("asc");
    }

    @Override
    public void onItemCateSearchClick(CategoryResponse category) {
        viewModel.isSearch.set(false);
        CATE_ID.postValue(category.getId());
        viewBinding.search.setText("");
        getSearchProduct(category.getId(), "", "asc");
        hideKeyboard();
    }

    @Override
    public void onItemClick(ProductResponse product) {
        hideKeyboard();
        getProductDetail(product.getId());
    }

    @Override
    public void onItemClick(CategoryResponse category) {
        hideKeyboard();
        CATE_ID.postValue(category.getId());
        for (CategoryResponse categoryResponse : Objects.requireNonNull(CATEGORY_LIST.getValue())) {
            categoryResponse.setIsSelected(categoryResponse.getId().equals(category.getId()));
        }
    }
}
