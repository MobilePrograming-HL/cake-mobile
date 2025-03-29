package nix.cake.android.data.remote;

import io.reactivex.rxjava3.core.Observable;
import nix.cake.android.data.model.api.ResponseWrapper;
import nix.cake.android.data.model.api.request.login.LoginRequest;
import nix.cake.android.data.model.api.request.login.RefreshTokenRequest;
import nix.cake.android.data.model.api.response.login.LoginResponse;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("v1/auth/login")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<LoginResponse>> login(@Body LoginRequest request);
    @POST("v1/auth/refresh-token")
    Observable<ResponseWrapper<LoginResponse>> refreshToken(@Body String token);
}
