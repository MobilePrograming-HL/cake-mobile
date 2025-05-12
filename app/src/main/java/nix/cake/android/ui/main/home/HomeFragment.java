package nix.cake.android.ui.main.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.FragmentHomeBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.home.adapter.ItemCategoryHomeAdapter;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;
import nix.cake.android.ui.main.product.detail.ProductDetailActivity;
import nix.cake.android.ui.main.product.detail.adapter.ImageSliderAdapter;
import nix.cake.android.ui.main.product.find.FindProductActivity;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements ProductItemAdapter.OnItemClickListener, ItemCategoryHomeAdapter.OnItemClickListener {

    public static MutableLiveData<List<ProductResponse>> PRODUCT_LIST = new MutableLiveData<>();
    public static MutableLiveData<List<CategoryResponse>> CATEGORIES_LIST = new MutableLiveData<>();
    ImageSliderAdapter imageSliderAdapter;
    private ProductItemAdapter productAdapter;
    private ItemCategoryHomeAdapter categoryAdapter;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setUpAdapter();
        setUpImageSlider();
        setUpCategoryAdapter();
        setUpObserversCategory();
        setUpObserversProduct();
        onSelectClick();
    }

    public void setUpImageSlider() {
        List<String> imageList = new ArrayList<>();
        imageList.add("https://res.cloudinary.com/dcxgx3ott/image/upload/v1744210471/wedding-3_dlteib.webp");
        imageList.add("https://res.cloudinary.com/dcxgx3ott/image/upload/v1744209366/Best-Birthday-Cake-with-milk-chocolate-buttercream-SQUARE_x5yuqo.webp");
        imageList.add("https://res.cloudinary.com/dcxgx3ott/image/upload/v1744211244/sponge-2_fptwjd.webp");
        imageList.add("https://res.cloudinary.com/dcxgx3ott/image/upload/v1744208115/product-10_q4cevx.webp");
        imageSliderAdapter = new ImageSliderAdapter(getContext(), imageList);
        binding.img.setAdapter(imageSliderAdapter);

        startAutoSlide();
    }
    private void startAutoSlide() {
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (imageSliderAdapter.getItemCount() == 0) return;

                int currentPosition = binding.img.getCurrentItem();
                currentPosition = (currentPosition + 1) % imageSliderAdapter.getItemCount();
                binding.img.setCurrentItem(currentPosition, true);

                sliderHandler.postDelayed(this, 3000);
            }
        };

        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
    public void setUpObserversCategory() {
        CATEGORIES_LIST.observe(this, categories -> {
            if (categoryAdapter != null) {
                categoryAdapter.setData(categories);
            }
        });
        PRODUCT_LIST.observe(this, products -> {
            if (productAdapter == null || products.isEmpty()) return;
            viewModel.isLoading.set(false);
            productAdapter.setData(products);
        });
    }

    public void setUpObserversProduct() {


    }
    public void setUpCategoryAdapter() {
        categoryAdapter = new ItemCategoryHomeAdapter(this);
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategory.setAdapter(categoryAdapter);
    }

    public void setUpAdapter() {
        productAdapter = new ProductItemAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        binding.rvProduct.setLayoutManager(gridLayoutManager);
        binding.rvProduct.setAdapter(productAdapter);
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }
    @Override
    protected void performDependencyInjection(FragmentComponent buildComponent) {
        buildComponent.inject(this);
    }
    public void onSelectClick(){
        binding.newIn.setOnClickListener(v -> {
            makeNoSelect();
            makeSelect(binding.newIn.getId());
        });
        binding.deals.setOnClickListener(v -> {
            makeNoSelect();
            makeSelect(binding.deals.getId());
        });
        binding.bestSellers.setOnClickListener(v -> {
            makeNoSelect();
            makeSelect(binding.bestSellers.getId());
        });

    }

    public void makeNoSelect() {
        viewModel.isLoading.set(true);
        productAdapter.setData(new ArrayList<>());

        binding.newIn.setTypeface(Typeface.DEFAULT);
        binding.deals.setTypeface(Typeface.DEFAULT);
        binding.bestSellers.setTypeface(Typeface.DEFAULT);

        binding.newIn.setTextColor(getResources().getColor(R.color.text));
        binding.deals.setTextColor(getResources().getColor(R.color.text));
        binding.bestSellers.setTextColor(getResources().getColor(R.color.text));

        binding.newIn.setBackgroundResource(R.drawable.background_tag);
        binding.deals.setBackgroundResource(R.drawable.background_tag);
        binding.bestSellers.setBackgroundResource(R.drawable.background_tag);
    }

    @SuppressLint({"NonConstantResourceId"})
    public void makeSelect(int id) {
        PRODUCT_LIST.setValue(new ArrayList<>());
        switch (id) {
            case R.id.new_in:
                ((MainActivity) requireActivity()).getListProductForHome(Constants.ASC, "");
                binding.newIn.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.newIn.setTextColor(getResources().getColor(R.color.white));
                binding.newIn.setBackgroundResource(R.drawable.background_tag_select);
                break;
            case R.id.deals:
                ((MainActivity) requireActivity()).getListProductForHome("", "");
                binding.deals.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.deals.setTextColor(getResources().getColor(R.color.white));
                binding.deals.setBackgroundResource(R.drawable.background_tag_select);
                break;
            case R.id.best_sellers:
                ((MainActivity) requireActivity()).getListProductForHome("", Constants.ASC);
                binding.bestSellers.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                binding.bestSellers.setTextColor(getResources().getColor(R.color.white));
                binding.bestSellers.setBackgroundResource(R.drawable.background_tag_select);
                break;
        }
    }
    @Override
    public void onItemClick(ProductResponse product) {
         ((MainActivity) requireActivity()).getProductDetail(product.getId());
    }

    @Override
    public void onItemCateSearchClick(CategoryResponse category) {
        Intent it = new Intent(getContext(), FindProductActivity.class);
        startActivity(it);
        ((MainActivity) requireActivity()).getListProductForSearch(category.getId(),"", "asc");
        ((MainActivity) requireActivity()).getListCategoriesForSearch();
    }
}
