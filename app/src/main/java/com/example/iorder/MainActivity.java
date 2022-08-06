package com.example.iorder;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.iorder.activity.CameraActivity;
import com.example.iorder.activity.MenuActivity;
import com.example.iorder.activity.OrderActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {
    Button scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan= findViewById(R.id.btn_scan);
        scan.setOnClickListener(scanButtonListener);
        SharedPreferences sh = getSharedPreferences("iOrder",0);
        if(sh.contains("orderCode")){
            startActivity(new Intent(this, OrderActivity.class));
            finish();
        }


    }
    ActivityResultLauncher<ScanOptions> cameraLauncer = registerForActivityResult(new ScanContract(),result -> {
        if(result.getContents() != null){
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra("table_id",result.getContents());
            startActivity(intent);
        }
    });

    View.OnClickListener scanButtonListener = view -> {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume up to flash on");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CameraActivity.class);
        cameraLauncer.launch(scanOptions);

    };


}