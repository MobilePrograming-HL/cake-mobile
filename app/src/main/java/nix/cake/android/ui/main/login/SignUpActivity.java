package nix.cake.android.ui.main.login;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.databinding.ActivitySignUpBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;

public class SignUpActivity extends BaseActivity<ActivitySignUpBinding, SignUpViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);

        viewBinding.email.setOnEditorActionListener((v, actionId, event) -> {
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

        viewBinding.name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_up;
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

    public void onLoginAccountClick() {
        viewModel.showLoading();
        finish();
    }
    public void onSignUpClick() {
        hideKeyboard();
        setNullMgs();
        String email = viewBinding.email.getText().toString().trim();
        String password = viewBinding.password.getText().toString().trim();
        String name = viewBinding.name.getText().toString().trim();


        // Check if the email is valid and password is not empty
        if (viewModel.isValidEmail(email) && viewModel.isValidEmail(password) && !viewModel.isEmpty(name)) {
            Toast.makeText(this, "Sign up success", Toast.LENGTH_LONG).show();
            setNullMgs();  // Clear any previous error messages
            return;
        }

        // Check if either field is empty
        if (viewModel.isEmpty(email) || viewModel.isEmpty(password) || viewModel.isEmpty(name)) {
            setNullMgs();
            viewBinding.errorMgs.setText(R.string.pleaseFillInformation);
            return;
        }

        // Check if the email is invalid
        if (!viewModel.isValidEmail(email)) {
            viewBinding.errorEmailMgs.setText(R.string.invalid_email);
        }

        // Check if the password is invalid
        if (!viewModel.isValidPassword(password)) {
            viewBinding.errorPasswordMgs.setText(R.string.invalid_password);
        }
    }


    public void setNullMgs() {
        viewBinding.errorEmailMgs.setText(R.string.empty);
        viewBinding.errorPasswordMgs.setText(R.string.empty);
        viewBinding.errorMgs.setText(R.string.empty);
    }
}
