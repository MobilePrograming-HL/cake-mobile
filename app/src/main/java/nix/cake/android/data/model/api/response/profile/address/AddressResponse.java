package nix.cake.android.data.model.api.response.profile.address;

import java.util.Objects;

import lombok.Data;
@Data
public class AddressResponse {
    private String id;
    private NationResponse province;
    private NationResponse district;
    private NationResponse commune;
    private String details;
    private String fullName;
    private String phoneNumber;
    private Boolean isDefault;
    private Boolean isSelect = false;
}
