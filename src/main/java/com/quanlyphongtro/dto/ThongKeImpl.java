package com.quanlyphongtro.dto;

import java.math.BigDecimal;

public class ThongKeImpl implements ThongKeDto {
    private String nhan;
    private BigDecimal giaTri;
    private BigDecimal giaTri2;

    public ThongKeImpl(String nhan, BigDecimal giaTri, BigDecimal giaTri2) {
        this.nhan = nhan;
        this.giaTri = giaTri;
        this.giaTri2 = giaTri2;
    }

    @Override public String getNhan() { return nhan; }
    @Override public BigDecimal getGiaTri() { return giaTri; }
    @Override public BigDecimal getGiaTri2() { return giaTri2; }
}