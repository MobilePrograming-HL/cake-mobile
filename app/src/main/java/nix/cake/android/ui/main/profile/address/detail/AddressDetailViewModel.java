package nix.cake.android.ui.main.profile.address.detail;

import android.app.Application;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.cart.CartRequest;
import nix.cake.android.data.model.api.request.profile.AddressRequest;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import timber.log.Timber;

public class AddressDetailViewModel extends BaseViewModel {
    public AddressDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void getListProvince(MainCalback<ResponseListObj<NationResponse>> callback, Integer kind, Integer page) {
        compositeDisposable.add(repository.getApiService().getListProvince(kind, page)
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
    public void getListDistrictOrCommune(MainCalback<ResponseListObj<NationResponse>> callback, Integer kind, String parentId, Integer page) {
        compositeDisposable.add(repository.getApiService().getListDistrictOrCommune(kind, parentId, page)
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

    public void addNewAddress(AddressRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().addNewAddress(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            hideLoading();
                            showSuccessMessage("Add Success");
                            application.getCurrentActivity().finish();
                        }, throwable -> {
                            hideLoading();
                            showNormalMessage("Add Failed");
                            Timber.e(throwable);
                        }));
    }

    public void updateAddress(AddressRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().updateAddress(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            hideLoading();
                            showSuccessMessage("Update Success");
                            application.getCurrentActivity().finish();
                        }, throwable -> {
                            hideLoading();
                            showNormalMessage("Update Failed");
                            Timber.e(throwable);
                        }));
    }
}
