package nix.cake.android.ui.base.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Objects;

import nix.cake.android.MVVMApplication;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.di.component.DaggerActivityComponent;
import nix.cake.android.di.module.ActivityModule;
import nix.cake.android.utils.DialogUtils;

import javax.inject.Inject;
import javax.inject.Named;

public abstract class BaseActivity<B extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity{

    protected B viewBinding;

    @Inject
    protected V viewModel;

    @Inject
    protected Context application;

    @Named("access_token")
    @Inject
    protected String token;

    @Named("device_id")
    @Inject
    protected String deviceId;

    private Dialog progressDialog;
    // Listen all action from local
    private BroadcastReceiver globalApplicationReceiver;
    private IntentFilter filterGlobalApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection(getBuildComponent());
        super.onCreate(savedInstanceState);
        performDataBinding();
        updateCurrentAcitivity();
        setupHideKeyboardOnTouch();
        viewModel.setToken(token);
        viewModel.setDeviceId(deviceId);
        viewModel.mIsLoading.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback(){

            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(((ObservableBoolean)sender).get()){
                    showProgressbar(getResources().getString(R.string.msg_loading));
                }else{
                    hideProgress();
                }
            }
        });
        viewModel.mErrorMessage.observe(this, toastMessage -> {
            if(toastMessage!=null){
                toastMessage.showMessage(getApplicationContext());
            }
        });
        viewModel.progressBarMsg.observe(this, progressBarMsg ->{
            if (progressBarMsg != null){
                changeProgressBarMsg(progressBarMsg);
            }
        });
        filterGlobalApplication = new IntentFilter();
        filterGlobalApplication.addAction(Constants.ACTION_EXPIRED_TOKEN);
        globalApplicationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action==null){
                    return;
                }
                if (action.equals(Constants.ACTION_EXPIRED_TOKEN)){
                    doExpireSession();
                }
            }
        };
        setupEditorActionListenerForAllEditTexts(viewBinding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.hideLoading();
        LocalBroadcastManager.getInstance(this).registerReceiver(globalApplicationReceiver, filterGlobalApplication);
        updateCurrentAcitivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(globalApplicationReceiver);
    }

    public abstract @LayoutRes int getLayoutId();

    public abstract int getBindingVariable();

    public void doExpireSession() {
        //implement later

    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }
    private void setupEditorActionListenerForAllEditTexts(View view) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_GO ||
                        actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_SEND ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN)) {

                    hideKeyboard();
                    return true;
                }
                return false;
            });
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setupEditorActionListenerForAllEditTexts(child);
            }
        }
    }
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        clearFocusFromAllEditTexts(viewBinding.getRoot());
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setupHideKeyboardOnTouch() {
        // Find root layout of the activity
        View rootView = findViewById(android.R.id.content);

        // Set onTouchListener to close keyboard when touch outside
        rootView.setOnTouchListener((v, event) -> {
            // If touch outside EditText, hide the keyboard
            if (getCurrentFocus() != null) {
                View view = getCurrentFocus();
                if (view instanceof EditText) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
            return false; // Allow the touch event to be handled by the current view
        });
    }

    public void clearFocusFromAllEditTexts(View view) {
        if (view instanceof EditText) {
            view.clearFocus();
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                clearFocusFromAllEditTexts(child);
            }
        }
    }

    private void performDataBinding() {
        viewBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewBinding.setVariable(getBindingVariable(), viewModel);
        viewBinding.executePendingBindings();
    }

    public void showProgressbar(String msg){
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = DialogUtils.createDialogLoading(this, msg);
        progressDialog.show();
    }

    public void changeProgressBarMsg(String msg){
        if (progressDialog != null){
            ((TextView)progressDialog.findViewById(R.id.progressbar_msg)).setText(msg);
        }
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    private ActivityComponent getBuildComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(((MVVMApplication)getApplication()).getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

    public abstract void performDependencyInjection(ActivityComponent buildComponent);

    private void updateCurrentAcitivity(){
        MVVMApplication mvvmApplication = (MVVMApplication)application;
        mvvmApplication.setCurrentActivity(this);
    }

    public boolean showHeader(){
        return false;
    }

    ObservableField<String> leftTitle;
    ObservableField<String> centerTitle;
    public void setCenterTitle(String msg){
        if (centerTitle == null){
            centerTitle = new ObservableField<>(msg);
        } else {
            centerTitle.set(msg);
        }
    }
    public void setLeftTitle(String msg){
        if (leftTitle == null){
            leftTitle = new ObservableField<>(msg);
        } else {
            leftTitle.set(msg);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        clearFocusFromAllEditTexts(viewBinding.getRoot());

    }
    public boolean isLogin() {
        return viewModel.getRepository().getToken() != null && !Objects.equals(viewModel.getRepository().getToken(), "") && !Objects.equals(viewModel.getRepository().getToken(), "NULL");
    }
}
