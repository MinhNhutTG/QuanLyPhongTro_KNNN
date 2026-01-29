package com.quanlyphongtro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DichVuPhongDto {
    private Integer id;
    private String soPhong;
    private String maHopDong;
    private String ki;
    private Integer soDienCu;
    private Integer soDienMoi;
    private Integer soNuocCu;
    private Integer soNuocMoi;
    private BigDecimal giaDien;
    private BigDecimal giaNuoc;
    private BigDecimal tienMang;
    private LocalDateTime ngayTao;
    private String trangThai;

    public DichVuPhongDto() {
    }

    public DichVuPhongDto(Integer id, String soPhong, String maHopDong, String ki, Integer soDienCu, Integer soDienMoi, Integer soNuocCu, Integer soNuocMoi, BigDecimal giaDien, BigDecimal giaNuoc, BigDecimal tienMang, LocalDateTime ngayTao, String trangThai) {
        this.id = id;
        this.soPhong = soPhong;
        this.maHopDong = maHopDong;
        this.ki = ki;
        this.soDienCu = soDienCu;
        this.soDienMoi = soDienMoi;
        this.soNuocCu = soNuocCu;
        this.soNuocMoi = soNuocMoi;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.tienMang = tienMang;
        this.ngayTao = ngayTao;
        this.trangThai = trangThai;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }

    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    public String getKi() { return ki; }
    public void setKi(String ki) { this.ki = ki; }

    public Integer getSoDienCu() { return soDienCu; }
    public void setSoDienCu(Integer soDienCu) { this.soDienCu = soDienCu; }

    public Integer getSoDienMoi() { return soDienMoi; }
    public void setSoDienMoi(Integer soDienMoi) { this.soDienMoi = soDienMoi; }

    public Integer getSoNuocCu() { return soNuocCu; }
    public void setSoNuocCu(Integer soNuocCu) { this.soNuocCu = soNuocCu; }

    public Integer getSoNuocMoi() { return soNuocMoi; }
    public void setSoNuocMoi(Integer soNuocMoi) { this.soNuocMoi = soNuocMoi; }

    public BigDecimal getGiaDien() { return giaDien; }
    public void setGiaDien(BigDecimal giaDien) { this.giaDien = giaDien; }

    public BigDecimal getGiaNuoc() { return giaNuoc; }
    public void setGiaNuoc(BigDecimal giaNuoc) { this.giaNuoc = giaNuoc; }

    public BigDecimal getTienMang() { return tienMang; }
    public void setTienMang(BigDecimal tienMang) { this.tienMang = tienMang; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
