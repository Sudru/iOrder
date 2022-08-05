package com.example.iorder.api;

import com.example.iorder.model.FoodCategory;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderMenuApi {

    @GET("/api/create-order/{id}")
    Call<JsonObject> getOrderId(@Path("id")String id);

    @GET("/api/menu")
    Call<List<FoodCategory>> getMenu();
}
