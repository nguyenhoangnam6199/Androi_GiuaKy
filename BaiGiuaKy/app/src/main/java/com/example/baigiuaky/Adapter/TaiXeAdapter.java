package com.example.baigiuaky.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baigiuaky.Models.TaiXe;
import com.example.baigiuaky.R;
import com.example.baigiuaky.TaiXeActivity;

import java.util.List;

public class TaiXeAdapter extends BaseAdapter {
    private TaiXeActivity context;
    private int layout;
    private List<TaiXe> taiXeList;

    public TaiXeAdapter(TaiXeActivity context, int layout, List<TaiXe> taiXeList) {
        this.context = context;
        this.layout = layout;
        this.taiXeList = taiXeList;
    }

    @Override
    public int getCount() {
        return taiXeList.size();
        //return 0;
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
        TextView tvTen, tvNgaySinh, tvDiaChi;
        ImageView imgSua, imgXoa;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvTen = convertView.findViewById(R.id.tvTen);
            holder.tvNgaySinh = convertView.findViewById(R.id.tvNgaySinh);
            holder.tvDiaChi = convertView.findViewById(R.id.tvDiaChi);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        TaiXe taiXe = taiXeList.get(position);
        holder.tvTen.setText("Tài Xế: "+taiXe.getTenTX().toString().trim());
        holder.tvNgaySinh.setText("Ngày Sinh: "+taiXe.getNgaySinh().toString().trim());
        holder.tvDiaChi.setText("Địa chỉ: "+taiXe.getDiaChi().toString().trim());

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chèn code sửa
                context.DialogUpdate(taiXe.getMaTX(),taiXe.getTenTX(),taiXe.getNgaySinh(),taiXe.getDiaChi());
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Chèn code xóa
                context.DialogDelete(taiXe.getMaTX(),taiXe.getTenTX());
            }
        });


        //Gán animation
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.scale_list);
        convertView.startAnimation(animation);
        return convertView;
    }
}
