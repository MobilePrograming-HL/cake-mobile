package nix.cake.android.data.model.api.response.cart;

import static nix.cake.android.utils.DisplayUtils.convertDoubleTwoDecimalsHasCurrency;

import android.graphics.Paint;
import android.view.View;

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
    private Integer status;
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
