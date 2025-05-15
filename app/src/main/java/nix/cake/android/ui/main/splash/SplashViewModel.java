package nix.cake.android.ui.main.splash;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.login.LoginRequest;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.utils.NetworkUtils;
import retrofit2.HttpException;
import timber.log.Timber;

public class SplashViewModel extends BaseViewModel {
    public SplashViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }
    public void getProductForHome(MainCalback<ResponseListObj<ProductResponse>> callback, Integer size, String createdSort, String soldSort) {
        compositeDisposable.add(repository.getApiService().getProductForHome(size, createdSort, soldSort)
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
    public void getListCategories(MainCalback<ResponseListObj<CategoryResponse>> callback){
        compositeDisposable.add(repository.getApiService().getListCategory()
                .subscribeOn(Schedulers.io())
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
    public void doRefreshToken(String Token) {
        compositeDisposable.add(repository.getApiService().refreshToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(throwable ->
                        throwable.flatMap((Function<Throwable, ObservableSource<?>>) throwable1 -> {
                            if (NetworkUtils.checkNetworkError(throwable1)) {
                                hideLoading();
                                return application.showDialogNoInternetAccess();
                            } else {
                                return Observable.error(throwable1);
                            }
                        })
                )
                .subscribe(
                        response -> {
                            repository.getSharedPreferences().setToken(response.getData().getToken());
                        }, throwable -> {
                            hideLoading();
                        }));
    }
}
