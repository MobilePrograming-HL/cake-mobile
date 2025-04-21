package nix.cake.android.data.model.api.response.product;

import java.util.Map;

import lombok.Data;

@Data
public class ReviewStatsResponse {
    String productId;
    Long total;
    Double average;
    Map<Integer, Long> reviewCountByRate;
}
