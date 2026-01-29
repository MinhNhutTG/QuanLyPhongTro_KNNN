package com.quanlyphongtro.dto;

public class ChiTietHopDongDto {
    private Integer maKhach;
    private String tenKhach;
    private String vaiTro; // Chủ hộ / Thành viên

    public ChiTietHopDongDto() {}
    public ChiTietHopDongDto(Integer maKhach, String tenKhach, String vaiTro) {
        this.maKhach = maKhach;
        this.tenKhach = tenKhach;
        this.vaiTro = vaiTro;
    }

    // Getters & Setters
    public Integer getMaKhach() { return maKhach; }
    public void setMaKhach(Integer maKhach) { this.maKhach = maKhach; }
    public String getTenKhach() { return tenKhach; }
    public void setTenKhach(String tenKhach) { this.tenKhach = tenKhach; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    // Override equals để check trùng trong danh sách
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHopDongDto that = (ChiTietHopDongDto) o;
        return maKhach.equals(that.maKhach);
    }
}