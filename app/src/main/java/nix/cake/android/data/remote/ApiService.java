package nix.cake.android.data.remote;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.ResponseWrapper;
import nix.cake.android.data.model.api.request.login.ActiveAccountRequest;
import nix.cake.android.data.model.api.request.login.LoginRequest;
import nix.cake.android.data.model.api.request.login.RefreshTokenRequest;
import nix.cake.android.data.model.api.request.login.SignUpRequest;
import nix.cake.android.data.model.api.response.login.LoginResponse;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @POST("v1/auth/login")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<LoginResponse>> login(@Body LoginRequest request);
    @POST("v1/auth/register")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper> register(@Body SignUpRequest request);
    @POST("v1/auth/active-account")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper> activeAccount(@Body ActiveAccountRequest request);
    @POST("v1/auth/refresh-token")
    Observable<ResponseWrapper<LoginResponse>> refreshToken(@Body String token);
    @POST("v1/auth/logout")
    Observable<String> logout(@Body String token);
    @GET("v1/category/list")
    @Headers({"IgnoreAuth: 1"})
    Observable<ResponseWrapper<ResponseListObj<CategoryResponse>>> getListCategory();

    @GET("v1/product/list")
    Observable<ResponseWrapper<ResponseListObj<ProductResponse>>> getListProduct(
            @Query("categoryId") String categoryId
    );
}
