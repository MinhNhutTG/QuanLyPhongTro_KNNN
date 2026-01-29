package com.quanlyphongtro.repository;

import com.quanlyphongtro.models.ChiTietHopDong;
import com.quanlyphongtro.models.HopDongThue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHopDongRepository extends JpaRepository<ChiTietHopDong, Long> {
    List<ChiTietHopDong> findByHopDong(HopDongThue hopDong);
    List<ChiTietHopDong> findByKhachThue_MaKhach(Integer maKhach);
    void deleteByHopDong(HopDongThue hopDong);
}
