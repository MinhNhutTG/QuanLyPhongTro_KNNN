package com.quanlyphongtro.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "khach_thue")
public class KhachThue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_khach")
    private Integer maKhach;

    @Column(name = "ho_ten", length = 100)
    private String hoTen;

    private LocalDateTime ngaySinh;

    @Column(length = 12, nullable = false)
    private String cccd;

    @Column(name = "so_dien_thoai", length = 11)
    private String soDienThoai;

    @Column(name = "que_quan", length = 2000)
    private String queQuan;

    @Column(name = "trang_thai", length = 100, nullable = false)
    private String trangThai;

    @Column(length = 100)
    private String email;

	public Integer getMaKhach() {
		return maKhach;
	}

	public void setMaKhach(Integer maKhach) {
		this.maKhach = maKhach;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public LocalDateTime getNgaySinh() {
		return ngaySinh;
	}

	public void setNgaySinh(LocalDateTime ngaySinh) {
		this.ngaySinh = ngaySinh;
	}

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		this.cccd = cccd;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getQueQuan() {
		return queQuan;
	}

	public void setQueQuan(String queQuan) {
		this.queQuan = queQuan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
}

