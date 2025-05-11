package nix.cake.android.ui.main.product.detail;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.cart.CartRequest;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.product.TagResponse;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import timber.log.Timber;

public class ProductDetailViewModel extends BaseViewModel {
    ProductResponse productDetail;
    TagResponse tag;
    Integer quantity = 1;
    public ProductDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void addCartItem(CartRequest request) {
        compositeDisposable.add(repository.getApiService().addCartItem(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            showNormalMessage("Add Success");
                        }, throwable -> {
                            showNormalMessage("Add Failed");
                            Timber.e(throwable);
                        }));
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
}
