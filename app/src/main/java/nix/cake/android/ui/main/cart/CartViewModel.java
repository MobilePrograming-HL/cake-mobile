package nix.cake.android.ui.main.cart;

import androidx.lifecycle.MutableLiveData;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;
import nix.cake.android.utils.DisplayUtils;

public class CartViewModel extends BaseFragmentViewModel {
    public MutableLiveData<String> total = new MutableLiveData<>(DisplayUtils.convertDoubleTwoDecimalsHasCurrency(0.0));
    public CartViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
