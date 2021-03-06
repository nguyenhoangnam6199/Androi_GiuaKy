package com.example.baigiuaky.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.baigiuaky.Models.PhanCong;
import com.example.baigiuaky.Models.TaiXe;
import com.example.baigiuaky.PhanCongActivity;
import com.example.baigiuaky.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class PhanCongAdapter extends BaseAdapter {
    private PhanCongActivity context;
    private int layout;
    private List<PhanCong> phanCongList;

    public PhanCongAdapter(PhanCongActivity context, int layout, List<PhanCong> phanCongList) {
        this.context = context;
        this.layout = layout;
        this.phanCongList = phanCongList;
    }

    @Override
    public int getCount() {
        return phanCongList.size();
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
        TextView tvTenChuyen, tvTenXe, tvTenTaiXe, tvNgayBD, tvNgayKT;
        ImageView imgSua, imgXoa;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);

            holder.tvTenChuyen = convertView.findViewById(R.id.tvTenChuyen);
            holder.tvTenXe = convertView.findViewById(R.id.tvTenXe);
            holder.tvTenTaiXe = convertView.findViewById(R.id.tvTenTaiXe);
            holder.tvNgayBD = convertView.findViewById(R.id.tvNgayBD);
            holder.tvNgayKT = convertView.findViewById(R.id.tvNgayKT);
            holder.imgSua = convertView.findViewById(R.id.imgSua);
            holder.imgXoa = convertView.findViewById(R.id.imgXoa);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        PhanCong phanCong = phanCongList.get(position);
        //thi???t l???p thu???c t??nh theo t???ng ??
            holder.tvTenChuyen.setText("Tuy???n: "+phanCong.getMaTuyen().getDiemBD()+" -> "+phanCong.getMaTuyen().getDiemKT());
            holder.tvTenXe.setText("Xe: "+phanCong.getMaXe().getTenXe());
            holder.tvTenTaiXe.setText("T??i X???: "+phanCong.getMaTaiXe().getTenTX());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String bd = formatter.format(phanCong.getNgayBatDau());
            holder.tvNgayBD.setText("B???t ?????u: "+bd);

            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            String kt = formatter.format(phanCong.getNgayKetThuc());
            holder.tvNgayKT.setText("K???t th??c: "+kt);

        holder.imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ch??n code s???a
                context.DialogUpdate(phanCong.getMaPhanCong(),phanCong.getMaXe().getTenXe(),phanCong.getMaTuyen().getDiemBD()+" -> "+phanCong.getMaTuyen().getDiemKT(),phanCong.getMaTaiXe().getTenTX(),bd,kt);
            }
        });

        holder.imgXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ch??n code x??a
                context.DialogDelete(phanCong.getMaPhanCong());
            }
        });

        return convertView;
    }
}
