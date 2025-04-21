package nix.cake.android.ui.main.shop;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class ShopViewModel extends BaseFragmentViewModel {
    MutableLiveData<List<CategoryResponse>> categoriesList = new MutableLiveData<>();
    MutableLiveData<List<ProductResponse>> productList = new MutableLiveData<>();

    public ShopViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}

