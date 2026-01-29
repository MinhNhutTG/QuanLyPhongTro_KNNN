package com.quanlyphongtro.repository;

import com.quanlyphongtro.dto.ThongKeDto;
import com.quanlyphongtro.models.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // Nhớ import dòng này
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongKeRepository extends JpaRepository<HoaDon, String> {

    // 1. Tổng quan (Giữ nguyên)
    @Query("""
        SELECT p.soPhong as nhan, SUM(h.tongTien) as giaTri
        FROM HoaDon h
        LEFT JOIN h.dichVuPhong dvp
        LEFT JOIN dvp.phong p
        WHERE h.trangThai = 'Đã Thanh Toán' AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year
        GROUP BY p.soPhong
        """)
    List<ThongKeDto> getDoanhThuTungPhong(@Param("year") int year);


    // 2. Doanh thu: Theo tháng (Giữ nguyên - Đã chạy đúng)
    @Query("SELECT FUNCTION('MONTH', h.ngayLapHoaDon), SUM(h.tongTien) " +
            "FROM HoaDon h WHERE h.trangThai = 'Đã Thanh Toán' AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year " +
            "GROUP BY FUNCTION('MONTH', h.ngayLapHoaDon) " +
            "ORDER BY FUNCTION('MONTH', h.ngayLapHoaDon)")
    List<Object[]> getDoanhThuTheoThangRaw(@Param("year") int year);

    // 3. Doanh thu: Theo Quý (SỬA LẠI ĐOẠN NÀY)
    // Thay logic CEILING(...) phức tạp bằng hàm QUARTER(...) của MySQL
    @Query("SELECT FUNCTION('QUARTER', h.ngayLapHoaDon), SUM(h.tongTien) " +
            "FROM HoaDon h WHERE h.trangThai = 'Đã Thanh Toán' AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year " +
            "GROUP BY FUNCTION('QUARTER', h.ngayLapHoaDon) " +
            "ORDER BY FUNCTION('QUARTER', h.ngayLapHoaDon)")
    List<Object[]> getDoanhThuTheoQuiRaw(@Param("year") int year);

    // 4. Dịch vụ (Giữ nguyên - Đã chạy đúng)
    @Query("SELECT FUNCTION('MONTH', d.ngayTao), " +
            "SUM(d.soDienMoi - d.soDienCu), " +
            "SUM(d.soNuocMoi - d.soNuocCu) " +
            "FROM DichVuPhong d WHERE FUNCTION('YEAR', d.ngayTao) = :year " +
            "GROUP BY FUNCTION('MONTH', d.ngayTao) " +
            "ORDER BY FUNCTION('MONTH', d.ngayTao)")
    List<Object[]> getTieuThuDienNuocRaw(@Param("year") int year);
}