package com.quanlyphongtro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HopDongDto {
    private String id; // Mã HĐ
    private String soPhong;
    private BigDecimal giaPhong;
    private LocalDate ngayThue;
    private LocalDate hanThue;
    private LocalDate ngayTao;
    private String trangThai;

    // Danh sách khách thuê trong hợp đồng này
    private List<ChiTietHopDongDto> listKhach = new ArrayList<>();

    public HopDongDto() {}

    // Getters & Setters...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
    public BigDecimal getGiaPhong() { return giaPhong; }
    public void setGiaPhong(BigDecimal giaPhong) { this.giaPhong = giaPhong; }
    public LocalDate getNgayThue() { return ngayThue; }
    public void setNgayThue(LocalDate ngayThue) { this.ngayThue = ngayThue; }
    public LocalDate getHanThue() { return hanThue; }
    public void setHanThue(LocalDate hanThue) { this.hanThue = hanThue; }
    public LocalDate getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDate ngayTao) { this.ngayTao = ngayTao; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public List<ChiTietHopDongDto> getListKhach() { return listKhach; }
    public void setListKhach(List<ChiTietHopDongDto> listKhach) { this.listKhach = listKhach; }
}