package com.quanlyphongtro.models;
import jakarta.persistence.*;

@Entity
@Table(name = "chitiethopdong")
public class ChiTietHopDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDHopDong", nullable = false)
    private HopDongThue hopDong;

    @ManyToOne
    @JoinColumn(name = "MaKhach", nullable = false)
    private KhachThue khachThue;

    @Column(name = "VaiTro", length = 100)
    private String vaiTro;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HopDongThue getHopDong() {
		return hopDong;
	}

	public void setHopDong(HopDongThue hopDong) {
		this.hopDong = hopDong;
	}

	public KhachThue getKhachThue() {
		return khachThue;
	}

	public void setKhachThue(KhachThue khachThue) {
		this.khachThue = khachThue;
	}

	public String getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}
    
    
}
