package com.quanlyphongtro.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hoadon")
public class HoaDon {

    @Id
    @Column(name = "IDHoaDon", length = 10)
    private String idHoaDon;

    @ManyToOne
    @JoinColumn(name = "IDDichVu")
    private DichVuPhong dichVuPhong;

    @Column(name = "SoDien")
    private Integer soDien;

    @Column(name = "SoNuoc")
    private Integer soNuoc;

    @Column(name = "TienDien")
    private BigDecimal tienDien;

    @Column(name = "TienNuoc")
    private BigDecimal tienNuoc;

    @Column(name = "PhiKhac")
    private BigDecimal phiKhac;

    @Column(name = "TongTien")
    private BigDecimal tongTien;

    @Column(name = "GiaPhong")
    private BigDecimal giaPhong;

    @Column(name = "NgayLapHoaDon")
    private LocalDateTime ngayLapHoaDon;

    @Column(name = "TrangThai", length = 100, nullable = false)
    private String trangThai;

    @Column(name = "GhiChu", length = 2000)
    private String ghiChu;

	public String getIdHoaDon() {
		return idHoaDon;
	}

	public void setIdHoaDon(String idHoaDon) {
		this.idHoaDon = idHoaDon;
	}

	public DichVuPhong getDichVuPhong() {
		return dichVuPhong;
	}

	public void setDichVuPhong(DichVuPhong dichVuPhong) {
		this.dichVuPhong = dichVuPhong;
	}

	public Integer getSoDien() {
		return soDien;
	}

	public void setSoDien(Integer soDien) {
		this.soDien = soDien;
	}

	public Integer getSoNuoc() {
		return soNuoc;
	}

	public void setSoNuoc(Integer soNuoc) {
		this.soNuoc = soNuoc;
	}

	public BigDecimal getTienDien() {
		return tienDien;
	}

	public void setTienDien(BigDecimal tienDien) {
		this.tienDien = tienDien;
	}

	public BigDecimal getTienNuoc() {
		return tienNuoc;
	}

	public void setTienNuoc(BigDecimal tienNuoc) {
		this.tienNuoc = tienNuoc;
	}

	public BigDecimal getPhiKhac() {
		return phiKhac;
	}

	public void setPhiKhac(BigDecimal phiKhac) {
		this.phiKhac = phiKhac;
	}

	public BigDecimal getTongTien() {
		return tongTien;
	}

	public void setTongTien(BigDecimal tongTien) {
		this.tongTien = tongTien;
	}

	public BigDecimal getGiaPhong() {
		return giaPhong;
	}

	public void setGiaPhong(BigDecimal giaPhong) {
		this.giaPhong = giaPhong;
	}

	public LocalDateTime getNgayLapHoaDon() {
		return ngayLapHoaDon;
	}

	public void setNgayLapHoaDon(LocalDateTime ngayLapHoaDon) {
		this.ngayLapHoaDon = ngayLapHoaDon;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getGhiChu() {
		return ghiChu;
	}

	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
    
    
}
