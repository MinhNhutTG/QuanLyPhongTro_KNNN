package com.quanlyphongtro.service;


import com.quanlyphongtro.models.DichVu;
import java.util.List;

public interface DichVuService {
    List<DichVu> getAllDichVu();
    DichVu saveDichVu(DichVu dichVu);
    void deleteDichVu(Long id);
}
