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
import android.widget.TextView;
import android.widget.Toast;

import com.example.iorder.MainActivity;
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
    TextView tableNameTextView, orderCodeTextView, totalPriceTextView;
    RecyclerView orderItemsRecyclerView;
    SharedPreferences sh;
    OrderedItemAdapter adapter;
    int orderId;
    Button clearButton;
    FloatingActionButton newOrderButton,paymentButton;
    static final String TAG = "orderd items ";
    ArrayList<OrderedItem> orderedItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        tableNameTextView = findViewById(R.id.tv_tableName);
        orderCodeTextView = findViewById(R.id.tv_orderCode);
        totalPriceTextView = findViewById(R.id.tv_totalPrice);
        orderItemsRecyclerView = findViewById(R.id.rv_orderedItems);
        clearButton = findViewById(R.id.btn_clear);
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        clearButton.setVisibility(View.INVISIBLE);
        newOrderButton = findViewById(R.id.btn_newOrder);
        newOrderButton.setOnClickListener(newOrderListener);
        paymentButton = findViewById(R.id.btn_pay);
        paymentButton.setOnClickListener(paymentListener);
        clearButton.setOnClickListener(clearListener);
        sh = getSharedPreferences("iOrder", 0);

        orderId = sh.getInt("orderCode", 0);

        api = ApiClient.getInstance().create(OrderMenuApi.class);
        getOrderedItems();

    }

    View.OnClickListener newOrderListener = v -> {
        startActivity(new Intent(this, MenuActivity.class));
    };

    private void getOrderedItems() {
        Call<JsonObject> call = api.getOrderedItems(orderId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d(TAG, "onResponse: " + response.body());
                JsonObject res = response.body();
                if (response.body().get("detail") != null) {
                    Log.d(TAG, "onResponse: "+response.body());
                    sh.edit().clear().commit();
                    startActivity(new Intent(OrderActivity.this, MainActivity.class));
                    finish();
                } else {
                    orderCodeTextView.setText("OrderCode: " + orderId);

                    tableNameTextView.setText("Table: " + response.body().get("table_name").getAsString());

                    totalPriceTextView.setText("Total Amount: Rs. " + response.body().get("totalPrice").getAsString());
                    JsonArray arr = response.body().getAsJsonArray("items");
                    Log.d(TAG, "onResponse: " + arr);
                    OrderedItem item;
                    orderedItemArrayList = new ArrayList<>();
                    for (int i = 0; i < arr.size(); i++) {
                        JsonObject obj = arr.get(i).getAsJsonObject();
                        Log.d(TAG, "onResponse: " + obj);
                        item = new OrderedItem();
                        item.setId(Integer.parseInt(obj.get("id").getAsString()));
                        item.setName(obj.get("name").getAsString());
                        item.setQuantity(Integer.parseInt(obj.get("quantity").getAsString()));
                        orderedItemArrayList.add(item);
                    }
                    adapter = new OrderedItemAdapter(orderedItemArrayList);
                    orderItemsRecyclerView.setAdapter(adapter);
                }
                if (res.get("transaction") != null) {
                    tableNameTextView.setText("Table: " + res.get("table_name").getAsString());
                    totalPriceTextView.setText("Bill: "+res.get("paymentStatus").getAsString()+"\n"+
                            "Method: "+res.get("transaction").getAsJsonObject().get("payment_method").getAsString()+"\n"+
                            "Amount: "+ res.get("transaction").getAsJsonObject().get("amount").getAsString()+"\n"+
                            "Transaction At: "+res.get("transaction").getAsJsonObject().get("transaction_datetime").getAsString());
                    paymentButton.setVisibility(View.INVISIBLE);
                    newOrderButton.setVisibility(View.INVISIBLE);
                    clearButton.setVisibility(View.VISIBLE);


                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    View.OnClickListener paymentListener = v->{
        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra("total",totalPriceTextView.getText());
        intent.putExtra("orderId",orderId);
        startActivity(intent);
    };
    View.OnClickListener clearListener = v->{
        sh.edit().clear().commit();
        startActivity(new Intent(OrderActivity.this, MainActivity.class));
        finish();
    };

    @Override
    protected void onResume() {
        super.onResume();
        getOrderedItems();
    }
}