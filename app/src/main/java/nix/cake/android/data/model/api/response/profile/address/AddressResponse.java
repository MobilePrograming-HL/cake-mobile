package nix.cake.android.data.model.api.response.profile.address;

import lombok.Data;
@Data
public class AddressResponse {
    private Long id;
    private String name;
    private String address;
    private String phone;
}
