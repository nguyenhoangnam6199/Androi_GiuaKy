package com.example.baigiuaky;

public class XeItem {
    private String TenXe;
    private byte[] AnhXe;

    public XeItem(String tenXe, byte[] anhXe) {
        TenXe = tenXe;
        AnhXe = anhXe;
    }

    public String getTenXe() {
        return TenXe;
    }

    public void setTenXe(String tenXe) {
        TenXe = tenXe;
    }

    public byte[] getAnhXe() {
        return AnhXe;
    }

    public void setAnhXe(byte[] anhXe) {
        AnhXe = anhXe;
    }
}
