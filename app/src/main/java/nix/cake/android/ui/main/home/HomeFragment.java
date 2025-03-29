package nix.cake.android.ui.main.home;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.databinding.FragmentHomeBinding;
import nix.cake.android.di.component.FragmentComponent;
import nix.cake.android.ui.base.fragment.BaseFragment;
import nix.cake.android.ui.main.product.adapter.ProductItemAdapter;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> implements ProductItemAdapter.OnItemClickListener {

    public static List<ProductResponse> PRODUCT_SALE;
    public static List<ProductResponse> PRODUCT_NEW;
    public static List<ProductResponse> PRODUCT_POPULAR;

    private ProductItemAdapter saleAdapter;
    private ProductItemAdapter newAdapter;
    private ProductItemAdapter popularAdapter;
    @Override
    protected void performDataBinding() {
        binding.setF(this);
        binding.setVm(viewModel);
        setUpAdapter();
    }
    public void setUpAdapter() {
        saleAdapter = new ProductItemAdapter(this);
        newAdapter = new ProductItemAdapter(this);
        popularAdapter = new ProductItemAdapter(this);

        binding.rvSale.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvSale.setAdapter(saleAdapter);
        saleAdapter.setData(viewModel.productSaleList);

        binding.rvNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvNew.setAdapter(newAdapter);
        newAdapter.setData(viewModel.productNewList);

        binding.rvPopular.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvPopular.setAdapter(popularAdapter);
        popularAdapter.setData(viewModel.productPopularList);
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

    @Override
    public void onItemClick(OrderResponse order) {

    }
}
