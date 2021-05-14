package com.example.baigiuaky;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiuaky.Adapter.XeAdapter;
import com.example.baigiuaky.Models.TaiXe;
import com.example.baigiuaky.Models.Xe;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class XeActivity extends AppCompatActivity {

    ConnectDB connectDB;
    ListView listView;
    ArrayList<Xe> arrayList;
    XeAdapter adapter;
    Button btnThemXe, btnThoatXe;
    ImageView imgAnhXe;

    public static int REQUEST_CODE_FOLDER = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xe);


        //Tạo Database
        connectDB = new ConnectDB(this,"QuanLyVanTai.db",null,1);

        //Tạo bảng Xe
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS Xe(MaXe INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", TenXe VARCHAR(200), NamSanXuat VARCHAR(50), AnhXe BLOB)");

        //ánh xạ
        listView = findViewById(R.id.lvDSXe);
        btnThemXe = findViewById(R.id.btnThemXe);
        btnThoatXe = findViewById(R.id.btnThoatXe);
        arrayList = new ArrayList<>();
        adapter = new XeAdapter(this,R.layout.item_xe,arrayList);
        listView.setAdapter(adapter);

        //Bắt sự kiện
        btnThoatXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(XeActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        btnThemXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hàm thêm xe
                DialogInsert();
            }
        });

        //Truy Vấn dữ liệu
        GetData();
    }

    public int getMaXe(String ten){
        int x = 0;
        Cursor data = connectDB.GetData("SELECT * FROM Xe WHERE TenXe='"+ten+"'");
        while (data.moveToNext()){
            x = data.getInt(0);
        }
        return x;
    }

    public boolean ktraXe(String ten){
        boolean kt = false;
        Cursor data = connectDB.GetData("SELECT * FROM PhanCong WHERE MaXe='"+getMaXe(ten)+"'");
        while (data.moveToNext()){
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
                if(ktraXe(ten)){
                    Toast.makeText(XeActivity.this,"Không thể xóa xe vì đã có phân công !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("DELETE FROM Xe WHERE MaXe='"+id+"'");
                    GetData();
                    Toast.makeText(XeActivity.this,"Xóa thành công!",Toast.LENGTH_SHORT).show();
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

    public void DialogUpdate(int id, String ten, String namsanxuat, byte[] AnhXe){
        Dialog dialog = new Dialog(XeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_xe);

        TextView tvTieuDe = dialog.findViewById(R.id.tvTieuDe);
        EditText edtTenXe, edtNamSanXuat;
        edtTenXe = dialog.findViewById(R.id.edtTenXe);
        edtNamSanXuat = dialog.findViewById(R.id.edtNamSanXuat);

        Button btnAnhXe=dialog.findViewById(R.id.btnAnhXe);
        imgAnhXe = dialog.findViewById(R.id.imgAnhXe);
        Button btnLuu, btnHuy;
        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        tvTieuDe.setText("CẬP NHẬT THÔNG TIN");
        edtTenXe.setText(ten);
        edtNamSanXuat.setText(namsanxuat);
        Bitmap bitmap = BitmapFactory.decodeByteArray(AnhXe,0,AnhXe.length);
        imgAnhXe.setImageBitmap(bitmap);

        btnAnhXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_FOLDER);
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenxe = edtTenXe.getText().toString().trim();
                String namsanxuat = edtNamSanXuat.getText().toString().trim();
                if(tenxe.isEmpty()){
                    edtTenXe.setError("Tên xe không được trống !");
                    edtTenXe.requestFocus();
                    return;
                }
                else if(!tenxe.equals(ten) && KtraTenXe(tenxe)){
                            edtTenXe.setError("Tên xe không được trùng !");
                            edtTenXe.requestFocus();
                            return;
                }
                else if(namsanxuat.isEmpty()){
                    edtNamSanXuat.setError("Năm sản xuất không được trống !");
                    edtNamSanXuat.requestFocus();
                    return;
                }
                else if(namsanxuat.length()!=4){
                    edtNamSanXuat.setError("Năm sản xuất phải đủ 4 kí tự số!");
                    edtNamSanXuat.requestFocus();
                    return;
                }
                else if(imgAnhXe.getDrawable() == null){
                    Toast.makeText(XeActivity.this,"Vui lòng chọn ảnh xe!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgAnhXe.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
                    byte[] AnhXe1 = byteArray.toByteArray();

                   // connectDB.QueryData("UPDATE Xe SET TenXe = '"+tenxe+"', NamSanXuat='"+namsanxuat+"', AnhXe="+AnhXe1+" WHERE MaXe='"+id+"'");
                    connectDB.SuaXe(id,tenxe,namsanxuat,AnhXe1);
                    Toast.makeText(XeActivity.this,"Sửa thành công !",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetData();
                }
            }
        });
        dialog.show();
    }
    public void DialogInsert(){
        Dialog dialog = new Dialog(XeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_xe);

        //ánh xạ trong dialog
        EditText edtTenXe, edtNamSanXuat;
        Button btnAnhXe, btnLuu, btnHuy;

        edtTenXe = dialog.findViewById(R.id.edtTenXe);
        edtNamSanXuat=dialog.findViewById(R.id.edtNamSanXuat);
        imgAnhXe = dialog.findViewById(R.id.imgAnhXe);
        btnAnhXe = dialog.findViewById(R.id.btnAnhXe);
        btnLuu= dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnAnhXe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_FOLDER);
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenxe = edtTenXe.getText().toString().trim();
                String namsanxuat = edtNamSanXuat.getText().toString().trim();
                if(tenxe.isEmpty()){
                    edtTenXe.setError("Tên xe không được trống !");
                    edtTenXe.requestFocus();
                    return;
                }
                else if(KtraTenXe(tenxe)){
                    edtTenXe.setError("Tên xe không được trùng !");
                    edtTenXe.requestFocus();
                    return;
                }
                else if(namsanxuat.isEmpty()){
                    edtNamSanXuat.setError("Năm sản xuất không được trống !");
                    edtNamSanXuat.requestFocus();
                    return;
                }
                else if(namsanxuat.length()!=4){
                    edtNamSanXuat.setError("Năm sản xuất phải đủ 4 kí tự số!");
                    edtNamSanXuat.requestFocus();
                    return;
                }
                else if(imgAnhXe.getDrawable() == null){
                    Toast.makeText(XeActivity.this,"Vui lòng chọn ảnh xe!",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    //chuyen data imageview thanh byte[]
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imgAnhXe.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArray);
                    byte[] AnhXe = byteArray.toByteArray();

                    connectDB.ThemXe(tenxe,
                            namsanxuat,
                            AnhXe);
                  // connectDB.QueryData("INSERT INTO Xe VALUES(null, '"+tenxe+"', '"+namsanxuat+"', "+AnhXe+")");
                    //GetData();
                    Toast.makeText(XeActivity.this,"Thêm thành công!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetData();
                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data !=null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgAnhXe.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void GetData(){
        Cursor data = connectDB.GetData("SELECT * FROM Xe");
        arrayList.clear();
        while(data.moveToNext()){
            arrayList.add(new Xe(
                    data.getInt(0),
                    data.getString(1),
                    data.getString(2),
                    data.getBlob(3)
            ));
        }
        adapter.notifyDataSetChanged();
    }

    public boolean KtraTenXe(String tenxe){
        boolean kt=false;
        Cursor datta = connectDB.GetData("SELECT * FROM Xe WHERE TenXe ='"+tenxe+"'");
        while(datta.moveToNext()){
            kt = true;
        }
        return kt;
    }
}