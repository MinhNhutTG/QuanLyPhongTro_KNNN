package com.quanlyphongtro.service;

import org.springframework.stereotype.Service;

import com.quanlyphongtro.models.DichVuPhong;
import com.quanlyphongtro.dto.DichVuPhongDto;
import java.util.List;

public interface DichVuPhongService {
    /**
     * Lấy danh sách tất cả dịch vụ phòng
     */
    List<DichVuPhongDto> getAll();

    /**
     * Lưu thông tin dịch vụ phòng
     */
    DichVuPhong save(DichVuPhong dv);

    /**
     * Xóa dịch vụ phòng theo ID
     */
    void delete(Integer id);

    /**
     * Lấy thông tin dịch vụ theo ID
     */
    DichVuPhong getById(Integer id);

    /**
     * Lấy danh sách dịch vụ của một phòng cụ thể
     */
    List<DichVuPhongDto> getByRoom(String soPhong);
    
    // Find usage for specific room and month/year
    DichVuPhong findByPhongAndMonth(String soPhong, int month, int year);
    
    // Find last recorded usage for room to populate "Old Index"
    DichVuPhong findLastUsage(String soPhong);

    /**
     * Kiểm tra trùng lặp kỳ thanh toán cho phòng
     */
    boolean checkDuplicateForPeriod(String soPhong, String ki);

    /**
     * Thêm mới dịch vụ phòng (Xử lý logic tính toán và lưu trữ)
     */
    DichVuPhong addDichVu(String soPhong, String ki, Integer soDienCu, Integer soDienMoi, 
                          Integer soNuocCu, Integer soNuocMoi, 
                          java.math.BigDecimal giaDien, java.math.BigDecimal giaNuoc, 
                          java.math.BigDecimal tienMang, String trangThai);

    // Sorted queries for legacy parity
    List<DichVuPhongDto> getAllSortedByKiDesc();
    List<DichVuPhongDto> getByRoomSortedByKiDesc(String soPhong);
    List<DichVuPhongDto> filterByStatus(String status);
}
