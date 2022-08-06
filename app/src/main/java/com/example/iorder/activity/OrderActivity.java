package com.example.iorder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.iorder.R;
import com.example.iorder.adapter.OrderedItemAdapter;
import com.example.iorder.api.OrderMenuApi;
import com.example.iorder.client.ApiClient;
import com.example.iorder.model.OrderedItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    OrderMenuApi api;
    TextView tableNameTextView,orderCodeTextView,totalPriceTextView;
    RecyclerView orderItemsRecyclerView;
    SharedPreferences sh;
    OrderedItemAdapter adapter;
    int orderId;
    FloatingActionButton newOrderButton;
    static final String TAG ="orderd items ";
    ArrayList<OrderedItem> orderedItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        tableNameTextView = findViewById(R.id.tv_tableName);
        orderCodeTextView = findViewById(R.id.tv_orderCode);
        totalPriceTextView = findViewById(R.id.tv_totalPrice);
        orderItemsRecyclerView = findViewById(R.id.rv_orderedItems);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        newOrderButton = findViewById(R.id.btn_newOrder);
        newOrderButton.setOnClickListener(newOrderListener);
        sh = getSharedPreferences("iOrder",0);

        orderId = sh.getInt("orderCode",0);

        api = ApiClient.getInstance().create(OrderMenuApi.class);
        getOrderedItems();

    }

    View.OnClickListener newOrderListener = v->{
        startActivity(new Intent(this,MenuActivity.class));
    };
    private void getOrderedItems(){
        Call<JsonObject> call = api.getOrderedItems(orderId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: "+response.body());
                tableNameTextView.setText("Table: "+response.body().get("table_name").getAsString());
                orderCodeTextView.setText("OrderCode: "+orderId);
                totalPriceTextView.setText("Total Amount: Rs. "+ response.body().get("totalPrice").getAsString());
                JsonArray arr = response.body().getAsJsonArray("items");
                Log.d(TAG, "onResponse: "+arr);
                OrderedItem item;
                orderedItemArrayList = new ArrayList<>();
                for(int i = 0;i<arr.size();i++){
                    JsonObject obj = arr.get(i).getAsJsonObject();
                    Log.d(TAG, "onResponse: "+obj);
                    item = new OrderedItem();
                    item.setId(Integer.parseInt(obj.get("id").getAsString()));
                    item.setName(obj.get("name").getAsString());
                    item.setQuantity(Integer.parseInt(obj.get("quantity").getAsString()));
                    orderedItemArrayList.add(item);
                }
                adapter = new OrderedItemAdapter(orderedItemArrayList);
                orderItemsRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderedItems();
    }
}