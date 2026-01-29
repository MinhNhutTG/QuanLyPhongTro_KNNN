package com.quanlyphongtro.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dichvu")
public class DichVu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;

    @Column(name = "TenDichVu", nullable = false)
    private String tenDichVu;

    @Column(name = "Gia")
    private BigDecimal gia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenDichVu() {
		return tenDichVu;
	}

	public void setTenDichVu(String tenDichVu) {
		this.tenDichVu = tenDichVu;
	}

	public BigDecimal getGia() {
		return gia;
	}

	public void setGia(BigDecimal gia) {
		this.gia = gia;
	}
    
    
}

