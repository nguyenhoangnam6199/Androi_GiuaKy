package com.example.baigiuaky;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiuaky.Adapter.TaiXeAdapter;
import com.example.baigiuaky.Models.TaiXe;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaiXeActivity extends AppCompatActivity {
    ConnectDB connectDB;
   // Button btnThemTX, btnThoat;
    ListView listView;
    ArrayList<TaiXe> arrayList;
    TaiXeAdapter adapter;
    Button btnThem1, btnThoat1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tai_xe);

        //Tạo Database
        connectDB = new ConnectDB(this,"QuanLyVanTai.db",null,1);
//
        //Tạo bảng Tài Xế
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS TaiXe(MaTX INTEGER PRIMARY KEY AUTOINCREMENT, TenTX VARCHAR(200), NgaySinh VARCHAR(50), DiaChi VARCHAR(200))");

        //ánh xạ
        listView = findViewById(R.id.lvDSTX);
        btnThem1 = findViewById(R.id.btnThemTX);
        btnThoat1 = findViewById(R.id.btnThoat);
        arrayList=new ArrayList<>();
        adapter = new TaiXeAdapter(this,R.layout.item_tai_xe,arrayList);
        listView.setAdapter(adapter);

        //Bắt sự kiện
        btnThem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hàm hiển thị dialog để thêm tài xế
                DialogInsert();

            }
        });

        btnThoat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hàm hiển thị dialog để thoát
                Intent i = new Intent(TaiXeActivity.this,MainActivity.class);
                startActivity(i);
            }
        });


        //Truy xuất dữ liệu
        GetData();
    }

    public boolean DinhDangTG(String x){
        boolean kt = false;
        Pattern pattern = Pattern.compile("^\\d{2}[/]\\d{2}[/]\\d{4}$");
        if(pattern.matcher(x).matches()){
            kt = true;
        }
        return kt;
    }

    //Hàm thêm tài xế
    public void DialogInsert(){
        Dialog dialog = new Dialog(TaiXeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_tai_xe);

        EditText edtTenTX, edtNS, edtDC;
        edtTenTX = dialog.findViewById(R.id.edtTenTX);
        edtNS = dialog.findViewById(R.id.edtNgaySinh);
        edtDC = dialog.findViewById(R.id.edtDiaChi);

        Button btnLuu, btnHuy;
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

//        edtTenTX.setText(ten);
//        edtNS.setText(ns);
//        edtDC.setText(dc);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten, ns, dc;
                ten = edtTenTX.getText().toString().trim();
                ns = edtNS.getText().toString().trim();
                dc = edtDC.getText().toString().trim();
                if(ten.isEmpty()){
                    edtTenTX.setError("Tên tài xế không được trống !");
                    edtTenTX.requestFocus();
                    return;
                }
                else if(ns.isEmpty()){
                    edtNS.setError("Ngày sinh không được trống !");
                    edtNS.requestFocus();
                    return;
                }
                else if(!DinhDangTG(ns)){
                    edtNS.setError("Ngày sinh không đúng định dạng dd/MM/yyyy !");
                    edtNS.requestFocus();
                    return;
                }
                else if(dc.isEmpty()){
                    edtDC.setError("Địa chỉ không được trống !");
                    edtDC.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("INSERT INTO TaiXe VALUES (null,'"+ten+"','"+ns+"','"+dc+"')");
                    GetData();
                    Toast.makeText(TaiXeActivity.this,"Thêm thành công !",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public void GetData(){
        Cursor dataTaiXe = connectDB.GetData("SELECT * FROM TaiXe ORDER BY TenTX ASC");
        arrayList.clear();
        while(dataTaiXe.moveToNext()){
            int id = dataTaiXe.getInt(0);
            String tentx = dataTaiXe.getString(1);
            String ngaysinh = dataTaiXe.getString(2);
            String diachi = dataTaiXe.getString(3);
            arrayList.add(new TaiXe(id,tentx,ngaysinh,diachi));
        }
        adapter.notifyDataSetChanged();
    }

    public void DialogUpdate(int id, String ten, String ns, String dc){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_tai_xe);

        TextView tieude;
        EditText edtTenTX, edtNS, edtDC;
        edtTenTX = dialog.findViewById(R.id.edtTenTX);
        edtNS = dialog.findViewById(R.id.edtNgaySinh);
        edtDC = dialog.findViewById(R.id.edtDiaChi);
        tieude = dialog.findViewById(R.id.tvTieuDe);

        Button btnLuu, btnHuy;
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        tieude.setText("CẬP NHẬT THÔNG TIN");
        edtTenTX.setText(ten);
        edtNS.setText(ns);
        edtDC.setText(dc);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten, ns, dc;
                ten = edtTenTX.getText().toString().trim();
                ns = edtNS.getText().toString().trim();
                dc = edtDC.getText().toString().trim();
                if(ten.isEmpty()){
                    edtTenTX.setError("Tên tài xế không được trống !");
                    edtTenTX.requestFocus();
                    return;
                }
                else if(ns.isEmpty()){
                    edtNS.setError("Ngày sinh không được trống !");
                    edtNS.requestFocus();
                    return;
                }
                else if(!DinhDangTG(ns)){
                    edtNS.setError("Ngày sinh không đúng định dạng dd/MM/yyyy !");
                    edtNS.requestFocus();
                    return;
                }
                else if(dc.isEmpty()){
                    edtDC.setError("Địa chỉ không được trống !");
                    edtDC.requestFocus();
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE TaiXe SET TenTX = '"+ten+"', NgaySinh = '"+ns+"', DiaChi='"+dc+"' WHERE MaTX = '"+id+"' ");
                    GetData();
                    Toast.makeText(TaiXeActivity.this,"Sửa thành công !",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public int getMaTX(String ten){
        int x = 0;
        Cursor data = connectDB.GetData("SELECT * FROM TaiXe WHERE TenTX='"+ten+"'");
        while (data.moveToNext()){
            x = data.getInt(0);
        }
        return x;
    }
    public boolean KtraTaiXe(String ten){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM PhanCong WHERE MaTaiXe='"+getMaTX(ten)+"'");
        while(data.moveToNext()){
            kt = true;
        }
        return kt;
    }

    public void DialogDelete(int id, String ten){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa "+ten+" hay không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(KtraTaiXe(ten)){
                    Toast.makeText(TaiXeActivity.this,"Không thể xóa tài xế vì đã được phân công !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("DELETE FROM TaiXe WHERE MaTX='"+id+"'");
                    GetData();
                    Toast.makeText(TaiXeActivity.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}