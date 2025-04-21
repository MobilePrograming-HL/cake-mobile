package nix.cake.android.ui.main.product.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.request.cart.CartRequest;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.product.TagResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import retrofit2.HttpException;
import timber.log.Timber;

public class ProductDetailViewModel extends BaseViewModel {
    ProductResponse productDetail;
    TagResponse tag;
    Integer quantity = 1;
    public ProductDetailViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void addCartItem(CartRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().addCartItem(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            hideLoading();
                            showSuccessMessage("Add Success");
                        }, throwable -> {
                            hideLoading();
                            showNormalMessage("Add Failed");
                            Timber.e(throwable);
                        }));
    }
}
