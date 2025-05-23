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
    private List<String> images;
    private Long totalSold;
    private ReviewStatsResponse reviewStats;
    public Double getPriceOfProduct() {
        if (discount == null) {
            return price;
        } else {
            return price * (1 - discount.getDiscountPercentage() / 100.0);
        }
    }
}
