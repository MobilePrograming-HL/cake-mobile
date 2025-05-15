package nix.cake.android.ui.main.profile.address.detail.fragment;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.ui.base.fragment.BaseFragmentViewModel;

public class ProvinceViewModel extends BaseFragmentViewModel {
    public MutableLiveData<List<NationResponse>> provinceList = new MutableLiveData<>(new ArrayList<>());

    public ProvinceViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
}
