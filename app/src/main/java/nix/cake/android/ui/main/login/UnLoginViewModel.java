package nix.cake.android.ui.main.login;

import androidx.databinding.ObservableBoolean;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class UnLoginViewModel extends BaseFragmentViewModel  {
    public ObservableBoolean isLoading = new ObservableBoolean(true);

    public UnLoginViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
