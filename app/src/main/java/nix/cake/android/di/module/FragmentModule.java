package nix.cake.android.di.module;

import android.content.Context;

import androidx.core.util.Supplier;
import androidx.lifecycle.ViewModelProvider;

import nix.cake.android.MVVMApplication;
import nix.cake.android.ViewModelProviderFactory;
import nix.cake.android.data.Repository;
import nix.cake.android.di.scope.FragmentScope;
import nix.cake.android.ui.base.fragment.BaseFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import nix.cake.android.ui.main.cart.CartViewModel;
import nix.cake.android.ui.main.home.HomeViewModel;
import nix.cake.android.ui.main.login.UnLoginViewModel;
import nix.cake.android.ui.main.profile.ProfileViewModel;
import nix.cake.android.ui.main.shop.ShopViewModel;

@Module
public class FragmentModule {

    private BaseFragment<?, ?> fragment;

    public FragmentModule(BaseFragment<?, ?> fragment) {
        this.fragment = fragment;
    }

    @Named("access_token")
    @Provides
    @FragmentScope
    String provideToken(Repository repository) {
        return repository.getToken();
    }

    @Provides
    @FragmentScope
    CartViewModel provideCartViewModel(Repository repository, Context application) {
        Supplier<CartViewModel> supplier = () -> new CartViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<CartViewModel> factory = new ViewModelProviderFactory<>(CartViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(CartViewModel.class);
    }

    @Provides
    @FragmentScope
    HomeViewModel provideHomeViewModel(Repository repository, Context application) {
        Supplier<HomeViewModel> supplier = () -> new HomeViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<HomeViewModel> factory = new ViewModelProviderFactory<>(HomeViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(HomeViewModel.class);
    }

    @Provides
    @FragmentScope
    ShopViewModel provideShopViewModel(Repository repository, Context application) {
        Supplier<ShopViewModel> supplier = () -> new ShopViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<ShopViewModel> factory = new ViewModelProviderFactory<>(ShopViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ShopViewModel.class);
    }

    @Provides
    @FragmentScope
    ProfileViewModel provideProfileViewModel(Repository repository, Context application) {
        Supplier<ProfileViewModel> supplier = () -> new ProfileViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<ProfileViewModel> factory = new ViewModelProviderFactory<>(ProfileViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(ProfileViewModel.class);
    }
    @Provides
    @FragmentScope
    UnLoginViewModel provideUnLoginViewModel(Repository repository, Context application) {
        Supplier<UnLoginViewModel> supplier = () -> new UnLoginViewModel(repository, (MVVMApplication)application);
        ViewModelProviderFactory<UnLoginViewModel> factory = new ViewModelProviderFactory<>(UnLoginViewModel.class, supplier);
        return new ViewModelProvider(fragment, factory).get(UnLoginViewModel.class);
    }
}
