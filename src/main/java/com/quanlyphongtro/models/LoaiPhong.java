package com.quanlyphongtro.models;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "loaiphong")
public class LoaiPhong {

    @Id
    @Column(name = "MaLoai", length = 10)
    private String maLoai;

    @Column(name = "TenLoai", length = 100)
    private String tenLoai;

    @Column(name = "Gia")
    private BigDecimal gia;

	@Column(name ="so_nguoi_toi_da")
	private Integer soNguoiToiDa;

	public Integer getSoNguoiToiDa() {
		return soNguoiToiDa;
	}

	public void setSoNguoiToiDa(Integer soNguoiToiDa) {
		this.soNguoiToiDa = soNguoiToiDa;
	}

	public String getMaLoai() {
		return maLoai;
	}

	public void setMaLoai(String maLoai) {
		this.maLoai = maLoai;
	}

	public String getTenLoai() {
		return tenLoai;
	}

	public void setTenLoai(String tenLoai) {
		this.tenLoai = tenLoai;
	}

	public BigDecimal getGia() {
		return gia;
	}

	public void setGia(BigDecimal gia) {
		this.gia = gia;
	}
    
    
}

