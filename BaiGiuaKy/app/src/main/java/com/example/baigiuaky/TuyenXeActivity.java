package com.example.baigiuaky;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiuaky.Adapter.TuyenXeAdapter;
import com.example.baigiuaky.Models.TuyenXe;

import java.util.ArrayList;
import java.util.List;

public class TuyenXeActivity extends AppCompatActivity {
    ConnectDB connectDB;
    Button btnThemTuyen, btnThoatTuyen;
    ListView listView;
    ArrayList<TuyenXe> arrayList;
    TuyenXeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuyen_xe);

        //Tạo Database
        connectDB = new ConnectDB(this,"QuanLyVanTai.db",null,1);

        //Tạo bảng tuyến xe
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS TuyenXe(MaTuyen INTEGER PRIMARY KEY AUTOINCREMENT, DiemBD VARCHAR(200), DiemKT VARCHAR(200), Gia FLOAT)");

        //Tạo dữ liệu mẫu
        //connectDB.QueryData("INSERT INTO TuyenXe VALUES (null,'Sài Gòn','Nha Trang',2000)");

        //Ánh xạ
        btnThemTuyen = findViewById(R.id.btnThemTuyen);
        btnThoatTuyen = findViewById(R.id.btnThoatTuyen);
        listView = findViewById(R.id.lvDSTuyenXe);
        arrayList=new ArrayList<>();
        adapter = new TuyenXeAdapter(this,R.layout.item_tuyen_xe,arrayList);
        listView.setAdapter(adapter);

        //Bắt sự kiện
        btnThemTuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiển thị Dialog để thêm tuyến xe
                DialogInsert();
            }
        });

        btnThoatTuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnThoatTuyen.setBackgroundColor(Color.RED);
                Intent i = new Intent(TuyenXeActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        //Truy xuất dữ liệu
        GetData();
    }
    public int getMaTuyen(String bd, String kt){
        int x = 0;
        Cursor data = connectDB.GetData("SELECT * FROM TuyenXe WHERE DiemBD='"+bd+"'AND DiemKT='"+kt+"'");
        while (data.moveToNext()){
            x = data.getInt(0);
        }
        return x;
    }
    public boolean KtraTuyenXe(String bd,String kt ){
        boolean ktra = false;
        Cursor data = connectDB.GetData("SELECT * FROM PhanCong WHERE MaTuyen='"+getMaTuyen(bd,kt)+"'");
        while (data.moveToNext()){
            ktra = true;
        }
        return ktra;
    }

    public void DialogInsert(){
        Dialog dialog = new Dialog(TuyenXeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_tuyen_xe);

        //Ánh xạ
        Spinner spnDiemBD, spnDiemKT;
        spnDiemBD = dialog.findViewById(R.id.spnDiemBD);
        spnDiemKT = dialog.findViewById(R.id.spnDiemKT);
        EditText edtGia = dialog.findViewById(R.id.edtGia);
        Button btnLuu,btnHuy;
        btnLuu = dialog.findViewById(R.id.btnLuu1);
        btnHuy = dialog.findViewById(R.id.btnHuy1);

        //Bắt sự kiện
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bd = spnDiemBD.getSelectedItem().toString();
                String kt = spnDiemKT.getSelectedItem().toString();
                //float gia = Float.parseFloat(edtGia.getText().toString());
                if(edtGia.getText().toString().isEmpty()){
                    edtGia.setError("Giá không được để trống !");
                    return;
                }
                /*else if(gia<0){
                    edtGia.setError("Giá phải dương !");
                    return;
                }*/
                else if(KtTuyenDaTonTai(bd,kt)){
                    Toast.makeText(TuyenXeActivity.this,"Tên Tuyến đã tồn tại !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(bd.equalsIgnoreCase(kt)){
                   Toast.makeText(TuyenXeActivity.this,"Điểm BD Không được trùng Điểm KT !",Toast.LENGTH_SHORT).show();
//                    ((TextView)spnDiemBD.getSelectedView()).setError("Không được trùng nhau !");
//                    ((TextView)spnDiemKT.getSelectedView()).setError("Không được trùng nhau !");
                    return;
                }
                else{
                    //connectDB.QueryData("INSERT INTO TuyenXe VALUES(null,'"+bd+"','"+kt+"',"+gia+")");
                    connectDB.QueryData("INSERT INTO TuyenXe VALUES(null,'"+bd+"','"+kt+"',"+edtGia.getText().toString()+")");
                    GetData();
                    Toast.makeText(TuyenXeActivity.this,"Thêm thành công !",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void DialogUpdate(int id, String diembd, String diemkt, float gia){
        Dialog dialog = new Dialog(TuyenXeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_tuyen_xe);

        //Ánh xạ
        TextView tvTieuDe = dialog.findViewById(R.id.tvTieuDe);
        Spinner spnDiemBD, spnDiemKT;
        spnDiemBD = dialog.findViewById(R.id.spnDiemBD);
        spnDiemKT = dialog.findViewById(R.id.spnDiemKT);
        EditText edtGia = dialog.findViewById(R.id.edtGia);
        Button btnLuu,btnHuy;
        btnLuu = dialog.findViewById(R.id.btnLuu1);
        btnHuy = dialog.findViewById(R.id.btnHuy1);

        tvTieuDe.setText("CẬP NHẬT THÔNG TIN");
        spnDiemBD.setSelection(getIndex(spnDiemBD,diembd));
        spnDiemKT.setSelection(getIndex(spnDiemKT,diemkt));
        edtGia.setText(Float.toString(gia));
        //Bắt sự kiện
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bdmoi = spnDiemBD.getSelectedItem().toString();
                String ktmoi = spnDiemKT.getSelectedItem().toString();
                float giamoi = Float.parseFloat(edtGia.getText().toString());
                if(edtGia.getText().toString().isEmpty()){
                    edtGia.setError("Giá không được để trống !");
                    return;
                }
                /*else if(gia<0){
                    edtGia.setError("Giá phải dương !");
                    return;
                }*/
                else if(KtTuyenDaTonTai(bdmoi,ktmoi)){
                    Toast.makeText(TuyenXeActivity.this,"Tên Tuyến đã tồn tại !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(bdmoi.equalsIgnoreCase(ktmoi)){
                    Toast.makeText(TuyenXeActivity.this,"Điểm BD Không được trùng Điểm KT !",Toast.LENGTH_SHORT).show();
//                    ((TextView)spnDiemBD.getSelectedView()).setError("Không được trùng nhau !");
//                    ((TextView)spnDiemKT.getSelectedView()).setError("Không được trùng nhau !");
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE TuyenXe SET DiemBD='"+bdmoi+"', DiemKT='"+ktmoi+"' , Gia="+giamoi+" WHERE MaTuyen="+id+"");
                    GetData();
                    Toast.makeText(TuyenXeActivity.this,"Sửa thành công !",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    public void DialogDelete(int id, String diembd, String diemkt){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa "+diembd+" -> "+diemkt+" hay không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(KtraTuyenXe(diembd, diemkt)){
                    Toast.makeText(TuyenXeActivity.this,"Không thể xóa tuyến vì đã có dữ liệu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("DELETE FROM TuyenXe WHERE MaTuyen='"+id+"'");
                    GetData();
                    Toast.makeText(TuyenXeActivity.this,"Xóa thành công !",Toast.LENGTH_SHORT).show();
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

    public boolean KtTuyenDaTonTai(String bd, String kt){
        boolean ktra = false;
        Cursor d = connectDB.GetData("SELECT * FROM TuyenXe WHERE DiemBD='"+bd+"' AND DiemKT='"+kt+"'");
        while (d.moveToNext()){
            ktra = true;
        }
        return ktra;
    }

    public void GetData(){
        Cursor dataTuyenXe = connectDB.GetData("SELECT * FROM TuyenXe");
        arrayList.clear();
        while(dataTuyenXe.moveToNext()){
            int id = dataTuyenXe.getInt(0);
            String diembd = dataTuyenXe.getString(1);
            String diemkt = dataTuyenXe.getString(2);
            float gia = dataTuyenXe.getFloat(3);
            arrayList.add(new TuyenXe(id,diembd,diemkt,gia));
        }
        adapter.notifyDataSetChanged();
    }
}