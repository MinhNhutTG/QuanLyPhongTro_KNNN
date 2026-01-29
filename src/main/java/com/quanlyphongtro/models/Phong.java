	package com.quanlyphongtro.models;
	
	import jakarta.persistence.*;
	
	@Entity
	@Table(name = "phong")
	public class Phong {
	
	    @Id
	    @Column(name = "so_phong", length = 20)
	    private String soPhong;
	
	    @Column(name = "trang_thai", length = 100, nullable = false)
	    private String trangThai;
	
	    @ManyToOne
	    @JoinColumn(name = "ma_loai", nullable = false)
	    private LoaiPhong loaiPhong;
	
	    @Column(name = "ghi_chu", length = 2000)
	    private String ghiChu;
	
		public String getSoPhong() {
			return soPhong;
		}
	
		public void setSoPhong(String soPhong) {
			this.soPhong = soPhong;
		}
	
		public String getTrangThai() {
			return trangThai;
		}
	
		public void setTrangThai(String trangThai) {
			this.trangThai = trangThai;
		}
	
		public LoaiPhong getLoaiPhong() {
			return loaiPhong;
		}
	
		public void setLoaiPhong(LoaiPhong loaiPhong) {
			this.loaiPhong = loaiPhong;
		}
	
		public String getGhiChu() {
			return ghiChu;
		}
	
		public void setGhiChu(String ghiChu) {
			this.ghiChu = ghiChu;
		}
	    
	    
	}
	
