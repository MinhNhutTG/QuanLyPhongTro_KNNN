package com.quanlyphongtro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDonDto {
    private String idHoaDon;
    private DichVuPhongDto dichVuPhong; // Include full DTO or maybe just Summary? Full DTO useful for details.
    
    // Flattened or just carry over fields
    private Integer soDien;
    private Integer soNuoc;
    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal phiKhac;
    private BigDecimal tongTien;
    private BigDecimal giaPhong;
    private LocalDateTime ngayLapHoaDon;
    private String trangThai;
    private String ghiChu;

    public HoaDonDto() {}

    public HoaDonDto(String idHoaDon, DichVuPhongDto dichVuPhong, Integer soDien, Integer soNuoc, BigDecimal tienDien, BigDecimal tienNuoc, BigDecimal phiKhac, BigDecimal tongTien, BigDecimal giaPhong, LocalDateTime ngayLapHoaDon, String trangThai, String ghiChu) {
        this.idHoaDon = idHoaDon;
        this.dichVuPhong = dichVuPhong;
        this.soDien = soDien;
        this.soNuoc = soNuoc;
        this.tienDien = tienDien;
        this.tienNuoc = tienNuoc;
        this.phiKhac = phiKhac;
        this.tongTien = tongTien;
        this.giaPhong = giaPhong;
        this.ngayLapHoaDon = ngayLapHoaDon;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public String getIdHoaDon() { return idHoaDon; }
    public void setIdHoaDon(String idHoaDon) { this.idHoaDon = idHoaDon; }

    public DichVuPhongDto getDichVuPhong() { return dichVuPhong; }
    public void setDichVuPhong(DichVuPhongDto dichVuPhong) { this.dichVuPhong = dichVuPhong; }

    public Integer getSoDien() { return soDien; }
    public void setSoDien(Integer soDien) { this.soDien = soDien; }

    public Integer getSoNuoc() { return soNuoc; }
    public void setSoNuoc(Integer soNuoc) { this.soNuoc = soNuoc; }

    public BigDecimal getTienDien() { return tienDien; }
    public void setTienDien(BigDecimal tienDien) { this.tienDien = tienDien; }

    public BigDecimal getTienNuoc() { return tienNuoc; }
    public void setTienNuoc(BigDecimal tienNuoc) { this.tienNuoc = tienNuoc; }

    public BigDecimal getPhiKhac() { return phiKhac; }
    public void setPhiKhac(BigDecimal phiKhac) { this.phiKhac = phiKhac; }

    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }

    public BigDecimal getGiaPhong() { return giaPhong; }
    public void setGiaPhong(BigDecimal giaPhong) { this.giaPhong = giaPhong; }

    public LocalDateTime getNgayLapHoaDon() { return ngayLapHoaDon; }
    public void setNgayLapHoaDon(LocalDateTime ngayLapHoaDon) { this.ngayLapHoaDon = ngayLapHoaDon; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
}
