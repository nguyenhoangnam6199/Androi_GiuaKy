package com.example.baigiuaky.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.baigiuaky.Models.TaiXe;
import com.example.baigiuaky.Models.TuyenXe;
import com.example.baigiuaky.R;
import com.example.baigiuaky.TuyenXeActivity;

import java.util.List;

public class TuyenXeAdapter extends BaseAdapter {
    private TuyenXeActivity context;
    private int layout;
    private List<TuyenXe> tuyenXeList;

    public TuyenXeAdapter(TuyenXeActivity context, int layout, List<TuyenXe> tuyenXeList) {
        this.context = context;
        this.layout = layout;
        this.tuyenXeList = tuyenXeList;
    }

    @Override
    public int getCount() {
        return tuyenXeList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView tvTenTuyen, tvGia;
        ImageView imgSua, imgXoa;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvTenTuyen = convertView.findViewById(R.id.tvTenTuyenXe);
            holder.tvGia = convertView.findViewById(R.id.tvGia);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        TuyenXe tuyenXe = tuyenXeList.get(position);

        holder.tvTenTuyen.setText("Tuyến: "+tuyenXe.getDiemBD().toString().trim()+" -> "+tuyenXe.getDiemKT().toString().trim());
        holder.tvGia.setText("Giá: "+Float.toString(tuyenXe.getGia()));

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chèn code sửa
                context.DialogUpdate(tuyenXe.getMaTuyen(),tuyenXe.getDiemBD(),tuyenXe.getDiemKT(),tuyenXe.getGia());
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chèn code xóa
                context.DialogDelete(tuyenXe.getMaTuyen(),tuyenXe.getDiemBD(),tuyenXe.getDiemKT());
            }
        });

        return convertView;
    }
}
