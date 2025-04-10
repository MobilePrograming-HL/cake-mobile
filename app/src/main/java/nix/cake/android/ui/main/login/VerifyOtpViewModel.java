package nix.cake.android.ui.main.login;

import android.content.Intent;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.request.login.ActiveAccountRequest;
import nix.cake.android.data.model.api.request.login.SignUpRequest;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.utils.NetworkUtils;
import retrofit2.HttpException;
import timber.log.Timber;

public class VerifyOtpViewModel extends BaseViewModel {
    String email;
    public VerifyOtpViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doVerifyOtp(ActiveAccountRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().activeAccount(request)
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
                            application.getCurrentActivity().finish();
                        }, throwable -> {
                            hideLoading();
                            Timber.e(throwable);
                            if (throwable instanceof HttpException && ((HttpException) throwable).code() == 400) {
                                HttpException httpException = (HttpException) throwable;
                                if (httpException.code() == 400) {
                                }
                                showNormalMessage("Incorrect account or password");
                            } else {
                                showNormalMessage("Incorrect account or password");
                            }
                        }));
    }
}
