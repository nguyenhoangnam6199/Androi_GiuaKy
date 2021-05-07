package com.example.baigiuaky.Models;

public class ThongKe {
    private String tenxe;
    private int solan;

    public ThongKe(String tenxe, int solan) {
        this.tenxe = tenxe;
        this.solan = solan;
    }

    public String getTenxe() {
        return tenxe;
    }

    public void setTenxe(String tenxe) {
        this.tenxe = tenxe;
    }

    public int getSolan() {
        return solan;
    }

    public void setSolan(int solan) {
        this.solan = solan;
    }
}
