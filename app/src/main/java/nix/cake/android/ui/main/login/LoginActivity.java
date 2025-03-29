package nix.cake.android.ui.main.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;

import nix.cake.android.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.request.login.LoginRequest;
import nix.cake.android.databinding.ActivityLoginBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);

        viewBinding.username.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v);
                return true;
            }
            return false;
        });

        viewBinding.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v);
                return true;
            }
            return false;
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }
    public void togglePasswordVisibility(View view) {
        if (viewBinding.password.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            viewBinding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            viewBinding.password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_close, 0);
        } else {
            viewBinding.password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            viewBinding.password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0);
        }
        viewBinding.password.setSelection(viewBinding.password.getText().length());
    }

    public void onSignUpAccountClick() {
        Intent it = new Intent(this, SignUpActivity.class);
        viewModel.showLoading();
        startActivity(it);
    }

    public void onLoginClick() {
        hideKeyboard();
        String username = viewBinding.username.getText().toString().trim();
        String password = viewBinding.password.getText().toString().trim();

        if (!viewModel.isEmpty(username) && !viewModel.isEmpty(password)) {
            Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show();
            setNullMgs();
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(username);
            loginRequest.setPassword(password);
            viewModel.doLogin(loginRequest);
            return;
        }

        if (viewModel.isEmpty(password) || viewModel.isEmpty(username)) {
            setNullMgs();
            viewBinding.errorMgs.setText(R.string.pleaseFillInformation);
            return;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        viewModel.hideLoading();
    }

    public void setNullMgs() {
        viewBinding.errorEmailMgs.setText(R.string.empty);
        viewBinding.errorPasswordMgs.setText(R.string.empty);
        viewBinding.errorMgs.setText(R.string.empty);
    }
}
