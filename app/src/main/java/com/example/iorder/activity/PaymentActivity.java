package com.example.iorder.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iorder.R;
import com.example.iorder.api.OrderMenuApi;
import com.example.iorder.client.ApiClient;
import com.example.iorder.model.PaymentMethod;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    TextView total;
    ImageView esewaButton,bankButton;
    OrderMenuApi api;
    int orderId;
    private static final String TAG="payment";
    ArrayList<PaymentMethod> paymentMethodArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        total = findViewById(R.id.tv_totalPrice_pay);
        esewaButton = findViewById(R.id.btn_esewa);
        bankButton = findViewById(R.id.btn_bank);
        esewaButton.setOnClickListener(esewaListener);
        bankButton.setOnClickListener(bankListener);
        total.setText(getIntent().getStringExtra("total"));
        orderId = getIntent().getIntExtra("orderId",0);
        api = ApiClient.getInstance().create(OrderMenuApi.class);
        getPaymentMethods();
    }
    private void getPaymentMethods(){
        Call<JsonArray> call = api.getPaymentMethods(orderId);
        paymentMethodArrayList = new ArrayList<>();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.d(TAG, "onResponse: "+response.body());
                JsonArray arr = response.body().getAsJsonArray();
                for(int i=0;i<arr.size();i++){
                    JsonObject obj = arr.get(i).getAsJsonObject();
                    int id = obj.get("id").getAsInt();
                    String method = obj.get("method").getAsString();
                    String information = obj.get("information").getAsString();
                    paymentMethodArrayList.add(new PaymentMethod(id,method,information));

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    View.OnClickListener esewaListener = v->{
        HashMap<String,Integer> map = new HashMap<>();
        for(PaymentMethod p : paymentMethodArrayList){
            if(p.getMethod().equals("E-sewa")){
                map.put("id",p.getId());
            }
        }
        String body = new Gson().toJson(map);
        Log.d(TAG, "body: "+body);
        Call<JsonObject> call = api.addPayment(orderId,body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    };
    View.OnClickListener bankListener = v->{
        HashMap<String,Integer> map = new HashMap<>();
        for(PaymentMethod p : paymentMethodArrayList){
            if(p.getMethod().equals("Bank-Transfer")){
                map.put("id",p.getId());
            }
        }
        String body = new Gson().toJson(map);
        Log.d(TAG, "body: "+body);
        Call<JsonObject> call = api.addPayment(orderId,body);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    };

}