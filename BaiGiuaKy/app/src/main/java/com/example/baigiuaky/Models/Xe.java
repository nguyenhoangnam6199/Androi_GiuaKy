package com.example.baigiuaky.Models;

public class Xe {
    public int MaXe;
    public String TenXe, NamSanXuat;
    public byte[] AnhXe;

    public Xe(int maXe, String tenXe, String namSanXuat) {
        MaXe = maXe;
        TenXe = tenXe;
        NamSanXuat = namSanXuat;
    }

    public Xe(int maXe, String tenXe, String namSanXuat, byte[] anhXe) {
        MaXe = maXe;
        TenXe = tenXe;
        NamSanXuat = namSanXuat;
        AnhXe = anhXe;
    }

    public int getMaXe() {
        return MaXe;
    }

    public void setMaXe(int maXe) {
        MaXe = maXe;
    }

    public String getTenXe() {
        return TenXe;
    }

    public void setTenXe(String tenXe) {
        TenXe = tenXe;
    }

    public String getNamSanXuat() {
        return NamSanXuat;
    }

    public void setNamSanXuat(String namSanXuat) {
        NamSanXuat = namSanXuat;
    }

    public byte[] getAnhXe() {
        return AnhXe;
    }

    public void setAnhXe(byte[] anhXe) {
        AnhXe = anhXe;
    }
}
