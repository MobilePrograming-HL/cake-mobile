package nix.cake.android.ui.main.profile.address;

import androidx.databinding.ObservableBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import timber.log.Timber;

public class ShippingAddressViewModel extends BaseViewModel {

    public ObservableBoolean isEmpty = new ObservableBoolean(false);
    public ShippingAddressViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void getListAddresses(MainCalback<ResponseListObj<AddressResponse>> callback, Integer size) {
        compositeDisposable.add(repository.getApiService().getListAddress(size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isResult()) {
                                callback.doSuccess(response.getData());
                            } else {
                                callback.doFail();
                            }
                        }, throwable -> {
                            Timber.e(throwable);
                            callback.doError(throwable);
                        }
                )
        );
    }
    public void setAddressDefault(String id) {
        compositeDisposable.add(repository.getApiService().setDefaultAddress(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                        }, Timber::e));
    }

    public void deleteAddress(String id) {
        compositeDisposable.add(repository.getApiService().deleteAddress(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                        }, Timber::e));
    }

    public void getAddressDetail(MainCalback<AddressResponse> callback, String id) {
        compositeDisposable.add(repository.getApiService().getAddressDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isResult()) {
                                callback.doSuccess(response.getData());
                            } else {
                                callback.doFail();
                            }
                        }, throwable -> {
                            Timber.e(throwable);
                            callback.doError(throwable);
                        }
                )
        );
    }
}
