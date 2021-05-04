package com.example.baigiuaky.Models;

import java.util.Date;

public class PhanCong {
    private int MaPhanCong;
    private Xe MaXe;
    private TaiXe MaTaiXe;
    private TuyenXe MaTuyen;
    private Date NgayBatDau;
    private Date NgayKetThuc;

    public PhanCong(int maPhanCong, Xe maXe, TaiXe maTaiXe, TuyenXe maTuyen, Date ngayBatDau, Date ngayKetThuc) {
        MaPhanCong = maPhanCong;
        MaXe = maXe;
        MaTaiXe = maTaiXe;
        MaTuyen = maTuyen;
        NgayBatDau = ngayBatDau;
        NgayKetThuc = ngayKetThuc;
    }



    public int getMaPhanCong() {
        return MaPhanCong;
    }

    public void setMaPhanCong(int maPhanCong) {
        MaPhanCong = maPhanCong;
    }

    public Xe getMaXe() {
        return MaXe;
    }

    public void setMaXe(Xe maXe) {
        MaXe = maXe;
    }

    public TaiXe getMaTaiXe() {
        return MaTaiXe;
    }

    public void setMaTaiXe(TaiXe maTaiXe) {
        MaTaiXe = maTaiXe;
    }

    public TuyenXe getMaTuyen() {
        return MaTuyen;
    }

    public void setMaTuyen(TuyenXe maTuyen) {
        MaTuyen = maTuyen;
    }

    public Date getNgayBatDau() {
        return NgayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        NgayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return NgayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        NgayKetThuc = ngayKetThuc;
    }

    @Override
    public String toString() {
        return "PhanCong{" +
                "MaPhanCong=" + MaPhanCong +
                ", MaXe=" + MaXe +
                ", MaTaiXe=" + MaTaiXe +
                ", MaTuyen=" + MaTuyen +
                ", NgayBatDau=" + NgayBatDau +
                ", NgayKetThuc=" + NgayKetThuc +
                '}';
    }
}
