package nix.cake.android.data.model.api.response.profile.address;

import lombok.Data;

@Data
public class NationResponse {
    private String id;
    private String name;
    private Integer kind;
    private String parentId;
    private boolean isSelected = false;
}
