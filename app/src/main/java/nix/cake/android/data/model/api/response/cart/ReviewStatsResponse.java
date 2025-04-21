package nix.cake.android.data.model.api.response.cart;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewStatsResponse {
    private String productId;
    private Long total;
    private Double average;
    private Map<String, Long> reviewCountByRate;
}