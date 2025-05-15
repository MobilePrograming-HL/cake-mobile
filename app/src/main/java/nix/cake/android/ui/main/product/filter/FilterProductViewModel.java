package nix.cake.android.ui.main.product.filter;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.activity.BaseViewModel;

public class FilterProductViewModel extends BaseViewModel {
    public FilterProductViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
