package nix.cake.android.ui.main.profile.address;

import java.util.ArrayList;
import java.util.List;

import nix.cake.android.MVVMApplication;
import nix.cake.android.data.Repository;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.ui.base.activity.BaseViewModel;

public class ShippingAddressViewModel extends BaseViewModel {

    List<AddressResponse> addressList;
    public ShippingAddressViewModel(Repository repository, MVVMApplication application) {
        super(repository, application);
        addressList = createDummyAddresses();
    }
    private List<AddressResponse> createDummyAddresses() {
        List<AddressResponse> addresses = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            AddressResponse address = new AddressResponse();
            address.setId((long) i);
            address.setName("Name " + i);
            address.setAddress("Address " + i + ", City, Country");
            address.setPhone("+1 555-000" + i);
            addresses.add(address);
        }

        return addresses;
    }
}
