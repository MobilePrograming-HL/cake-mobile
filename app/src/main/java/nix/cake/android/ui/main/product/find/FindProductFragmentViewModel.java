package nix.cake.android.ui.main.product.find;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class FindProductFragmentViewModel extends BaseFragmentViewModel {
    public FindProductFragmentViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
