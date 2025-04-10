package nix.cake.android.di.component;

import nix.cake.android.di.module.ActivityModule;
import nix.cake.android.di.scope.ActivityScope;
import nix.cake.android.ui.main.MainActivity;

import dagger.Component;
import nix.cake.android.ui.main.login.LoginActivity;
import nix.cake.android.ui.main.login.SignUpActivity;
import nix.cake.android.ui.main.login.VerifyOtpActivity;
import nix.cake.android.ui.main.product.detail.ProductDetailActivity;
import nix.cake.android.ui.main.product.filter.FilterProductActivity;
import nix.cake.android.ui.main.product.find.FindProductActivity;
import nix.cake.android.ui.main.profile.address.ShippingAddressActivity;
import nix.cake.android.ui.main.profile.address.detail.AddressDetailActivity;
import nix.cake.android.ui.main.profile.order.MyOrdersActivity;
import nix.cake.android.ui.main.profile.order.detail.OrderDetailActivity;

@ActivityScope
@Component(modules = {ActivityModule.class}, dependencies = AppComponent.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(SignUpActivity activity);
    void inject(MyOrdersActivity activity);
    void inject(OrderDetailActivity activity);
    void inject(ShippingAddressActivity activity);
    void inject(AddressDetailActivity activity);
    void inject(FilterProductActivity activity);
    void inject(ProductDetailActivity activity);
    void inject(VerifyOtpActivity activity);
    void inject(FindProductActivity activity);
}

