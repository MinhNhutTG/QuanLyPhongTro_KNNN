package com.quanlyphongtro.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quanlyphongtro.models.Phong;
@Repository
public interface PhongRepository extends JpaRepository<Phong, String> {
    long countByTrangThai(String trangThai);
}
