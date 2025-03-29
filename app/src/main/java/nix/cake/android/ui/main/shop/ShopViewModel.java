package nix.cake.android.ui.main.shop;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class ShopViewModel extends BaseFragmentViewModel {
    public ShopViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
