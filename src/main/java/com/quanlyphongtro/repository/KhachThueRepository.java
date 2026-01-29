package com.quanlyphongtro.repository;

import com.quanlyphongtro.models.KhachThue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachThueRepository extends JpaRepository<KhachThue, Integer> {
    
    // Tìm kiếm theo các trường liên quan
    @Query("SELECT k FROM KhachThue k WHERE " +
           "LOWER(k.hoTen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "k.cccd LIKE CONCAT('%', :keyword, '%') OR " +
           "k.soDienThoai LIKE CONCAT('%', :keyword, '%') OR " +
           "CAST(k.maKhach AS string) LIKE CONCAT('%', :keyword, '%')")
    List<KhachThue> searchKhachThue(@Param("keyword") String keyword);

    // Lọc theo trạng thái
    List<KhachThue> findByTrangThai(String trangThai);


}
