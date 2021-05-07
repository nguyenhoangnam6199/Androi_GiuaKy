package com.example.baigiuaky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.baigiuaky.Models.ThongKe;
import com.example.baigiuaky.Models.Xe;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ThongKeActivity extends AppCompatActivity {
    ConnectDB connectDB;
    BarChart barChart;
    Button btnThoatChart;
    ArrayList<BarEntry> barEntryArrayList;
    ArrayList<String> labelsName;
    ArrayList<ThongKe> dsXeHD = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);

        barChart = findViewById(R.id.barChart);
        btnThoatChart = findViewById(R.id.btnThoatCHart);

        connectDB = new ConnectDB(this,"QuanLyVanTai.db",null,1);

        LayDuLieu();

        btnThoatChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Trở về trang main
                Intent i = new Intent(ThongKeActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        barEntryArrayList = new ArrayList<>();
        labelsName = new ArrayList<>();

        for(int i=0; i<dsXeHD.size(); i++){
            barEntryArrayList.add(new BarEntry(i,dsXeHD.get(i).getSolan()));
            labelsName.add(dsXeHD.get(i).getTenxe());
        }

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList,"Tần Xuất HD các Xe");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        Description description = new Description();
        description.setText("Số Lần");
        barChart.setDescription(description);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        //Set dinh dang cho labelsName
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsName));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(labelsName.size());
        xAxis.setLabelRotationAngle(270);

        barChart.animateY(2000);
        barChart.invalidate();

    }

    public void LayDuLieu(){
        //String sql = "SELECT DISTINCT MaXe, COUNT(MaXe) FROM PhanCong GROUP BY MaXe";
        Cursor data = connectDB.GetData("SELECT DISTINCT MaXe, COUNT(MaXe) FROM PhanCong GROUP BY MaXe");
        while(data.moveToNext()){
            int maxe = data.getInt(0);
            int solan = data.getInt(1);
            String tenxe = LayTenXeTuMa(maxe);
            dsXeHD.add(new ThongKe(tenxe,solan));
        }
    }

    public String LayTenXeTuMa(int id){
        Cursor data = connectDB.GetData("SELECT TenXe FROM Xe WHERE MaXe='"+id+"'");
        String ten=null;
        while(data.moveToNext()){
            ten = data.getString(0);
        }
        return ten;
    }
}