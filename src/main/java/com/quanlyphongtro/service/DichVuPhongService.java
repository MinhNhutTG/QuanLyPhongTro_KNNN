package com.quanlyphongtro.service;

import org.springframework.stereotype.Service;

import com.quanlyphongtro.models.DichVuPhong;
import com.quanlyphongtro.dto.DichVuPhongDto;
import java.util.List;

public interface DichVuPhongService {
    List<DichVuPhongDto> getAll();
    DichVuPhong save(DichVuPhong dv);
    void delete(Integer id);
    DichVuPhong getById(Integer id);
    List<DichVuPhongDto> getByRoom(String soPhong);
    
    // Find usage for specific room and month/year
    DichVuPhong findByPhongAndMonth(String soPhong, int month, int year);
    
    // Find last recorded usage for room to populate "Old Index"
    DichVuPhong findLastUsage(String soPhong);

    boolean checkDuplicateForPeriod(String soPhong, String ki);

    // New method to handle creation from UI data without direct Repository access
    DichVuPhong addDichVu(String soPhong, String ki, Integer soDienCu, Integer soDienMoi, 
                          Integer soNuocCu, Integer soNuocMoi, 
                          java.math.BigDecimal giaDien, java.math.BigDecimal giaNuoc, 
                          java.math.BigDecimal tienMang, String trangThai);

    // Sorted queries for legacy parity
    List<DichVuPhongDto> getAllSortedByKiDesc();
    List<DichVuPhongDto> getByRoomSortedByKiDesc(String soPhong);
    List<DichVuPhongDto> filterByStatus(String status);
}
