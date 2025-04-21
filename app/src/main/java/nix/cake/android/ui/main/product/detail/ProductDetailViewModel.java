package nix.cake.android.ui.main.product.detail;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.activity.BaseViewModel;

public class ProductDetailViewModel extends BaseViewModel {
    public ProductDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
