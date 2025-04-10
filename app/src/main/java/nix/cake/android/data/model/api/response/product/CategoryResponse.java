package nix.cake.android.data.model.api.response.product;

import lombok.Data;

@Data
public class CategoryResponse {
    private String id;
    private String code;
    private String name;
    private String description;
    private String image;

    public CategoryResponse(String id, String name, String description, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
