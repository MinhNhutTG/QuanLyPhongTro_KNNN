	package com.quanlyphongtro.models;
	
	import jakarta.persistence.*;
	
	/**
	 * Entity đại diện cho Phòng trọ
	 */
	@Entity
	@Table(name = "phong")
	public class Phong {
	
	    @Id
	    @Column(name = "SoPhong", length = 10)
	    private String soPhong; // Mã số phòng (Primary Key)
	
	    @Column(name = "TrangThai", length = 50, nullable = false)
	    private String trangThai; // Trạng thái: Trống, Đang thuê, Đang sửa...
	
	    @ManyToOne
	    @JoinColumn(name = "MaLoai", nullable = false)
	    private LoaiPhong loaiPhong; // Loại phòng (VIP, Thường...)
	
	    @Column(name = "GhiChu", length = 1000)
	    private String ghiChu; // Ghi chú thêm (nếu có)
	
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
	
