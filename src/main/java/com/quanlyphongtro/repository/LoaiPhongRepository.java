package com.quanlyphongtro.repository;

import com.quanlyphongtro.models.LoaiPhong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiPhongRepository extends JpaRepository<LoaiPhong, String> {
}
