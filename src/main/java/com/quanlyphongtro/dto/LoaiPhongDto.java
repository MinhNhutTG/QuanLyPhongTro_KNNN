package com.quanlyphongtro.dto;

import java.math.BigDecimal;

public class LoaiPhongDto {
    private String maLoai;
    private String tenLoai;
    private BigDecimal gia;
    private Integer soNguoiToiDa;

    public LoaiPhongDto() {}

    public LoaiPhongDto(String maLoai, String tenLoai, BigDecimal gia,Integer sntd) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.gia = gia;
        this.soNguoiToiDa = sntd;
    }

    // Getters & Setters
    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public BigDecimal getGia() { return gia; }
    public void setGia(BigDecimal gia) { this.gia = gia; }

    public Integer getSoNguoiToiDa() {
        return soNguoiToiDa;
    }

    public void setSoNguoiToiDa(Integer soNguoiToiDa) {
        this.soNguoiToiDa = soNguoiToiDa;
    }

    // Override toString để hiển thị trong JComboBox
    @Override
    public String toString() {
        return tenLoai;
    }
}