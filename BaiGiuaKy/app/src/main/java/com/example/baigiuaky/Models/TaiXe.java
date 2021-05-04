package com.example.baigiuaky.Models;

public class TaiXe {
    public int MaTX;
    private String TenTX, NgaySinh, DiaChi;

    public TaiXe(int maTX, String tenTX, String ngaySinh, String diaChi) {
        MaTX = maTX;
        TenTX = tenTX;
        NgaySinh = ngaySinh;
        DiaChi = diaChi;
    }

    public int getMaTX() {
        return MaTX;
    }

    public void setMaTX(int maTX) {
        MaTX = maTX;
    }

    public String getTenTX() {
        return TenTX;
    }

    public void setTenTX(String tenTX) {
        TenTX = tenTX;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    @Override
    public String toString() {
        return "TaiXe{" +
                "MaTX=" + MaTX +
                ", TenTX='" + TenTX + '\'' +
                ", NgaySinh='" + NgaySinh + '\'' +
                ", DiaChi='" + DiaChi + '\'' +
                '}';
    }
}
