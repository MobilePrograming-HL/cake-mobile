package nix.cake.android.ui.main.profile.order;

import androidx.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.order.CreateOrderRequest;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.order.OrderResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import timber.log.Timber;

public class MyOrdersViewModel extends BaseViewModel {
    public ObservableBoolean isEmpty = new ObservableBoolean(false);
    public ObservableBoolean isLoading = new ObservableBoolean(false);
    public MyOrdersViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void getListProducts(MainCalback<ResponseListObj<ProductResponse>> callback, String categoryId, Integer page) {
        compositeDisposable.add(repository.getApiService().getListProduct(categoryId, 10, page)
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
    public void getListOrder(MainCalback<ResponseListObj<OrderResponse>> callback, Integer status, Integer size) {
        compositeDisposable.add(repository.getApiService().getListOrder(size, status)
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
    public void getOrder(MainCalback<OrderResponse> callback, String id) {
        compositeDisposable.add(repository.getApiService().getOrder(id)
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
