package com.example.baigiuaky;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class ConnectDB extends SQLiteOpenHelper {
    public ConnectDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //Truy vấn không trả về kết quả
    public void QueryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    //Ham them xe
    public void ThemXe(String ten, String namsx, byte[] anhxe){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO Xe VALUES(null, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,ten);
        statement.bindString(2,namsx);
        statement.bindBlob(3,anhxe);

        statement.executeInsert();
    }

    public void SuaXe(int ma,String ten, String namsx, byte[] anhxe){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE Xe SET TenXe= ?, NamSanXuat= ?, AnhXe= ? WHERE MaXe=?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,ten);
        statement.bindString(2,namsx);
        statement.bindBlob(3,anhxe);
        statement.bindDouble(4,ma);

        statement.executeUpdateDelete();
    }

    //Truy vấn trả lại kết quả
    public Cursor GetData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
