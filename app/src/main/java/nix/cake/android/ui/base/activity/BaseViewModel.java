package nix.cake.android.ui.base.activity;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.other.ToastMessage;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import lombok.Getter;
import lombok.Setter;

public class BaseViewModel extends ViewModel {
    protected CompositeDisposable compositeDisposable;
    protected final ObservableBoolean mIsLoading = new ObservableBoolean();
    protected final MutableLiveData<String> progressBarMsg = new MutableLiveData<>();
    protected final MutableLiveData<ToastMessage> mErrorMessage = new MutableLiveData<>();

    @Setter
    protected String token;

    @Setter
    protected String deviceId;

    @Getter
    public final Repository repository;

    @Getter
    protected final MVVMApplication application;

    public BaseViewModel(Repository repository, MVVMApplication application){
        this.repository = repository;
        this.application = application;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void showLoading(){
        mIsLoading.set(true);
    }

    public void hideLoading(){
        mIsLoading.set(false);
    }

    public void showSuccessMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_SUCCESS,message));
    }

    public void showNormalMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_NORMAL,message));
    }

    public void showWarningMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_WARNING,message));
    }

    public void showErrorMessage(String message){
        mErrorMessage.setValue(new ToastMessage(ToastMessage.TYPE_ERROR,message));
    }

    public void changeProgressBarMsg(String message){
        progressBarMsg.setValue(message);
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean isEmpty(String data) {
        return data == null || data.isEmpty();
    }
}
