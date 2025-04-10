package nix.cake.android.data.model.api.response.product;

import java.util.List;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private Double price;
    private String description;
    private Long quantity;
    private CategoryResponse category;
    private List<TagResponse> tags;
    private DiscountResponse discount;
    private int status;
    private String image;
    private Long totalSold;
}
