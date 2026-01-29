package com.quanlyphongtro.dto;

import java.math.BigDecimal;

public class PhongDto {
    private String soPhong;
    private String maLoai; // ID for logic
    private String tenLoai; // Name for display
    private BigDecimal giaTien;
    private String trangThai;
    private String ghiChu;

    public PhongDto() {}

    public PhongDto(String soPhong, String maLoai, String tenLoai, BigDecimal giaTien, String trangThai, String ghiChu) {
        this.soPhong = soPhong;
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.giaTien = giaTien;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    public BigDecimal getGiaTien() { return giaTien; }
    public void setGiaTien(BigDecimal giaTien) { this.giaTien = giaTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
