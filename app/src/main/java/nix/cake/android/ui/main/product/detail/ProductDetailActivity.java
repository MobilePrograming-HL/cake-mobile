package nix.cake.android.ui.main.product.detail;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.ActivityProductDetailBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.product.detail.adapter.ImageSliderAdapter;

public class ProductDetailActivity extends BaseActivity<ActivityProductDetailBinding, ProductDetailViewModel> {

    private ImageSliderAdapter imageSliderAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        viewBinding.setRating("4.3");
        viewBinding.setReviews("1k2");

        List<String> imageList = new ArrayList<>();
        imageList.add("https://example.com/image1.jpg");
        imageList.add("https://example.com/image2.jpg");
        imageList.add("https://example.com/image3.jpg");

        imageSliderAdapter = new ImageSliderAdapter(this, imageList);
        viewBinding.img.setAdapter(imageSliderAdapter);

        viewBinding.img.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                String imageCount = (position + 1) + "/" + imageList.size();
                viewBinding.tvImageCount.setText(imageCount);
            }
        });

        viewBinding.tvImageCount.setText("1/" + imageList.size());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }
}