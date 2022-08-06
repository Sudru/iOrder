package com.example.iorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.iorder.R;
import com.example.iorder.adapter.FoodCategoryAdapter;
import com.example.iorder.adapter.FoodItemsAdapter;
import com.example.iorder.api.OrderMenuApi;
import com.example.iorder.client.ApiClient;
import com.example.iorder.model.FoodCategory;
import com.example.iorder.model.FoodItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements FoodItemsAdapter.QuantityListener {
    private static final String TAG = "orderactiity";
    int orderId = 0;
    SharedPreferences sh;
    OrderMenuApi api;
    RecyclerView categoryRecyclerView;
    List<FoodCategory> foodCategoryList;
    HashMap<String, Object> order;
    ArrayList<HashMap<String, Integer>> orderItemList;
    Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        categoryRecyclerView = findViewById(R.id.rv_category);
        placeOrder = findViewById(R.id.btn_order);
        placeOrder.setOnClickListener(orderListener);
        //dummy test data
//        foodCategoryList = new ArrayList<>();
//        foodCategoryList = getDummyList();
//        categoryRecyclerView.setAdapter(new FoodCategoryAdapter(foodCategoryList, this));
//        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        order = new HashMap<>();
        orderItemList = new ArrayList<>();
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
                    Toast.makeText(MenuActivity.this, response.body().get("detail").toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MenuActivity.this, "Order Created", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = sh.edit();
                    edit.putInt("orderCode", Integer.parseInt(response.body().get("orderCode").toString()));
                    orderId = Integer.parseInt(response.body().get("orderCode").toString());
                    edit.commit();

                }
               getMenu();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
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
                FoodCategoryAdapter adapter = new FoodCategoryAdapter(foodCategoryList, MenuActivity.this);
                categoryRecyclerView.setAdapter(adapter);
                categoryRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));


            }

            @Override
            public void onFailure(Call<List<FoodCategory>> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private List<FoodCategory> getDummyList() {
//        ArrayList<FoodCategory> lst = new ArrayList<>();
//        ArrayList<FoodItem> itm = new ArrayList<>();
//        FoodCategory d;
//        FoodItem it;
//        for (int i = 0; i < 5; i++) {
//            it = new FoodItem();
//            it.setId(i);
//            it.setName(String.valueOf(i));
//            it.setPrice(i * 5);
//            itm.add(it);
//        }
//        for (int i = 0; i < 10; i++) {
//            d = new FoodCategory();
//            d.setName(String.valueOf(i));
//            d.setFooditems(itm);
//            lst.add(d);
//
//        }
//        return lst;
//    }

    View.OnClickListener orderListener = v -> {
        Gson gson = new Gson();
        String body = gson.toJson(order);
        Call<JsonObject> call = api.placeOrder(body, orderId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse of order: " + response.body());
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });



    };


    @Override
    public void onQuantityChanged(FoodItem item) {
        boolean found = false;

        for (HashMap<String, Integer> a : orderItemList) {
            if (a.get("fooditem") == item.getId()) {
                a.put("qty", item.getQuantity());
                if(item.getQuantity()==0){
                    orderItemList.remove(a);
                }
                found = true;
            }
        }
        if (!found) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("fooditem", item.getId());
            map.put("qty", item.getQuantity());
            orderItemList.add(map);
        }

        order.put("orderItems", orderItemList);
        Log.d(TAG, "onQuantityChanged: " + new Gson().toJson(order));
    }
}

