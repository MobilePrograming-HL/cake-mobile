package nix.cake.android.data.model.api;

import lombok.Data;

@Data
public class ResponseWrapper<T> {
    private boolean result;
    private T data;
    private String message;
    private String code;

    public boolean isResult() {
        return result;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
