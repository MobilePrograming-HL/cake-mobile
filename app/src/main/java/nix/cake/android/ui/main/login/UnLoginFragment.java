package nix.cake.android.ui.main.login;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;

import java.util.List;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.databinding.FragmentUnLoginBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.MainActivity;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;

public class UnLoginFragment extends BaseFragment<FragmentUnLoginBinding, UnLoginViewModel> implements ProductItemAdapter.OnItemClickListener {

    public static MutableLiveData<List<ProductResponse>> PRODUCT_LIST = new MutableLiveData<>();
    private ProductItemAdapter productAdapter;

    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setUpProductAdapter();
        setUpObserversProduct();
        Glide.with(this)
                .asGif()
                .load(R.drawable.rabbit)
                .into(binding.gif);
    }
    public void setUpObserversProduct() {
        PRODUCT_LIST.observe(this, productList -> {
            if (productAdapter == null && productList == null) return;
            viewModel.isLoading.set(false);
            productAdapter.setData(productList);
        });
    }
    public void setUpProductAdapter() {
        productAdapter = new ProductItemAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columns
        binding.rvProduct.setLayoutManager(gridLayoutManager);
        binding.rvProduct.setAdapter(productAdapter);
    }
    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_un_login;
    }
    public void onLoginClick() {
        ((MainActivity) requireActivity()).login();
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
