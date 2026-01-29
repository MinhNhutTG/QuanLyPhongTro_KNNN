package com.quanlyphongtro.models;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "hopdongthue")
public class HopDongThue {

    @Id
    @Column(name = "ID", length = 10)
    private String id;

    @Column(name = "NgayThue")
    private LocalDateTime ngayThue;

    @Column(name = "HanThue")
    private LocalDateTime hanThue;

    @ManyToOne
    @JoinColumn(name = "SoPhong", nullable = false)
    private Phong phong;

    @Column(name = "GiaPhong")
    private BigDecimal giaPhong;

    @Column(name = "TrangThai", length = 50, nullable = false)
    private String trangThai;

    @Column(name = "NgayTaoHopDong")
    private LocalDateTime ngayTaoHopDong;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getNgayThue() {
		return ngayThue;
	}

	public void setNgayThue(LocalDateTime ngayThue) {
		this.ngayThue = ngayThue;
	}

	public LocalDateTime getHanThue() {
		return hanThue;
	}

	public void setHanThue(LocalDateTime hanThue) {
		this.hanThue = hanThue;
	}

	public Phong getPhong() {
		return phong;
	}

	public void setPhong(Phong phong) {
		this.phong = phong;
	}

	public BigDecimal getGiaPhong() {
		return giaPhong;
	}

	public void setGiaPhong(BigDecimal giaPhong) {
		this.giaPhong = giaPhong;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public LocalDateTime getNgayTaoHopDong() {
		return ngayTaoHopDong;
	}

	public void setNgayTaoHopDong(LocalDateTime ngayTaoHopDong) {
		this.ngayTaoHopDong = ngayTaoHopDong;
	}
    
    
}

