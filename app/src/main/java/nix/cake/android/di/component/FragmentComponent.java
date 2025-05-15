package nix.cake.android.di.component;


import nix.cake.android.di.module.FragmentModule;
import nix.cake.android.di.scope.FragmentScope;

import dagger.Component;
import nix.cake.android.ui.main.cart.CartFragment;
import nix.cake.android.ui.main.home.HomeFragment;
import nix.cake.android.ui.main.login.UnLoginFragment;
import nix.cake.android.ui.main.product.find.FindProductFragment;
import nix.cake.android.ui.main.profile.ProfileFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.CommuneFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.DistrictFragment;
import nix.cake.android.ui.main.profile.address.detail.fragment.ProvinceFragment;
import nix.cake.android.ui.main.shop.ShopFragment;

@FragmentScope
@Component(modules = {FragmentModule.class},dependencies = AppComponent.class)
public interface FragmentComponent {
    void inject(CartFragment fragment);
    void inject(HomeFragment fragment);
    void inject(ShopFragment fragment);
    void inject(ProfileFragment fragment);
    void inject(UnLoginFragment fragment);
    void inject(FindProductFragment fragment);
    void inject(ProvinceFragment fragment);
    void inject(DistrictFragment fragment);
    void inject(CommuneFragment fragment);
}
