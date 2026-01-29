package com.quanlyphongtro.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hoa_don")
public class HoaDon {

    @Id
    @Column(name = "id_hoa_don", length = 10)
    private String idHoaDon;

    @ManyToOne
    @JoinColumn(name = "id_dich_vu")
    private DichVuPhong dichVuPhong;

    private Integer soDien;
    private Integer soNuoc;

    private BigDecimal tienDien;
    private BigDecimal tienNuoc;
    private BigDecimal phiKhac;
    private BigDecimal tongTien;
    private BigDecimal giaPhong;

    private LocalDateTime ngayLapHoaDon;

    @Column(length = 100, nullable = false)
    private String trangThai;

    @Column(length = 2000)
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
