package com.example.iorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.iorder.R;
import com.example.iorder.adapter.FoodCategoryAdapter;
import com.example.iorder.api.OrderMenuApi;
import com.example.iorder.client.ApiClient;
import com.example.iorder.model.FoodCategory;
import com.example.iorder.model.FoodItem;
import com.example.iorder.model.Order;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    int orderId = 0;
    SharedPreferences sh;
    OrderMenuApi api;
    RecyclerView categoryRecyclerView;
    List<FoodCategory> foodCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        categoryRecyclerView = findViewById(R.id.rv_category);
        //dummy test data
        foodCategoryList = getDummyList();
        FoodCategoryAdapter adapter = new FoodCategoryAdapter(foodCategoryList);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(adapter);

        api = ApiClient.getInstance().create(OrderMenuApi.class);
        sh = getSharedPreferences("iOrder", MODE_PRIVATE);
        if (sh.contains("orderCode")) {
            orderId = sh.getInt("orderCode", 0);
            getMenu();
            Log.d("TAG", "onCreate: " + orderId);
        } else {
            getOrderCode();
        }

    }

    private void getOrderCode() {
        Intent intent = getIntent();
        String tableNo = intent.getStringExtra("table_id");
        Call<JsonObject> call = api.getOrderId(tableNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("detail") != null) {
                    Toast.makeText(OrderActivity.this, response.body().get("detail").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderActivity.this, "Order Created", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = sh.edit();
                    edit.putInt("orderCode", Integer.parseInt(response.body().get("orderCode").toString()));
                    edit.commit();

                }
                if (orderId != 0) {
                    getMenu();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getMenu() {
        Call<List<FoodCategory>> caller = api.getMenu();
        caller.enqueue(new Callback<List<FoodCategory>>() {
            @Override
            public void onResponse(Call<List<FoodCategory>> call, Response<List<FoodCategory>> response) {
                Log.d("TAG", "onResponse: " + response.body());
                foodCategoryList = response.body();
                FoodCategoryAdapter adapter = new FoodCategoryAdapter(foodCategoryList);
                categoryRecyclerView.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<FoodCategory>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private List<FoodCategory> getDummyList(){
        ArrayList<FoodCategory> lst = new ArrayList<>();
        ArrayList<FoodItem> itm = new ArrayList<>();
        FoodCategory d;
        FoodItem it;
        for(int i=0;i<5;i++){
            it = new FoodItem();
            it.setName(String.valueOf(i));
            it.setPrice(i*5);
            itm.add(it);
        }
        for(int i = 0 ;i < 10;i++){
            d = new FoodCategory();
            d.setName(String.valueOf(i));
            d.setFooditems(itm);
            lst.add(d);

        }
        return lst;
    }
}