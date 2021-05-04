package com.example.baigiuaky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThongTinActivity extends AppCompatActivity {

    Button btnThoat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);

        btnThoat = findViewById(R.id.btnThoat);

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThongTinActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}