package nix.cake.android.ui.main.product.find;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import timber.log.Timber;

public class FindProductViewModel extends BaseViewModel {
    MutableLiveData<Double> minPrice = new MutableLiveData<>();
    MutableLiveData<Double> maxPrice = new MutableLiveData<>();
    MutableLiveData<Double> startPrice = new MutableLiveData<>();
    MutableLiveData<Double> endPrice = new MutableLiveData<>();
    MutableLiveData<String> categoryId = new MutableLiveData<>();

    public ObservableBoolean isEmpty = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public ObservableBoolean isSearch = new ObservableBoolean(false);

    public FindProductViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void searchProduct(MainCalback<ResponseListObj<ProductResponse>> callback, String categoryId, Integer size, String name, String sort) {
        compositeDisposable.add(repository.getApiService().searchProduct(categoryId, size, name, sort)
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

    public void getProductDetail(MainCalback<ProductResponse> callback, String id) {
        showLoading();
        compositeDisposable.add(repository.getApiService().getProductDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isResult()) {
                                callback.doSuccess(response.getData());
                            } else {
                                hideLoading();
                                callback.doFail();
                            }
                        }, throwable -> {
                            Timber.e(throwable);
                            callback.doError(throwable);
                        }
                )
        );
    }

    public void filterProduct(MainCalback<ResponseListObj<ProductResponse>> callback, String categoryId, Integer size, String name, Double fromPrice, Double toPrice, String sort) {
        compositeDisposable.add(repository.getApiService().filterProduct(categoryId, size, name, fromPrice, toPrice, sort)
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

    public void getListProduct(MainCalback<ResponseListObj<ProductResponse>> callback, String categoryId, Integer size, Integer page) {
        compositeDisposable.add(repository.getApiService().getListProduct(categoryId, size, page)
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
