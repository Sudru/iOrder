package com.example.iorder.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.iorder.R;
import com.example.iorder.api.OrderMenuApi;
import com.example.iorder.client.ApiClient;
import com.example.iorder.model.FoodCategory;
import com.example.iorder.model.Order;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    int orderId =0;
    SharedPreferences sh;
    OrderMenuApi api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Intent intent = getIntent();
        String tableNo = intent.getStringExtra("table_id");
        api = ApiClient.getInstance().create(OrderMenuApi.class);
        sh = getSharedPreferences("iOrder",MODE_PRIVATE);
        if(sh.contains("orderCode")){
            orderId = sh.getInt("orderCode",0);
            getMenu();
            Log.d("TAG", "onCreate: "+ orderId);
        }
        Call<JsonObject> call = api.getOrderId(tableNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body().get("detail")!=null){
                    Toast.makeText(OrderActivity.this, response.body().get("detail").toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(OrderActivity.this, "Order Created", Toast.LENGTH_SHORT).show();
                    orderId = Integer.parseInt(response.body().get("orderCode").toString());
                    Log.d("TAG", "onResponse: "+orderId);
                    SharedPreferences.Editor edit = sh.edit();
                    edit.putInt("orderCode",Integer.parseInt(response.body().get("orderCode").toString()));
                    edit.commit();
                }
                if(orderId!=0){
                    getMenu();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



        //get menu items


    }
    private void getMenu(){
        Call<List<FoodCategory>> caller = api.getMenu();
        caller.enqueue(new Callback<List<FoodCategory>>() {
            @Override
            public void onResponse(Call<List<FoodCategory>> call, Response<List<FoodCategory>> response) {
                Log.d("TAG", "onResponse: " + response.body());
                for(FoodCategory a : response.body()){
                    Log.d("TAG", "menu items: " + a.getDescription());
                }

            }

            @Override
            public void onFailure(Call<List<FoodCategory>> call, Throwable t) {

            }
        });

    }
}