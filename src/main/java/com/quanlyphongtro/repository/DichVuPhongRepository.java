package com.quanlyphongtro.repository;

import com.quanlyphongtro.models.DichVuPhong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DichVuPhongRepository extends JpaRepository<DichVuPhong, Integer> {
    @org.springframework.data.jpa.repository.Query("SELECT d FROM DichVuPhong d WHERE d.phong.soPhong = :soPhong AND FUNCTION('MONTH', d.ngayTao) = :month AND FUNCTION('YEAR', d.ngayTao) = :year")
    DichVuPhong findByPhongAndMonthYear(@org.springframework.data.repository.query.Param("soPhong") String soPhong, @org.springframework.data.repository.query.Param("month") int month, @org.springframework.data.repository.query.Param("year") int year);

    DichVuPhong findTopByPhong_SoPhongOrderByNgayTaoDesc(String soPhong);

    java.util.List<DichVuPhong> findByPhong_SoPhong(String soPhong);

    boolean existsByPhong_SoPhongAndKi(String soPhong, String ki);

    java.util.List<DichVuPhong> findByTrangThai(String trangThai);

    // Sorted queries for legacy parity
    java.util.List<DichVuPhong> findAllByOrderByKiDesc();
    java.util.List<DichVuPhong> findByPhong_SoPhongOrderByKiDesc(String soPhong);
    java.util.List<DichVuPhong> findByTrangThaiOrderByKiDesc(String trangThai);
    @org.springframework.data.jpa.repository.Query("SELECT MAX(d.id) FROM DichVuPhong d")
    Integer findMaxId();
}
