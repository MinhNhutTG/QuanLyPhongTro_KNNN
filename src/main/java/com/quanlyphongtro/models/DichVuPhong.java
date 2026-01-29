package com.quanlyphongtro.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dichvuphong")
public class DichVuPhong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Integer id;	

    @ManyToOne
    @JoinColumn(name = "SoPhong", nullable = false)
    private Phong phong;

    @Column(name = "MaHopDong", length = 10)
    private String maHopDong;

    @Column(name = "Ki")
    private String ki;

    @Column(name = "SoDienCu")
    private Integer soDienCu;

    @Column(name = "SoDienMoi")
    private Integer soDienMoi;

    @Column(name = "SoNuocCu")
    private Integer soNuocCu;

    @Column(name = "SoNuocMoi")
    private Integer soNuocMoi;

    @Column(name = "GiaDien")
    private BigDecimal giaDien;

    @Column(name = "GiaNuoc")
    private BigDecimal giaNuoc;

    @Column(name = "TienMang")
    private BigDecimal tienMang;

	    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "TrangThai", length = 100)
    private String trangThai;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Phong getPhong() {
		return phong;
	}

	public void setPhong(Phong phong) {
		this.phong = phong;
	}

	public String getMaHopDong() {
		return maHopDong;
	}

	public void setMaHopDong(String maHopDong) {
		this.maHopDong = maHopDong;
	}

	public String getKi() {
		return ki;
	}

	public void setKi(String ki) {
		this.ki = ki;
	}

	public Integer getSoDienCu() {
		return soDienCu;
	}

	public void setSoDienCu(Integer soDienCu) {
		this.soDienCu = soDienCu;
	}

	public Integer getSoDienMoi() {
		return soDienMoi;
	}

	public void setSoDienMoi(Integer soDienMoi) {
		this.soDienMoi = soDienMoi;
	}

	public Integer getSoNuocCu() {
		return soNuocCu;
	}

	public void setSoNuocCu(Integer soNuocCu) {
		this.soNuocCu = soNuocCu;
	}

	public Integer getSoNuocMoi() {
		return soNuocMoi;
	}

	public void setSoNuocMoi(Integer soNuocMoi) {
		this.soNuocMoi = soNuocMoi;
	}

	public BigDecimal getGiaDien() {
		return giaDien;
	}

	public void setGiaDien(BigDecimal giaDien) {
		this.giaDien = giaDien;
	}

	public BigDecimal getGiaNuoc() {
		return giaNuoc;
	}

	public void setGiaNuoc(BigDecimal giaNuoc) {
		this.giaNuoc = giaNuoc;
	}

	public BigDecimal getTienMang() {
		return tienMang;
	}

	public void setTienMang(BigDecimal tienMang) {
		this.tienMang = tienMang;
	}

	public LocalDateTime getNgayTao() {
		return ngayTao;
	}

	public void setNgayTao(LocalDateTime ngayTao) {
		this.ngayTao = ngayTao;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
    
    
}

