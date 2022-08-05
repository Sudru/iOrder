package com.example.iorder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.iorder.R;
import com.example.iorder.api.OrderMenuApi;
import com.example.iorder.client.ApiClient;
import com.example.iorder.model.Order;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        String tableNo = intent.getStringExtra("table_id");
        OrderMenuApi api = ApiClient.getInstance().create(OrderMenuApi.class);
        Call<JsonObject> call = api.getOrderId(tableNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("TAG", "onResponse: "+response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


        //get menu items
//        Call<List<FoodItem>> caller = api.getMenu();
//        caller.enqueue(new Callback<List<FoodItem>>() {
//            @Override
//            public void onResponse(Call<List<FoodItem>> call, Response<List<FoodItem>> response) {
//                Log.d("TAG", "onResponse: "+response.body());
//                for(FoodItem a :response.body()){
//                    Log.d("TAG", "onResponse: "+a.getFooditems().isEmpty());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<FoodItem>> call, Throwable t) {
//
//            }
//        });

    }
}