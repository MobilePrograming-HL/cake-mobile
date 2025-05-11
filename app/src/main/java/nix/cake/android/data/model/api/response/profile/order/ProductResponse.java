package nix.cake.android.data.model.api.response.profile.order;

import java.util.List;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private Double price;
    private String description;
    private Integer quantity;
    private CategoryResponse category;
    private List<TagResponse> tags;
    private DiscountResponse discount;
    private Integer status;
    private String image;
    private List<String> images;
    private Integer totalSold;
    private ReviewStatsResponse reviewStats;
    public Double getPriceOfProduct() {
        if (discount == null) {
            return price;
        } else {
            return price * (1 - discount.getDiscountPercentage() / 100.0);
        }
    }
}
