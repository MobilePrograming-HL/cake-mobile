package nix.cake.android.ui.main.cart;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class CartViewModel extends BaseFragmentViewModel {
    public CartViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
