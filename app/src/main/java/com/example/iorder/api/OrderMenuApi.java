package com.example.iorder.api;

import com.example.iorder.model.FoodItem;
import com.example.iorder.model.Order;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderMenuApi {

    @GET("/api/create-order/{id}")
    Call<JsonObject> getOrderId(@Path("id")String id);

    @GET("/api/menu")
    Call<List<FoodItem>> getMenu();
}
