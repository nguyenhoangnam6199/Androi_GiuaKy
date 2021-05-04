package com.example.baigiuaky.Models;

public class TuyenXe {
    private int maTuyen;
    private String diemBD, diemKT;
    private float gia;

    public TuyenXe(int maTuyen, String diemBD, String diemKT, float gia) {
        this.maTuyen = maTuyen;
        this.diemBD = diemBD;
        this.diemKT = diemKT;
        this.gia = gia;
    }

    public int getMaTuyen() {
        return maTuyen;
    }

    public void setMaTuyen(int maTuyen) {
        this.maTuyen = maTuyen;
    }

    public String getDiemBD() {
        return diemBD;
    }

    public void setDiemBD(String diemBD) {
        this.diemBD = diemBD;
    }

    public String getDiemKT() {
        return diemKT;
    }

    public void setDiemKT(String diemKT) {
        this.diemKT = diemKT;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    @Override
    public String toString() {
        return "TuyenXe{" +
                "maTuyen=" + maTuyen +
                ", diemBD='" + diemBD + '\'' +
                ", diemKT='" + diemKT + '\'' +
                ", gia=" + gia +
                '}';
    }
}
