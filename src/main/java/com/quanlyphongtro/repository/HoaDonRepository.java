package com.quanlyphongtro.repository;

import com.quanlyphongtro.models.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE FUNCTION('MONTH', h.ngayLapHoaDon) = :month AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year")
    long countHoaDonInMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE FUNCTION('MONTH', h.ngayLapHoaDon) = :month AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year")
    BigDecimal sumDoanhThuInMonth(@Param("month") int month, @Param("year") int year);

    // Search by ID or Room Name
    @Query("SELECT h FROM HoaDon h WHERE " +
           "LOWER(h.idHoaDon) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.dichVuPhong.phong.soPhong) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    java.util.List<HoaDon> searchHoaDon(@Param("keyword") String keyword);

    // Filter by Status
    java.util.List<HoaDon> findByTrangThai(String trangThai);
    
    // Find by Month/Year (using JPQL)
    @Query("SELECT h FROM HoaDon h WHERE FUNCTION('MONTH', h.ngayLapHoaDon) = :month AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year")
    java.util.List<HoaDon> findByThangNam(@Param("month") int month, @Param("year") int year);
}
