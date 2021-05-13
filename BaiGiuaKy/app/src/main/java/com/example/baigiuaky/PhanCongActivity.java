package com.example.baigiuaky;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baigiuaky.Adapter.PhanCongAdapter;
import com.example.baigiuaky.Adapter.XeItemAdapter;
import com.example.baigiuaky.Models.PhanCong;
import com.example.baigiuaky.Models.TaiXe;
import com.example.baigiuaky.Models.TuyenXe;
import com.example.baigiuaky.Models.Xe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class PhanCongActivity extends AppCompatActivity {
    ConnectDB connectDB;
    ListView listView;
    ArrayList<PhanCong> arrayList;
    PhanCongAdapter adapter;
    Button btnThemPC, btnThoatPC;

    XeItemAdapter mAdapter;
    ArrayList<XeItem> mArraylist  = new ArrayList<>();
    boolean isThem = false;
    String tenxe;

    ArrayList<String> dsTuyen = new ArrayList<>();
    ArrayList<String> dsXe = new ArrayList<>();
    ArrayList<String> dsTaiXe = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phan_cong);

        //Tạo Database
        connectDB = new ConnectDB(this,"QuanLyVanTai.db",null,1);

        //Tạo bảng Phân Công
        connectDB.QueryData("CREATE TABLE IF NOT EXISTS PhanCong(MaPC INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MaTuyen INTEGER, MaXe INTEGER, MaTaiXe INTEGER, NgayBD DATE, NgayKT DATE, " +
                "FOREIGN KEY(MaTuyen) REFERENCES TuyenXe(MaTuyen)," +
                "FOREIGN KEY(MaXe) REFERENCES Xe(MaXe)," +
                "FOREIGN KEY(MaTaiXe) REFERENCES TaiXe(MaTX))");
        //ánh xạ
        btnThemPC = findViewById(R.id.btnThemPhanCong);
        btnThoatPC=findViewById(R.id.btnThoatPhanCong);
        listView = findViewById(R.id.lvDSPhanCong);
        arrayList = new ArrayList<>();
        adapter = new PhanCongAdapter(this,R.layout.item_phan_cong,arrayList);
        listView.setAdapter(adapter);

        //Bắt sự kiện
        btnThemPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hàm thêm phân công
                //isThem = true;
                DialogInsert();
            }
        });

        btnThoatPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Trở về trang main
                Intent i = new Intent(PhanCongActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        //Lấy dữ liệu
        try {
            GetData();
        } catch (ParseException e) {

        }
    }

    public void DialogDelete(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa phân công có mã "+id+" hay không ?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                connectDB.QueryData("DELETE FROM PhanCong WHERE MaPC='"+id+"'");
                try {
                    GetData();
                } catch (ParseException e) {
                    e.printStackTrace();
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

    public boolean DinhDangTG(String x){
        boolean kt = false;
        Pattern pattern = Pattern.compile("^\\d{2}[/]\\d{2}[/]\\d{4}$");
        if(pattern.matcher(x).matches()){
            kt = true;
        }
        return kt;
    }

    //mã xe ở đây hiểu là tên xe, mã tài xế hiểu là tên tài xế, mã tuyến hiểu là tên tuyến
    public void DialogUpdate(int mapc, String maxe, String matuyen, String matx, String bd, String kt){
        Dialog dialog = new Dialog(PhanCongActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_phan_cong);

        TextView tvTenChuyen, tvTenXe, tvTenTaiXe, tvTieuDe;
        Spinner spnTuyen, spnXe,spnTaiXe;
        EditText edtBD, edtKT;
        Button btnLuu, btnHuy;

        edtBD = dialog.findViewById(R.id.edtBD);
        edtKT = dialog.findViewById(R.id.edtKT);

        tvTenChuyen = dialog.findViewById(R.id.tvTenChuyen);
        tvTenXe = dialog.findViewById(R.id.tvTenXe);
        tvTenTaiXe = dialog.findViewById(R.id.tvTaiXe);
        tvTieuDe = dialog.findViewById(R.id.tvTieuDe);

        spnTuyen = dialog.findViewById(R.id.spnChuyen);
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,getDsTuyen());
        spnTuyen.setAdapter(a1);

        //Lệnh truyền vào Custom Spiner Xe
        spnXe = dialog.findViewById(R.id.spnXe);
        mAdapter = new XeItemAdapter(this,getItemDSXe1(getMaXe(maxe)));
        spnXe.setAdapter(mAdapter);

        spnTaiXe = dialog.findViewById(R.id.spnTaiXe);
        ArrayAdapter<String> a3 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,getDSTaiXe());
        spnTaiXe.setAdapter(a3);

        tvTieuDe.setText("CẬP NHẬT THÔNG TIN");
        spnTuyen.setSelection(getIndex(spnTuyen,matuyen));
        spnTaiXe.setSelection(getIndex(spnTaiXe,matx));
        edtBD.setText(bd);
        edtKT.setText(kt);

        spnTaiXe.setEnabled(false);
        spnTuyen.setEnabled(false);
        spnXe.setEnabled(false);

        spnXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                XeItem clickedItem = (XeItem) parent.getItemAtPosition(position);
                tenxe = clickedItem.getTenXe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenchuyen = spnTuyen.getSelectedItem().toString();
                String tentx = spnTaiXe.getSelectedItem().toString();

                String[] a = tenchuyen.split(" -> ");
                String diembd = a[0];
                String diemkt = a[1];

                String bd1 = edtBD.getText().toString();
                String kt1 = edtKT.getText().toString();

                String[] a1 = TachThoiGian(bd1);
                String[] a2 = TachThoiGian(kt1);

                if(bd1.isEmpty()){
                    edtBD.setError("Không được để trống !");
                    edtBD.requestFocus();
                    return;
                }
                else if(kt1.isEmpty()){
                    edtKT.setError("Không được để trống !");
                    edtKT.requestFocus();
                    return;
                }
                else if(!DinhDangTG(bd1)){
                    edtBD.setError("Không đúng định dạng dd/MM/yyyy !");
                    edtBD.requestFocus();
                    return;
                }
                else if(!DinhDangTG(kt1)){
                    edtKT.setError("Không đúng định dạng dd/MM/yyyy !");
                    edtKT.requestFocus();
                    return;
                }
                else if(a2[2].compareToIgnoreCase(a1[2])<0){
                    Toast.makeText(PhanCongActivity.this,"Thời gian kết thúc không được bé hơn thời gian bắt đầu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(a2[2].compareToIgnoreCase(a1[2])==0&&a2[1].compareToIgnoreCase(a1[1])<0){
                    Toast.makeText(PhanCongActivity.this,"Thời gian kết thúc không được bé hơn thời gian bắt đầu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(a2[2].compareToIgnoreCase(a1[2])==0&&a2[1].compareToIgnoreCase(a1[1])==0&&a2[0].compareToIgnoreCase(a1[0])<=0){
                    Toast.makeText(PhanCongActivity.this,"Thời gian kết thúc không được bé hơn hoặc bằng thời gian bắt đầu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tentx.equalsIgnoreCase(matx) && TaiXeDaLaiXe(getMaTX(tentx),getMaXe(tenxe),bd1)){
                    Toast.makeText(PhanCongActivity.this,"Đã lên lịch phân công !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tentx.equalsIgnoreCase(matx) && TaiXeLaiKhacXe(getMaTX(tentx),bd1)){
                    Toast.makeText(PhanCongActivity.this,"Đã lên lịch phân công !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(tenxe.equalsIgnoreCase(maxe) && XeDaDcLai(getMaXe(tenxe),bd1)){
                    Toast.makeText(PhanCongActivity.this,"Đã lên lịch phân công !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    connectDB.QueryData("UPDATE PhanCong SET MaTuyen="+getMaTuyen(diembd,diemkt)+",MaXe="+getMaXe(tenxe)+",MaTaiXe="+getMaTX(tentx)+"," +
                            "NgayBD='"+bd1+"',NgayKT='"+kt1+"' WHERE MaPC='"+mapc+"'");
                    try {
                        GetData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(PhanCongActivity.this,"Sửa thành công !",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }
        });


        dialog.show();
    }

    public ArrayList<XeItem> getItemDSXe1(int id){
        mArraylist.clear();
        Cursor data = connectDB.GetData("SELECT * FROM Xe WHERE MaXe='"+id+"'");
        while(data.moveToNext()){
            String tenxe = data.getString(1);
            byte[] anhxe = data.getBlob(3);
            mArraylist.add(new XeItem(tenxe,anhxe));
        }
        return mArraylist;
    }

    public int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
    public void DialogInsert(){
        Dialog dialog = new Dialog(PhanCongActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_phan_cong);

        TextView tvTenChuyen, tvTenXe, tvTenTaiXe;
        Spinner spnTuyen, spnXe,spnTaiXe;
        EditText edtBD, edtKT;
        Button btnLuu, btnHuy;

        edtBD = dialog.findViewById(R.id.edtBD);
        edtKT = dialog.findViewById(R.id.edtKT);

        tvTenChuyen = dialog.findViewById(R.id.tvTenChuyen);
        tvTenXe = dialog.findViewById(R.id.tvTenXe);
        tvTenTaiXe = dialog.findViewById(R.id.tvTaiXe);

        //Khai báo mảng để gắn vô spiner
        ArrayList<String> b = new ArrayList<>();
        ArrayList<String> b1 = new ArrayList<>();
        ArrayList<String> b2 = new ArrayList<>();

        b.clear();
        b1.clear();
        b2.clear();

        b = getDsTuyen();
        b1 = getDSXe();
        b2 = getDSTaiXe();

        spnTuyen = dialog.findViewById(R.id.spnChuyen);
        ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,b);
        spnTuyen.setAdapter(a1);

        spnXe = dialog.findViewById(R.id.spnXe);
        mAdapter = new XeItemAdapter(this,getItemDSXe());
        spnXe.setAdapter(mAdapter);


        spnXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                XeItem clickedItem = (XeItem) parent.getItemAtPosition(position);
                tenxe = clickedItem.getTenXe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnTaiXe = dialog.findViewById(R.id.spnTaiXe);
        ArrayAdapter<String> a3 = new ArrayAdapter<String>(this,R.layout.spiner_layout,R.id.txt,b2);
        spnTaiXe.setAdapter(a3);

        btnLuu = dialog.findViewById(R.id.btnLuu);
        btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenchuyen = spnTuyen.getSelectedItem().toString();
                //String tenxe = spnXe.getSelectedItem().toString();
                String tentx = spnTaiXe.getSelectedItem().toString();

                String[] a = tenchuyen.split(" -> ");
                String diembd = a[0];
                String diemkt = a[1];

                String bd = edtBD.getText().toString();
                String kt = edtKT.getText().toString();

                String[] a1 = TachThoiGian(bd);
                String[] a2 = TachThoiGian(kt);
                if(bd.isEmpty()){
                    edtBD.setError("Không được để trống !");
                    edtBD.requestFocus();
                    return;
                }
                else if(kt.isEmpty()){
                    edtKT.setError("Không được để trống !");
                }
                else if(!DinhDangTG(bd)){
                    edtBD.setError("Không đúng định dạng dd/MM/yyyy !");
                    edtBD.requestFocus();
                    return;
                }
                else if(!DinhDangTG(kt)){
                    edtKT.setError("Không đúng định dạng dd/MM/yyyy !");
                    edtKT.requestFocus();
                    return;
                }
                else if(a2[2].compareToIgnoreCase(a1[2])<0){
                    Toast.makeText(PhanCongActivity.this,"Thời gian kết thúc không được bé hơn thời gian bắt đầu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(a2[2].compareToIgnoreCase(a1[2])==0&&a2[1].compareToIgnoreCase(a1[1])<0){
                    Toast.makeText(PhanCongActivity.this,"Thời gian kết thúc không được bé hơn thời gian bắt đầu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(a2[2].compareToIgnoreCase(a1[2])==0&&a2[1].compareToIgnoreCase(a1[1])==0&&a2[0].compareToIgnoreCase(a1[0])<=0){
                    Toast.makeText(PhanCongActivity.this,"Thời gian kết thúc không được bé hơn hoặc bằng thời gian bắt đầu !",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TaiXeDaLaiXe(getMaTX(tentx),getMaXe(tenxe),bd)){
                    Toast.makeText(PhanCongActivity.this,"Đã lên lịch phân công !",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TaiXeLaiKhacXe(getMaTX(tentx),bd)){
                    Toast.makeText(PhanCongActivity.this,"Đã lên lịch phân công !",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(XeDaDcLai(getMaXe(tenxe),bd)){
                    Toast.makeText(PhanCongActivity.this,"Xe đã lên lịch phân công !",Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    connectDB.QueryData("INSERT INTO PhanCong VALUES(null,"+getMaTuyen(diembd,diemkt)+","+getMaXe(tenxe)+","+getMaTX(tentx)+",'"+bd+"','"+kt+"')");
                    try {
                        GetData();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(PhanCongActivity.this,"Thêm thành công !",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            }
        });

        dialog.show();
    }


    public int getMaTuyen(String bd, String kt){
        int x = 0;
        Cursor data = connectDB.GetData("SELECT * FROM TuyenXe WHERE DiemBD='"+bd+"'AND DiemKT='"+kt+"'");
        while (data.moveToNext()){
            x = data.getInt(0);
        }
        return x;
    }

    public boolean KTThoiGian(String ngaybd, String thangbd, String nambd, String ngaykt, String thangkt, String namkt){
        boolean kt = false;
        if(namkt.compareToIgnoreCase(nambd)<0){
            kt = true;
        }
        else if(namkt.compareToIgnoreCase(nambd)==0){
            if(thangkt.compareToIgnoreCase(thangbd)<0){
                kt = true;
            }
            else if(thangkt.compareToIgnoreCase(thangbd)==0){
                if(ngaykt.compareToIgnoreCase(ngaybd)<0){
                    kt = false;
                }
            }
        }
        return kt;
    }

    public int getMaXe(String ten){
        int x = 0;
        Cursor data = connectDB.GetData("SELECT * FROM Xe WHERE TenXe='"+ten+"'");
        while (data.moveToNext()){
            x = data.getInt(0);
        }
        return x;
    }

    public int getMaTX(String ten){
        int x = 0;
        Cursor data = connectDB.GetData("SELECT * FROM TaiXe WHERE TenTX='"+ten+"'");
        while (data.moveToNext()){
            x = data.getInt(0);
        }
        return x;
    }
    public void GetData() throws ParseException {
        Cursor data = connectDB.GetData("SELECT * FROM PhanCong");
        arrayList.clear();
        while(data.moveToNext()){
            int id = data.getInt(0);
            int matuyen = data.getInt(1);
            int maxe = data.getInt(2);
            int matx = data.getInt(3);
            String bd = data.getString(4);
            String kt = data.getString(5);

            Date ngaybd = new SimpleDateFormat("dd/MM/yyyy").parse(bd);
            Date ngaykt = new SimpleDateFormat("dd/MM/yyyy").parse(kt);

            PhanCong pc = new PhanCong(id,LayXeTuMa(maxe),LayTaiXeTuMa(matx),LayChuyenTuMa(matuyen),ngaybd,ngaykt);
            arrayList.add(pc);
        }
        adapter.notifyDataSetChanged();
    }

    public Xe LayXeTuMa(int id){
        Cursor data = connectDB.GetData("SELECT * FROM Xe WHERE MaXe='"+id+"'");
        String ten=null, namsx = null;
        while(data.moveToNext()){
            ten = data.getString(1);
            namsx = data.getString(2);
        }
        Xe xe = new Xe(id,ten,namsx);
        return xe;
    }
    public TaiXe LayTaiXeTuMa(int id){
        Cursor data = connectDB.GetData("SELECT * FROM TaiXe WHERE MaTX='"+id+"'");
        String ten=null, ns = null, dc=null;
        while(data.moveToNext()){
            ten = data.getString(1);
            ns = data.getString(2);
            dc = data.getString(3);
        }
        TaiXe taiXe = new TaiXe(id,ten,ns,dc);
        return taiXe;
    }

    public TuyenXe LayChuyenTuMa(int id){
        Cursor data = connectDB.GetData("SELECT * FROM TuyenXe WHERE MaTuyen='"+id+"'");
        String diembd=null, diemkt=null;
        float gia = 0;
        while(data.moveToNext()){
            diembd = data.getString(1);
            diemkt = data.getString(2);
            gia = data.getFloat(3);
        }
        TuyenXe tuyenXe = new TuyenXe(id,diembd,diemkt,gia);
        return tuyenXe;
    }

    public ArrayList<String> getDsTuyen(){
        dsTuyen.clear();
        Cursor data = connectDB.GetData("SELECT * FROM TuyenXe");
        while(data.moveToNext()){
            String ten = data.getString(1)+" -> "+data.getString(2);
            dsTuyen.add(ten);
        }
        return dsTuyen;
    }

    public ArrayList<XeItem> getItemDSXe(){
        mArraylist.clear();
        Cursor data = connectDB.GetData("SELECT * FROM Xe");
        while(data.moveToNext()){
            String tenxe = data.getString(1);
            byte[] anhxe = data.getBlob(3);
            mArraylist.add(new XeItem(tenxe,anhxe));
        }
        return mArraylist;
    }
    public ArrayList<String> getDSXe(){
        dsXe.clear();
        Cursor data = connectDB.GetData("SELECT * FROM Xe");
        while(data.moveToNext()){
            String ten = data.getString(1);
            dsXe.add(ten);
        }
        return dsXe;
    }

    public ArrayList<String> getDSTaiXe(){
        dsTaiXe.clear();
        Cursor data = connectDB.GetData("SELECT * FROM TaiXe");
        while(data.moveToNext()){
            String ten = data.getString(1);
            dsTaiXe.add(ten);
        }
        return dsTaiXe;
    }

    public boolean TaiXeDaLaiXe(int mataixe, int maxe, String bd){
        boolean ktra = false;
        Cursor d = connectDB.GetData("SELECT * FROM PhanCong WHERE MaTaiXe='"+mataixe+"' AND MaXe='"+maxe+"' AND (NgayBD<='"+bd+"' AND '"+bd+"'<=NgayKT)");
        while(d.moveToNext()){
            ktra=true;
        }
        return ktra;
    }

    public  String[] TachThoiGian(String x){
        String[] a = null;
        a = x.split("/");
        return a;
    }

    public boolean TaiXeLaiKhacXe(int mataixe, String bd){
        boolean ktra = false;
        Cursor d = connectDB.GetData("SELECT * FROM PhanCong WHERE MaTaiXe='"+mataixe+"' AND (NgayBD<='"+bd+"' AND '"+bd+"'<=NgayKT)");
        while(d.moveToNext()){
            ktra=true;
        }
        return ktra;
    }

    public boolean XeDaDcLai(int maxe, String bd){
        boolean ktra = false;
        Cursor d = connectDB.GetData("SELECT * FROM PhanCong WHERE MaXe='"+maxe+"' AND (NgayBD<='"+bd+"' AND '"+bd+"'<=NgayKT)");
        while(d.moveToNext()){
            ktra=true;
        }
        return ktra;
    }
}