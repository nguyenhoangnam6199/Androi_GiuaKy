package com.example.baigiuaky.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.baigiuaky.R;
import com.example.baigiuaky.XeItem;

import java.util.ArrayList;

public class XeItemAdapter  extends ArrayAdapter {
    public XeItemAdapter(Context context, ArrayList<XeItem> xeList){
        super(context,0,xeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private  View initView(int pos, View converView, ViewGroup parent){
        if(converView==null){
            converView = LayoutInflater.from(getContext()).
                    inflate(R.layout.xe_spinner_row,parent,
                            false);
        }
        ImageView imgAnhXe = converView.findViewById(R.id.spnAnhXe);
        TextView tvTenXe = converView.findViewById(R.id.spnTenXe);

        XeItem xeItem = (XeItem) getItem(pos);
        if(xeItem!=null){
            byte[] AnhXe = xeItem.getAnhXe();
            Bitmap bitmap = BitmapFactory.decodeByteArray(AnhXe,0,AnhXe.length);
            imgAnhXe.setImageBitmap(bitmap);
            tvTenXe.setText(xeItem.getTenXe());
        }

        return converView;
    }

}
