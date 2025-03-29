package nix.cake.android.ui.main;

import android.content.Intent;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.ResponseWrapper;
import nix.cake.android.data.model.api.request.login.LoginRequest;
import nix.cake.android.data.model.api.response.login.LoginResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.ui.main.login.LoginActivity;
import nix.cake.android.utils.NetworkUtils;
import retrofit2.HttpException;
import timber.log.Timber;

public class MainViewModel extends BaseViewModel {

    public MainViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doLogin(LoginRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().login(request)
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
                            hideLoading();
                            repository.getSharedPreferences().setToken(response.getData().getToken());
                            application.getCurrentActivity().finish();
                            showSuccessMessage("Login success");
                        }, throwable -> {
                            hideLoading();
                            Timber.e(throwable);
                            if (throwable instanceof HttpException && ((HttpException) throwable).code() == 400) {
                                HttpException httpException = (HttpException) throwable;
                                if (httpException.code() == 400) {
                                }
                                showErrorMessage("Login failed");
                            } else {
                                showErrorMessage("Login failed");
                            }
                        }));
    }
    public void doRefreshToken() {
        showLoading();
        compositeDisposable.add(repository.getApiService().refreshToken(repository.getSharedPreferences().getToken())
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
                            hideLoading();
                            repository.getSharedPreferences().setToken(response.getData().getToken());
                            showSuccessMessage("Refresh token success");
                        }, throwable -> {
                            hideLoading();
                            Timber.e(throwable);
                            if (throwable instanceof HttpException && ((HttpException) throwable).code() == 400) {
                                HttpException httpException = (HttpException) throwable;
                                if (httpException.code() == 400) {
                                }
                                showErrorMessage("Refresh token failed");
                            } else {
                                showErrorMessage("Refresh token failed");
                            }
                        }));

    }
    public void logout(MainCalback<String> callback) {
        showLoading();
        compositeDisposable.add(repository.getApiService().logout(repository.getSharedPreferences().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            repository.getSharedPreferences().setToken("");
                            callback.doSuccess(response);
                        }, throwable -> {
                            hideLoading();
                            Timber.e(throwable);
                            if (throwable instanceof HttpException && ((HttpException) throwable).code() == 400) {
                                HttpException httpException = (HttpException) throwable;
                                if (httpException.code() == 400) {
                                }
                                showErrorMessage("Logout failed");
                            } else {
                                showErrorMessage("Logout failed");
                            }
                        }));
    }
}
