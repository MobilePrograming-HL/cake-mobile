package nix.cake.android.ui.main.product.find;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.activity.BaseViewModel;

public class FindProductViewModel extends BaseViewModel {
    public FindProductViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
