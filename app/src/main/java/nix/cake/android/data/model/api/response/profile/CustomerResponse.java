package nix.cake.android.data.model.api.response.profile;

import lombok.Data;

@Data
public class CustomerResponse {
    private String id;
    private String username;
    private String email;
    private String avatarPath;
    private String firstName;
    private String lastName;
    private String fullName;
    private String dob;
    private String phoneNumber;
    private long loyalty;
}