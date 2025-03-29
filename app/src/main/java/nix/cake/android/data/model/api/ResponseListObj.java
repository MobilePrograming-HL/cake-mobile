package nix.cake.android.data.model.api;

import java.util.List;

import lombok.Data;

@Data
public class ResponseListObj<T> {
    private List<T> data;
    private Integer page;
    private Integer totalPage;
    private Long totalElements;
}
