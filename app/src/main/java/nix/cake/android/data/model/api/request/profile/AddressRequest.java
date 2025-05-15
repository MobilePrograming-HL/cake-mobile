package nix.cake.android.data.model.api.request.profile;

import lombok.Data;

@Data
public class AddressRequest {
    private String id;
    private String provinceId;
    private String districtId;
    private String communeId;
    private String details;
    private Boolean isDefault;
    private String fullName;
    private String phoneNumber;
}