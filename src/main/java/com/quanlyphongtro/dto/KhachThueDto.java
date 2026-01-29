package com.quanlyphongtro.dto;

import java.time.LocalDateTime;

public class KhachThueDto {
    private Integer maKhach;
    private String hoTen;
    private LocalDateTime ngaySinh;
    private String cccd;
    private String soDienThoai;
    private String queQuan;
    private String trangThai;
    private String email;

    public KhachThueDto() {}

    public KhachThueDto(Integer maKhach, String hoTen, LocalDateTime ngaySinh, String cccd, String soDienThoai, String queQuan, String trangThai, String email) {
        this.maKhach = maKhach;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.cccd = cccd;
        this.soDienThoai = soDienThoai;
        this.queQuan = queQuan;
        this.trangThai = trangThai;
        this.email = email;
    }

    public Integer getMaKhach() { return maKhach; }
    public void setMaKhach(Integer maKhach) { this.maKhach = maKhach; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    
    public LocalDateTime getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDateTime ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getQueQuan() { return queQuan; }
    public void setQueQuan(String queQuan) { this.queQuan = queQuan; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}