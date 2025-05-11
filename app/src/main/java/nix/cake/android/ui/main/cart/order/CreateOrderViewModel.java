package nix.cake.android.ui.main.cart.order;

import android.content.Intent;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.order.BuyNowOrderRequest;
import nix.cake.android.data.model.api.request.order.CreateOrderRequest;
import nix.cake.android.data.model.api.request.profile.AddressRequest;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.utils.DisplayUtils;
import timber.log.Timber;

public class CreateOrderViewModel extends BaseViewModel {
    public ObservableBoolean isEmpty = new ObservableBoolean(false);

    public MutableLiveData<Double> total = new MutableLiveData<>(0.0);
    public MutableLiveData<Double> standard_fee = new MutableLiveData<>(25000.0);
    public MutableLiveData<Double> fast_fee = new MutableLiveData<>(35000.0);
    public MutableLiveData<String> standard_day = new MutableLiveData<>();
    public MutableLiveData<String> fast_day = new MutableLiveData<>();
    public MutableLiveData<Double> shipping_fee = new MutableLiveData<>(25000.0);
    CreateOrderRequest createOrderRequest = new CreateOrderRequest();
    BuyNowOrderRequest buyNowOrderRequest = new BuyNowOrderRequest();
    public CreateOrderViewModel(Repository repository, MVVMApplication application) {
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
    public void createOrder(CreateOrderRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().createOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            hideLoading();
                            Intent it = new Intent(application.getApplicationContext(), OrderSuccessActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // cần nếu gọi từ Application
                            application.getApplicationContext().startActivity(it);

                            application.getCurrentActivity().finish();
                        }, throwable -> {
                            hideLoading();
                            Timber.e(throwable);
                        }));
    }

    public void buyNowOrder(BuyNowOrderRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().buyNowOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            hideLoading();
                            Intent it = new Intent(application.getApplicationContext(), OrderSuccessActivity.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // cần nếu gọi từ Application
                            application.getApplicationContext().startActivity(it);

                            application.getCurrentActivity().finish();

                        }, throwable -> {
                            hideLoading();
                            Timber.e(throwable);
                        }));
    }
}
