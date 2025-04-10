package nix.cake.android.ui.main.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.request.login.SignUpRequest;
import nix.cake.android.databinding.ActivitySignUpBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;

public class SignUpActivity extends BaseActivity<ActivitySignUpBinding, SignUpViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setupInputFields();

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

        viewBinding.username.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(v);
                return true;
            }
            return false;
        });
    }
    private void setupInputFields() {
        EditText[] inputFields = {
                viewBinding.username,
                viewBinding.email,
                viewBinding.password
        };

        for (EditText field : inputFields) {
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (field.hasFocus()) {
                        if (field == viewBinding.email) {
                            String email = s.toString().trim();
                            if (viewModel.isValidEmail(email)) {
                                field.setBackgroundResource(R.drawable.background_text_box_radius_4);
                                viewBinding.errorEmailMgs.setText(R.string.empty);
                            } else {
                                field.setBackgroundResource(R.drawable.background_text_box_radius_4_error_is_input);
                            }
                        } else {
                            field.setBackgroundResource(R.drawable.background_text_box_radius_4_is_input);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            field.setOnFocusChangeListener((v, hasFocus) -> {
                String text = field.getText().toString().trim();
                if (hasFocus) {
                    if (field.getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.background_text_box_radius_4_error).getConstantState())) {
                        field.setBackgroundResource(R.drawable.background_text_box_radius_4_error_is_input);
                    } else {
                        field.setBackgroundResource(R.drawable.background_text_box_radius_4_is_input);
                    }
                } else {
                    if (text.isEmpty()) {
                        field.setBackgroundResource(R.drawable.background_text_box_radius_4_error);
                    } else {
                        if (field == viewBinding.email && !viewModel.isValidEmail(text)) {
                            field.setBackgroundResource(R.drawable.background_text_box_radius_4_error);
                            viewBinding.errorEmailMgs.setText(R.string.invalid_email);
                        } else {
                            field.setBackgroundResource(R.drawable.background_text_box_radius_4);
                        }
                    }
                }
            });
        }
    }
    public void onSignUpClick() {
        hideKeyboard();
        setNullMgs();

        String email = viewBinding.email.getText().toString().trim();
        String password = viewBinding.password.getText().toString().trim();
        String name = viewBinding.username.getText().toString().trim();

        boolean hasError = false;

        if (viewModel.isEmpty(email)) {
            viewBinding.email.setBackgroundResource(R.drawable.background_text_box_radius_4_error);
            hasError = true;
        } else if (!viewModel.isValidEmail(email)) {
            viewBinding.email.setBackgroundResource(R.drawable.background_text_box_radius_4_error);
            viewBinding.errorEmailMgs.setText(R.string.invalid_email);
            hasError = true;
        }

        if (viewModel.isEmpty(password)) {
            viewBinding.password.setBackgroundResource(R.drawable.background_text_box_radius_4_error);
            hasError = true;
        }

        if (viewModel.isEmpty(name)) {
            viewBinding.username.setBackgroundResource(R.drawable.background_text_box_radius_4_error);
            hasError = true;
        }

        if (hasError) {
            viewBinding.errorMgs.setText(R.string.pleaseFillInformation);
            return;
        }

        SignUpRequest request = new SignUpRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setUsername(name);
        request.setConfirmPassword(password);
        request.setTermsAccepted(true);

        viewModel.doRegister(request);
        setNullMgs();
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
    public void setNullMgs() {
        viewBinding.errorEmailMgs.setText(R.string.empty);
        viewBinding.errorPasswordMgs.setText(R.string.empty);
        viewBinding.errorMgs.setText(R.string.empty);
    }
}
