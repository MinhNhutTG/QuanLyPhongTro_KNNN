package com.quanlyphongtro.service;

import org.springframework.stereotype.Service;

import com.quanlyphongtro.models.DichVu;
import java.util.List;

public interface DichVuService {
    List<DichVu> getAllDichVu();
    DichVu saveDichVu(DichVu dichVu);
    void deleteDichVu(Long id);
}
