package nix.cake.android.ui.main.home;

import androidx.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class HomeViewModel extends BaseFragmentViewModel {
    List<ProductResponse> productSaleList;
    List<ProductResponse> productNewList;
    List<ProductResponse> productPopularList;

    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public HomeViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        productSaleList = new ArrayList<>();
        productNewList = new ArrayList<>();
        productPopularList = new ArrayList<>();
    }
}
