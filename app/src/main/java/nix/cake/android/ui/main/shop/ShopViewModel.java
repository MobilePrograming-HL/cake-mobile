package nix.cake.android.ui.main.shop;

import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;
import nix.cake.android.ui.main.MainCalback;
import timber.log.Timber;

public class ShopViewModel extends BaseFragmentViewModel {
    List<CategoryResponse> categoriesList;
    List<ProductResponse> productList;
    public ShopViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
