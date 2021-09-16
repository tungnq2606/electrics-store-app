package com.example.myapplication.Myretrofit;

import com.example.myapplication.Model.AccessToken;
import com.example.myapplication.Model.Account;
import com.example.myapplication.Model.Cart;
import com.example.myapplication.Model.Categories;
import com.example.myapplication.Model.Products;
import com.example.myapplication.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IRetrofitService {
    @POST("user_login.php")
    Call<AccessToken> login(@Body Account account);

    @POST("user_register.php")
    Call<String> register(@Body User user);

    @POST("user_profile.php")
    Call<User> getProfile();

    @POST("user_changepassword.php")
    Call<String> changePassword(@Body Account account);

    @POST("user_forgot_password.php")
    Call<String> forgotPassword(@Body Account account);

    @POST("categories.php")
    Call<List<Categories>> getCategories();

    @POST("products.php")
    Call<List<Products>> getProducts();

    @POST("add_category.php")
    Call<String> addCategory(@Body Categories categories);

    @POST("update_category.php")
    Call<String> updateCategory(@Body Categories categories);

    @POST("delete_category.php")
    Call<String> deleteCategory(@Body Categories categories);

    @POST("add_product.php")
    Call<String> addProduct(@Body Products products);

    @POST("update_product.php")
    Call<String> updateProduct(@Body Products products);

    @POST("delete_product.php")
    Call<String> deleteProduct(@Body Products products);

    @POST("cart.php")
    Call<List<Cart>> getCart();
}
