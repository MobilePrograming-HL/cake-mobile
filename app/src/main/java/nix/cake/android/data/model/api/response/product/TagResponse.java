package nix.cake.android.data.model.api.response.product;

import lombok.Data;

@Data
public class TagResponse {
    private String id;
    private String code;
    private String name;
    private boolean isSelected = false;
}
