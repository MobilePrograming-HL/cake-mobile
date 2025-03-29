package nix.cake.android.data.model.api.response.product;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;
    private CategoryResponse category;

    public ProductResponse(Long id, String name, String description, Double price, String image, CategoryResponse category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
    }
}
