package com.quanlyphongtro.models;

import jakarta.persistence.*;

@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_nha_tro", length = 200, nullable = false)
    private String tenNhaTro;

    @Column(name = "so_tai_khoan", length = 20, nullable = false)
    private String soTaiKhoan;

    @Column(name = "ten_tai_khoan", length = 200, nullable = false)
    private String tenTaiKhoan;

    @Column(name = "ten_ngan_hang", length = 200, nullable = false)
    private String tenNganHang;

    @Column(name = "email_system", columnDefinition = "TEXT")
    private String emailSystem;

    @Column(name = "app_password", columnDefinition = "TEXT")
    private String appPassword;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenNhaTro() {
		return tenNhaTro;
	}

	public void setTenNhaTro(String tenNhaTro) {
		this.tenNhaTro = tenNhaTro;
	}

	public String getSoTaiKhoan() {
		return soTaiKhoan;
	}

	public void setSoTaiKhoan(String soTaiKhoan) {
		this.soTaiKhoan = soTaiKhoan;
	}

	public String getTenTaiKhoan() {
		return tenTaiKhoan;
	}

	public void setTenTaiKhoan(String tenTaiKhoan) {
		this.tenTaiKhoan = tenTaiKhoan;
	}

	public String getTenNganHang() {
		return tenNganHang;
	}

	public void setTenNganHang(String tenNganHang) {
		this.tenNganHang = tenNganHang;
	}

	public String getEmailSystem() {
		return emailSystem;
	}

	public void setEmailSystem(String emailSystem) {
		this.emailSystem = emailSystem;
	}

	public String getAppPassword() {
		return appPassword;
	}

	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
    
    
}

