package nix.cake.android.ui.main.login;

import android.content.Intent;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.request.login.LoginRequest;
import nix.cake.android.data.model.api.request.login.SignUpRequest;
import nix.cake.android.ui.base.activity.BaseViewModel;
import nix.cake.android.utils.NetworkUtils;
import retrofit2.HttpException;
import timber.log.Timber;

public class SignUpViewModel extends BaseViewModel {
    public SignUpViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
    }

    public void doRegister(SignUpRequest request) {
        showLoading();
        compositeDisposable.add(repository.getApiService().register(request)
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
                            VerifyOtpActivity.EMAIL = request.getEmail();
                            Intent it = new Intent(application.getCurrentActivity(), VerifyOtpActivity.class);
                            application.getCurrentActivity().startActivity(it);
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
