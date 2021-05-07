package com.example.baigiuaky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ConnectDB connectDB;
    Button btnXe, btnTaiXe, btnChuyen, btnPhanCong, btnThongTin, btnThongKe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Quản Lý Vận Tải");
        actionBar.setDisplayShowHomeEnabled(true);

        //Tạo Database
       // connectDB = new ConnectDB(this,"QuanLyVanTai.db",null,1);

        //Tạo bảng Tài Xế
    //    connectDB.QueryData("CREATE TABLE IF NOT EXISTS TaiXe(MaTX INTEGER PRIMARY KEY AUTOINCREMENT, TenTX VARCHAR(200), NgaySinh VARCHAR(50), DiaChi VARCHAR(200))");


        //thêm dữ liệu
     //   connectDB.QueryData("INSERT INTO TaiXe VALUES (null,'Nguyen Van A','01/01/1999','Ha Noi')");

        //ánh xạ
        setControl();

        //Bắt sự kiện
        btnThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ThongTinActivity.class);
                startActivity(intent);
            }
        });

        btnTaiXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TaiXeActivity.class);
                startActivity(intent);
            }
        });

        btnChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,TuyenXeActivity.class);
                startActivity(intent);
            }
        });

        btnXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,XeActivity.class);
                startActivity(intent);
            }
        });

        btnPhanCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PhanCongActivity.class);
                startActivity(intent);
            }
        });
        btnThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ThongKeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this,"Search Clicked !",Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    public void setControl(){
        btnXe = findViewById(R.id.btnXe);
        btnChuyen = findViewById(R.id.btnChuyen);
        btnPhanCong = findViewById(R.id.btnPhanCong);
        btnTaiXe = findViewById(R.id.btnTaiXe);
        btnThongTin = findViewById(R.id.btnThongTin);
        btnThongKe = findViewById(R.id.btnThongKe);
    }
}