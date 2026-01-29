package com.quanlyphongtro.repository;

import com.quanlyphongtro.models.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, Long> {
}
