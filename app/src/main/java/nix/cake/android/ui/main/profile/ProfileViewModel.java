package nix.cake.android.ui.main.profile;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class ProfileViewModel extends BaseFragmentViewModel {
    public ProfileViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
