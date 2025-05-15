package nix.cake.android.ui.main.login;

import android.os.Bundle;

import androidx.annotation.Nullable;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.data.model.api.request.login.ActiveAccountRequest;
import nix.cake.android.databinding.ActivityVerifyOtpBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class VerifyOtpActivity extends BaseActivity<ActivityVerifyOtpBinding, VerifyOtpViewModel> {
    public static String EMAIL;
    private int resendAttempts = 0;
    private final int maxResendAttempts = 3;
    private final long resendIntervalMillis = 60_000;

    private CountDownTimer countDownTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        viewModel.email = EMAIL;
        viewBinding.email.setText(viewModel.email);
        setupOtpInputs();
        startResendCountdown();
        viewBinding.resend.setOnClickListener(v -> {
            if (resendAttempts < maxResendAttempts) {
                viewModel.resendOtp(EMAIL);
                startResendCountdown();
            }
        });

    }
    private void startResendCountdown() {
        viewBinding.resend.setEnabled(false);
        resendAttempts++;

        countDownTimer = new CountDownTimer(resendIntervalMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                viewBinding.resend.setText("Resend OTP (" + seconds + "s)");
            }

            @Override
            public void onFinish() {
                if (resendAttempts < maxResendAttempts) {
                    viewBinding.resend.setText("Resend OTP");
                    viewBinding.resend.setEnabled(true);
                } else {
                    viewBinding.resend.setText("Resend limit reached");
                    viewBinding.resend.setEnabled(false);
                }
            }
        }.start();
    }

    private void setupOtpInputs() {
        EditText[] otpFields = {
                viewBinding.otp1,
                viewBinding.otp2,
                viewBinding.otp3,
                viewBinding.otp4,
                viewBinding.otp5,
                viewBinding.otp6
        };

        for (int i = 0; i < otpFields.length; i++) {
            final int index = i;

            otpFields[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (otpFields[index].hasFocus()) {
                        if (otpFields[index].getBackground().getConstantState().equals(
                                getResources().getDrawable(R.drawable.background_text_box_otp_error).getConstantState())) {
                            otpFields[index].setBackgroundResource(R.drawable.background_text_box_otp_error_is_input);
                        } else {
                            otpFields[index].setBackgroundResource(R.drawable.background_text_box_otp_is_input);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        otpFields[index].setBackgroundResource(R.drawable.background_text_box_otp);
                        if (index < otpFields.length - 1) {
                            otpFields[index + 1].requestFocus();
                        }
                    }
                }
            });

            otpFields[index].setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    if (otpFields[index].getBackground().getConstantState().equals(
                            getResources().getDrawable(R.drawable.background_text_box_otp_error).getConstantState())) {
                        otpFields[index].setBackgroundResource(R.drawable.background_text_box_otp_error_is_input);
                    } else {
                        otpFields[index].setBackgroundResource(R.drawable.background_text_box_otp_is_input);
                    }
                } else if (otpFields[index].getText().toString().isEmpty()) {
                    otpFields[index].setBackgroundResource(R.drawable.background_text_box_otp);
                }
            });

            otpFields[index].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN && keyCode == android.view.KeyEvent.KEYCODE_DEL) {
                    if (otpFields[index].getText().toString().isEmpty() && index > 0) {
                        otpFields[index - 1].requestFocus();
                        otpFields[index - 1].setText("");
                    }
                }
                return false;
            });
        }
    }

    public void onVerifyClick() {
        EditText[] otpFields = {
                viewBinding.otp1,
                viewBinding.otp2,
                viewBinding.otp3,
                viewBinding.otp4,
                viewBinding.otp5,
                viewBinding.otp6
        };

        boolean hasError = false;

        for (EditText field : otpFields) {
            if (field.getText().toString().isEmpty()) {
                field.setBackgroundResource(R.drawable.background_text_box_otp_error);
                hasError = true;
            } else {
                field.setBackgroundResource(R.drawable.background_text_box_otp);
            }
        }

        if (hasError) {
            viewBinding.errorMgs.setText(R.string.error_empty_otp);
            viewBinding.errorMgs.setVisibility(View.VISIBLE);
        } else {
            viewBinding.errorMgs.setVisibility(View.GONE);

            StringBuilder otpCode = new StringBuilder();
            for (EditText field : otpFields) {
                otpCode.append(field.getText().toString());
            }

            ActiveAccountRequest request = new ActiveAccountRequest();
            request.setEmail(viewModel.email);
            request.setOtpCode(otpCode.toString());

            viewModel.doVerifyOtp(request);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_otp;
    }

    @Override
    public int getBindingVariable() {
        return BR.vm;
    }

    @Override
    public void performDependencyInjection(ActivityComponent buildComponent) {
        buildComponent.inject(this);
    }
}
