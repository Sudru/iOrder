package com.example.iorder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.iorder.activity.CameraActivity;
import com.example.iorder.activity.MenuActivity;
import com.example.iorder.activity.OrderActivity;
import com.example.iorder.api.OrderMenuApi;
import com.example.iorder.client.ApiClient;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button scan;
    OrderMenuApi api;
    int orderId;
    SharedPreferences sh;
    String tableNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan= findViewById(R.id.btn_scan);
        scan.setOnClickListener(scanButtonListener);
        api = ApiClient.getInstance().create(OrderMenuApi.class);
        sh = getSharedPreferences("iOrder",0);
        SharedPreferences sh = getSharedPreferences("iOrder",0);
        if(sh.contains("orderCode")&&sh.getInt("orderCode",0)!=0){
            startActivity(new Intent(this,OrderActivity.class));
            finish();
        }



    }
    ActivityResultLauncher<ScanOptions> cameraLauncer = registerForActivityResult(new ScanContract(),result -> {
        if(result.getContents() != null){
            tableNo = result.getContents();
            Call<JsonObject> call = api.getOrderId(tableNo);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().get("detail") != null) {
                        Toast.makeText(MainActivity.this, response.body().get("detail").toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Order Created", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor edit = sh.edit();
                        edit.putInt("orderCode", Integer.parseInt(response.body().get("orderCode").toString()));
                        orderId = Integer.parseInt(response.body().get("orderCode").toString());
                        edit.commit();

                    }
                    Intent intent = new Intent(MainActivity.this,OrderActivity.class);
                    intent.putExtra("orderCode",orderId);
                    startActivity(intent);
                    finish();

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

    View.OnClickListener scanButtonListener = view -> {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume up to Turn On Flash");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CameraActivity.class);
        cameraLauncer.launch(scanOptions);

    };

}