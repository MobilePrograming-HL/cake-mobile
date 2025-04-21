package nix.cake.android.data.remote;

import io.reactivex.rxjava3.core.Observable;
import nix.cake.android.data.model.api.ResponseListObj;
import nix.cake.android.data.model.api.ResponseWrapper;
import nix.cake.android.data.model.api.request.cart.CartRequest;
import nix.cake.android.data.model.api.request.cart.UpdateCartItemRequest;
import nix.cake.android.data.model.api.request.login.ActiveAccountRequest;
import nix.cake.android.data.model.api.request.login.LoginRequest;
import nix.cake.android.data.model.api.request.login.SignUpRequest;
import nix.cake.android.data.model.api.request.profile.AddressRequest;
import nix.cake.android.data.model.api.response.cart.CartResponse;
import nix.cake.android.data.model.api.response.login.LoginResponse;
import nix.cake.android.data.model.api.response.product.CategoryResponse;
import nix.cake.android.data.model.api.response.product.ProductResponse;
import nix.cake.android.data.model.api.response.profile.address.AddressResponse;
import nix.cake.android.data.model.api.response.profile.address.NationResponse;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
            @Query("categoryId") String categoryId,
            @Query("page") Integer page
    );
    @GET("v1/product/get/{id}")
    Observable<ResponseWrapper<ProductResponse>> getProductDetail(@Path("id") String id);

    @POST("v1/cart/add-item")
    Observable<ResponseWrapper> addCartItem(@Body CartRequest request);
    @GET("v1/nation/list")
    Observable<ResponseWrapper<ResponseListObj<NationResponse>>> getListProvince(
            @Query("kind") Integer kind,
            @Query("page") Integer page
    );

    @GET("v1/nation/list")
    Observable<ResponseWrapper<ResponseListObj<NationResponse>>> getListDistrictOrCommune(
            @Query("kind") Integer kind,
            @Query("parentId") String parentId,
            @Query("page") Integer page
    );

    @POST("v1/address/create")
    Observable<ResponseWrapper> addNewAddress(@Body AddressRequest request);

    @GET("v1/address/list")
    Observable<ResponseWrapper<ResponseListObj<AddressResponse>>> getListAddress(
            @Query("page") Integer page
    );

    @PUT("v1/address/set-default/{id}")
    Observable<ResponseWrapper> setDefaultAddress(@Path("id") String id);

    @DELETE("v1/address/delete/{id}")
    Observable<ResponseWrapper> deleteAddress(@Path("id") String id);

    @GET("v1/address/get/{id}")
    Observable<ResponseWrapper<AddressResponse>> getAddressDetail(@Path("id") String id);

    @PUT("v1/address/update")
    Observable<ResponseWrapper> updateAddress(@Body AddressRequest request);

    @GET("v1/cart/get")
    Observable<ResponseWrapper<CartResponse>> getCart();

    @PUT("v1/cart-item/update")
    Observable<ResponseWrapper> updateCartItem(@Body UpdateCartItemRequest request);
}
