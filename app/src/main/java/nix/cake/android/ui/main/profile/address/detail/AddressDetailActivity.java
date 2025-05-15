package nix.cake.android.ui.main.profile.address.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;
import java.util.Objects;

import eu.davidea.flexibleadapter.databinding.BR;
import nix.cake.android.R;
import nix.cake.android.constant.Constants;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.request.profile.AddressRequest;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import nix.cake.android.databinding.ActivityAddressDetailBinding;
import nix.cake.android.di.component.ActivityComponent;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainCalback;
import nix.cake.android.ui.main.profile.address.detail.fragment.CommuneFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.DistrictFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.ProvinceFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.adapter.AddressViewPagerAdapter;

public class AddressDetailActivity extends BaseActivity<ActivityAddressDetailBinding, AddressDetailViewModel> {
    private AddressViewPagerAdapter viewPagerAdapter;
    private final String[] titles = new String[]{Constants.PROVINCE, Constants.DISTRICT, Constants.COMMUNE};
    public static NationResponse PROVINCE = null;
    public static NationResponse DISTRICT = null;
    public static NationResponse COMMUNE = null;

    private static final int TYPE_PROVINCE = 0;
    private static final int TYPE_DISTRICT = 1;
    private static final int TYPE_COMMUNE = 2;
    public static Integer TYPE_HANDLE_ADDRESS;

    public static AddressResponse ADDRESS_DETAIL;
    private TextWatcher textWatcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.setA(this);
        viewBinding.setVm(viewModel);
        setupForm();
        setupDrawer();
        setUpAddress();
        setupViewPager();

