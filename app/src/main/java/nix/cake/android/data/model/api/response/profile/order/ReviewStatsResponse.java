package nix.cake.android.data.model.api.response.profile.order;

import java.util.Map;

import lombok.Data;

@Data
public class ReviewStatsResponse {
    private String productId;
    private Integer total;
    private Double average;
    private Map<String, Integer> reviewCountByRate;
}
