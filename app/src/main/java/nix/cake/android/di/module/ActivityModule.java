package nix.cake.android.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import nix.cake.android.MVVMApplication;
import nix.cake.android.ViewModelProviderFactory;
import nix.cake.android.data.Repository;
import nix.cake.android.di.scope.ActivityScope;
import nix.cake.android.di.scope.FragmentScope;
import nix.cake.android.ui.base.activity.BaseActivity;
import nix.cake.android.ui.main.MainViewModel;
import nix.cake.android.ui.main.login.LoginViewModel;
import nix.cake.android.ui.main.login.SignUpViewModel;
import nix.cake.android.ui.main.product.filter.FilterProductViewModel;
import nix.cake.android.ui.main.profile.address.ShippingAddressViewModel;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailViewModel;
import nix.cake.android.ui.main.profile.order.MyOrdersViewModel;
import nix.cake.android.ui.main.profile.order.detail.OrderDetailViewModel;
import nix.cake.android.utils.GetInfo;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private BaseActivity<?, ?> activity;

    public ActivityModule(BaseActivity<?, ?> activity) {
        this.activity = activity;
    }

    @Named("access_token")
    @Provides
    @ActivityScope
    String provideToken(Repository repository){
        return repository.getToken();
    }

    @Named("device_id")
    @Provides
    @ActivityScope
    String provideDeviceId( Context applicationContext){
        return GetInfo.getAll(applicationContext);
    }


    @Provides
    @ActivityScope
    MainViewModel provideMainViewModel(Repository repository, Context application) {
        Supplier<MainViewModel> supplier = () -> new MainViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<MainViewModel> factory = new ViewModelProviderFactory<>(MainViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MainViewModel.class);
    }

    @Provides
    @ActivityScope
    LoginViewModel provideLoginViewModel(Repository repository, Context application) {
        Supplier<LoginViewModel> supplier = () -> new LoginViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<LoginViewModel> factory = new ViewModelProviderFactory<>(LoginViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(LoginViewModel.class);
    }

    @Provides
    @ActivityScope
    SignUpViewModel provideSignUpViewModel(Repository repository, Context application) {
        Supplier<SignUpViewModel> supplier = () -> new SignUpViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<SignUpViewModel> factory = new ViewModelProviderFactory<>(SignUpViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(SignUpViewModel.class);
    }

    @Provides
    @ActivityScope
    MyOrdersViewModel provideMyOrdersViewModel(Repository repository, Context application) {
        Supplier<MyOrdersViewModel> supplier = () -> new MyOrdersViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<MyOrdersViewModel> factory = new ViewModelProviderFactory<>(MyOrdersViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(MyOrdersViewModel.class);
    }

    @Provides
    @ActivityScope
    OrderDetailViewModel provideOrderDetailViewModel(Repository repository, Context application) {
        Supplier<OrderDetailViewModel> supplier = () -> new OrderDetailViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<OrderDetailViewModel> factory = new ViewModelProviderFactory<>(OrderDetailViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(OrderDetailViewModel.class);
    }

    @Provides
    @ActivityScope
    ShippingAddressViewModel provideShippingAddressViewModel(Repository repository, Context application) {
        Supplier<ShippingAddressViewModel> supplier = () -> new ShippingAddressViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<ShippingAddressViewModel> factory = new ViewModelProviderFactory<>(ShippingAddressViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(ShippingAddressViewModel.class);
    }

    @Provides
    @ActivityScope
    AddressDetailViewModel provideAddressDetailViewModel(Repository repository, Context application) {
        Supplier<AddressDetailViewModel> supplier = () -> new AddressDetailViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<AddressDetailViewModel> factory = new ViewModelProviderFactory<>(AddressDetailViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(AddressDetailViewModel.class);
    }

    @Provides
    @ActivityScope
    FilterProductViewModel provideFilterProductViewModel(Repository repository, Context application) {
        Supplier<FilterProductViewModel> supplier = () -> new FilterProductViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<FilterProductViewModel> factory = new ViewModelProviderFactory<>(FilterProductViewModel.class, supplier);
        return new ViewModelProvider(activity, factory).get(FilterProductViewModel.class);
    }
}
