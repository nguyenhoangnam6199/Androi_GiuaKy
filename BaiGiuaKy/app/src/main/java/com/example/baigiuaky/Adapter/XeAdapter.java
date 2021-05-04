package com.example.baigiuaky.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baigiuaky.Models.TaiXe;
import com.example.baigiuaky.Models.Xe;
import com.example.baigiuaky.R;
import com.example.baigiuaky.XeActivity;

import java.util.List;

public class XeAdapter extends BaseAdapter {
    private XeActivity context;
    private int layout;
    private List<Xe> xeList;

    public XeAdapter(XeActivity context, int layout, List<Xe> xeList) {
        this.context = context;
        this.layout = layout;
        this.xeList = xeList;
    }

    @Override
    public int getCount() {
        return xeList.size();
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
        TextView tvTenXe, tvNamSanXuat;
        ImageView imgSua, imgXoa,imgAnhXe;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvTenXe = convertView.findViewById(R.id.tvTenXe);
            holder.tvNamSanXuat = convertView.findViewById(R.id.tvNamSanXuat);
            holder.imgAnhXe = convertView.findViewById(R.id.imgAnhXe);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        Xe xe = xeList.get(position);
        holder.tvTenXe.setText("Tên Xe: "+xe.getTenXe().toString().trim());
        holder.tvNamSanXuat.setText("Năm sản xuất: "+xe.getNamSanXuat().toString().trim());

        //chuyen byte thanh bitmap
        byte[] AnhXe = xe.getAnhXe();
        Bitmap bitmap = BitmapFactory.decodeByteArray(AnhXe,0,AnhXe.length);
        holder.imgAnhXe.setImageBitmap(bitmap);

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chèn code sửa
                context.DialogUpdate(xe.getMaXe(),xe.getTenXe(),xe.getNamSanXuat(),xe.getAnhXe());
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chèn code xóa
                context.DialogDelete(xe.getMaXe(),xe.getTenXe());
            }
        });

        return convertView;
    }
}
