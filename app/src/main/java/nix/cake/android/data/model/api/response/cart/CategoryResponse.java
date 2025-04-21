package nix.cake.android.data.model.api.response.cart;

import lombok.Data;

@Data
public class CategoryResponse {
    private String id;
    private String code;
    private String name;
    private String description;
    private String image;
}