        setupTextWatchers();
        setupFocusListeners();
        setupLocationFieldsListeners();
        startFakeLoading(viewBinding.progressLoadingFirst.progressBar);
    }

    private void setupForm() {
        if (fakeProgressAnimator != null && fakeProgressAnimator.isRunning()) {
            fakeProgressAnimator.cancel();
        }

        ValueAnimator completeAnimator = ValueAnimator.ofInt(currentProgress, 100);
        completeAnimator.setDuration(300);
        completeAnimator.setInterpolator(new DecelerateInterpolator());
        completeAnimator.addUpdateListener(anim -> {
            int value = (int) anim.getAnimatedValue();
            viewBinding.progressLoadingFirst.progressBar.setProgress(value);
        });

        completeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (ADDRESS_DETAIL != null) {
                    viewBinding.name.setText(Objects.requireNonNull(ADDRESS_DETAIL).getFullName());
                    viewBinding.phone.setText(ADDRESS_DETAIL.getPhoneNumber());
                    viewBinding.province.setText(ADDRESS_DETAIL.getProvince().getName());
                    viewBinding.district.setText(ADDRESS_DETAIL.getDistrict().getName());
                    viewBinding.commune.setText(ADDRESS_DETAIL.getCommune().getName());
                    viewBinding.address.setText(ADDRESS_DETAIL.getDetails());
                    viewModel.isDefault.set(ADDRESS_DETAIL.getIsDefault());
                    getListProvince(Constants.PAGE_START);
                    getListDistrict(PROVINCE.getId(), Constants.PAGE_START);
                    getListCommune(DISTRICT.getId(), Constants.PAGE_START);
                }
                viewBinding.progressLoadingFirst.getRoot().setVisibility(View.GONE);
            }
        });

        completeAnimator.start();
    }
    private void setupViewPager() {
        viewPagerAdapter = new AddressViewPagerAdapter(this);
        viewBinding.viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(viewBinding.tabLayout, viewBinding.viewPager, (tab, position) -> {
            tab.setText(titles[position]);
        }).attach();

        viewBinding.viewPager.setUserInputEnabled(false);

        updateTabVisibility();
    }
    private void updateTabVisibility() {
        TabLayout tabLayout = viewBinding.tabLayout;

        tabLayout.post(() -> {
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);

                    switch (i) {
                        case TYPE_PROVINCE:
                            tabView.setVisibility(View.VISIBLE);
                            tab.setText(PROVINCE != null ? PROVINCE.getName() : Constants.PROVINCE);
                            break;
                        case TYPE_DISTRICT:
                            tab.setText(DISTRICT != null ? DISTRICT.getName() : Constants.DISTRICT);
                            tabView.setVisibility(PROVINCE != null ? View.VISIBLE : View.INVISIBLE);
                            break;
                        case TYPE_COMMUNE:
                            tab.setText(COMMUNE != null ? COMMUNE.getName() : Constants.COMMUNE);
                            tabView.setVisibility(DISTRICT != null ? View.VISIBLE : View.INVISIBLE);
                            break;
                    }
                }
            }
            tabLayout.requestLayout();
        });

        // Cập nhật vị trí ViewPager
        if (PROVINCE == null) {
            viewBinding.viewPager.setCurrentItem(TYPE_PROVINCE);
        } else if (DISTRICT == null) {
            viewBinding.viewPager.setCurrentItem(TYPE_DISTRICT);
        } else if (COMMUNE == null) {
            viewBinding.viewPager.setCurrentItem(TYPE_COMMUNE);
        }
    }
    public void updateAddress(NationResponse value, int type) {
        switch (type) {
            case TYPE_PROVINCE:
                PROVINCE = value;
                getListDistrict(PROVINCE.getId(), Constants.PAGE_START);
                DISTRICT = null;
                COMMUNE = null;
                viewBinding.province.setText(PROVINCE.getName());
                viewBinding.district.setText("");
                viewBinding.commune.setText("");
                viewBinding.errProvince.setVisibility(View.GONE);
                viewBinding.errDistrict.setVisibility(View.VISIBLE);
                viewBinding.errCommune.setVisibility(View.VISIBLE);
                break;
            case TYPE_DISTRICT:
                DISTRICT = value;
                getListCommune(DISTRICT.getId(), Constants.PAGE_START);
                COMMUNE = null;
                viewBinding.district.setText(DISTRICT.getName());
                viewBinding.commune.setText("");
                viewBinding.errDistrict.setVisibility(View.GONE);
                viewBinding.errCommune.setVisibility(View.VISIBLE);
                break;
            case TYPE_COMMUNE:
                COMMUNE = value;
                viewBinding.commune.setText(COMMUNE.getName());
                viewBinding.errCommune.setVisibility(View.GONE);
                break;
        }

        updateTabVisibility();
    }
    public void setUpAddress() {
        viewBinding.province.setOnClickListener(v -> handleProvinceClick());
        viewBinding.district.setOnClickListener(v -> handleDistrictClick());
        viewBinding.commune.setOnClickListener(v -> handleCommuneClick());
    }
    private void handleProvinceClick() {
        hideKeyboard();
        navigateToSection(TYPE_PROVINCE);
        getListProvince(Constants.PAGE_START);
    }
    private void handleDistrictClick() {
        hideKeyboard();

        if (PROVINCE == null) {
            navigateToSection(TYPE_PROVINCE);
            getListProvince(Constants.PAGE_START);
            return;
        }

        navigateToSection(TYPE_DISTRICT);
        if (DISTRICT == null) {
            getListDistrict(PROVINCE.getId(), Constants.PAGE_START);
        }
    }
    private void handleCommuneClick() {
        hideKeyboard();

        if (PROVINCE == null) {
            navigateToSection(TYPE_PROVINCE);
            getListProvince(Constants.PAGE_START);
            return;
        }

        if (DISTRICT == null) {
            navigateToSection(TYPE_DISTRICT);
            getListDistrict(PROVINCE.getId(), Constants.PAGE_START);
            return;
        }

        navigateToSection(TYPE_COMMUNE);
        if (COMMUNE == null) {
            getListCommune(DISTRICT.getId(), Constants.PAGE_START);
        }
    }
    private void navigateToSection(int sectionType) {
        viewBinding.viewPager.setCurrentItem(sectionType);
        showRightMenu();
    }
    private void setupDrawer() {

        ViewGroup.LayoutParams params = viewBinding.selectAddress.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels;
        viewBinding.selectAddress.setLayoutParams(params);
    }
    public void showRightMenu() {
        if (!viewBinding.lMyAddress.isDrawerOpen(GravityCompat.END)) {
            viewBinding.lMyAddress.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            viewBinding.lMyAddress.openDrawer(GravityCompat.END);
        }
    }
    public void closeRightMenu() {
        if (viewBinding.lMyAddress.isDrawerOpen(GravityCompat.END)) {
            if (PROVINCE != null) {
                viewBinding.province.setText(PROVINCE.getName());
            } else {
                viewBinding.province.setText(null);
            }
            if (DISTRICT != null) {
                viewBinding.district.setText(DISTRICT.getName());
            } else {
                viewBinding.district.setText(null);
            }
            if (COMMUNE != null) {
                viewBinding.commune.setText(COMMUNE.getName());
            } else {
                viewBinding.commune.setText(null);
            }
            viewBinding.lMyAddress.closeDrawer(GravityCompat.END);
        }
    }
    private void setupLocationFieldsListeners() {
        TextWatcher locationWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validateLocationFields();
            }
        };

        viewBinding.province.addTextChangedListener(locationWatcher);
        viewBinding.district.addTextChangedListener(locationWatcher);
        viewBinding.commune.addTextChangedListener(locationWatcher);
    }
    private void validateLocationFields() {
        if (PROVINCE == null || viewBinding.province.getText().toString().isEmpty()) {
            viewBinding.errProvince.setVisibility(View.VISIBLE);
        } else {
            viewBinding.errProvince.setVisibility(View.GONE);
        }

        if (DISTRICT == null || viewBinding.district.getText().toString().isEmpty()) {
            viewBinding.errDistrict.setVisibility(View.VISIBLE);
        } else {
            viewBinding.errDistrict.setVisibility(View.GONE);
        }

        if (COMMUNE == null || viewBinding.commune.getText().toString().isEmpty()) {
            viewBinding.errCommune.setVisibility(View.VISIBLE);
        } else {
            viewBinding.errCommune.setVisibility(View.GONE);
        }
    }
    private void setupTextWatchers() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                View focusedView = getCurrentFocus();
                if (focusedView != null) {
                    validateField(focusedView.getId());
                }
            }
        };

        viewBinding.name.addTextChangedListener(textWatcher);
        viewBinding.phone.addTextChangedListener(textWatcher);
        viewBinding.address.addTextChangedListener(textWatcher);
    }
    private void setupFocusListeners() {
        View.OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
            if (!hasFocus) {
                validateField(v.getId());
            }
        };

        viewBinding.name.setOnFocusChangeListener(focusChangeListener);
        viewBinding.phone.setOnFocusChangeListener(focusChangeListener);
        viewBinding.address.setOnFocusChangeListener(focusChangeListener);
    }
    private boolean validateField(int viewId) {
        if (viewId == R.id.name) {
            String name = viewBinding.name.getText().toString().trim();
            if (name.length() < 2 || name.length() > 40) {
                viewBinding.errName.setVisibility(View.VISIBLE);
                return false;
            }
            viewBinding.errName.setVisibility(View.GONE);
        } else if (viewId == R.id.phone) {
            String phone = viewBinding.phone.getText().toString().trim();
            if (!phone.matches("^0\\d{9,10}$")) {
                viewBinding.errPhone.setVisibility(View.VISIBLE);
                return false;
            }
            viewBinding.errPhone.setVisibility(View.GONE);
        } else if (viewId == R.id.address) {
            String address = viewBinding.address.getText().toString().trim();
            if (address.length() < 5 || address.length() > 120) {
                viewBinding.errAddress.setVisibility(View.VISIBLE);
                return false;
            }
            viewBinding.errAddress.setVisibility(View.GONE);
        }
        return true;
    }
    private boolean validateAllFields() {
        boolean isValid = true;

        String name = viewBinding.name.getText().toString().trim();
        if (name.length() < 2 || name.length() > 40) {
            viewBinding.errName.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            viewBinding.errName.setVisibility(View.GONE);
        }

        String phone = viewBinding.phone.getText().toString().trim();
        if (!phone.matches("^0\\d{9,10}$")) {
            viewBinding.errPhone.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            viewBinding.errPhone.setVisibility(View.GONE);
        }

        if (PROVINCE == null) {
            viewBinding.errProvince.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            viewBinding.errProvince.setVisibility(View.GONE);
        }

        if (DISTRICT == null) {
            viewBinding.errDistrict.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            viewBinding.errDistrict.setVisibility(View.GONE);
        }

        if (COMMUNE == null) {
            viewBinding.errCommune.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            viewBinding.errCommune.setVisibility(View.GONE);
        }

        String address = viewBinding.address.getText().toString().trim();
        if (address.length() < 5 || address.length() > 120) {
            viewBinding.errAddress.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            viewBinding.errAddress.setVisibility(View.GONE);
        }

        return isValid;
    }

    public void onSaveClick() {
        if (validateAllFields()) {
            switch (TYPE_HANDLE_ADDRESS) {
                case Constants.TYPE_CREATE:
                    AddressRequest requestCreate = new AddressRequest();
                    requestCreate.setFullName(Objects.requireNonNull(viewBinding.name.getText()).toString());
                    requestCreate.setPhoneNumber(String.valueOf(viewBinding.phone.getText()));
                    requestCreate.setProvinceId(PROVINCE.getId());
                    requestCreate.setDistrictId(DISTRICT.getId());
                    requestCreate.setCommuneId(COMMUNE.getId());
                    requestCreate.setDetails(String.valueOf(viewBinding.address.getText()));
                    requestCreate.setIsDefault(viewBinding.makeDefault.isChecked());

                    viewModel.addNewAddress(requestCreate);
                    break;
                case Constants.TYPE_EDIT:
                    AddressRequest requestUpdate = new AddressRequest();
                    requestUpdate.setId(ADDRESS_DETAIL.getId());
                    requestUpdate.setFullName(Objects.requireNonNull(viewBinding.name.getText()).toString());
                    requestUpdate.setPhoneNumber(String.valueOf(viewBinding.phone.getText()));
                    requestUpdate.setProvinceId(PROVINCE.getId());
                    requestUpdate.setDistrictId(DISTRICT.getId());
                    requestUpdate.setCommuneId(COMMUNE.getId());
                    requestUpdate.setDetails(String.valueOf(viewBinding.address.getText()));
                    requestUpdate.setIsDefault(viewBinding.makeDefault.isChecked());

                    viewModel.updateAddress(requestUpdate);

                    break;
            }

        }
    }
    public void getListProvince(Integer page) {
        viewModel.getListProvince(new MainCalback<ResponseListObj<NationResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(ResponseListObj<NationResponse> object) {
                ProvinceFragment.updateNationList(object.getContent());
            }
            @Override
            public void doFail() {

            }
        }, Constants.KIND_PROVINCE, page);
    }
    public void getListDistrict(String parentId , Integer page) {
        viewModel.getListDistrictOrCommune(new MainCalback<ResponseListObj<NationResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(ResponseListObj<NationResponse> object) {
                DistrictFragment.updateNationList(object.getContent());
            }
            @Override
            public void doFail() {

            }
        }, Constants.KIND_DISTRICT, parentId, page);
    }
    public void getListCommune(String parentId , Integer page) {
        viewModel.getListDistrictOrCommune(new MainCalback<ResponseListObj<NationResponse>>() {
            @Override
            public void doError(Throwable error) {

            }

            @Override
            public void doSuccess() {

            }

            @Override
            public void doSuccess(ResponseListObj<NationResponse> object) {
                CommuneFragment.updateNationList(object.getContent());
            }
            @Override
            public void doFail() {

            }
        }, Constants.KIND_COMMUNE, parentId, page);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PROVINCE = null;
        DISTRICT = null;
        COMMUNE = null;
        TYPE_HANDLE_ADDRESS = null;
        ProvinceFragment.PROVINCE_LIST = new MutableLiveData<>();
        DistrictFragment.DISTRICT_LIST = new MutableLiveData<>();
        CommuneFragment.COMMUNE_LIST = new MutableLiveData<>();
        ADDRESS_DETAIL = null;

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_detail;
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
