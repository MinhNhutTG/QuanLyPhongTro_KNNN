package com.quanlyphongtro.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import com.quanlyphongtro.dto.HoaDonDto;

@Service
public interface HoaDonService {
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE FUNCTION('MONTH', h.ngayLapHoaDon) = :month AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year")
    long countHoaDonInMonth(int month, int year);

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE FUNCTION('MONTH', h.ngayLapHoaDon) = :month AND FUNCTION('YEAR', h.ngayLapHoaDon) = :year")
    BigDecimal sumDoanhThuInMonth(int month, int year);
    
    java.util.List<HoaDonDto> getAllHoaDon();
    
    HoaDonDto getHoaDonById(String id);
    
    // Save typically accepts Entity or DTO. For simple refactor let's keep Entity for save OR upgrade to DTO.
    // Given UI uses Entity to save, let's keep Entity for parameter but maybe return DTO?
    // Actually, to fully decouple, saving should ideally take DTO. But UI constructs Entity.
    // Let's keep `saveHoaDon(HoaDon hoaDon)` for now to avoid massive UI rewrite of the save logic in AddEditInvoice.
    HoaDonDto saveHoaDon(HoaDonDto hoaDonDto) throws Exception;
    
    void deleteHoaDon(String id);
    
    java.util.List<HoaDonDto> searchHoaDon(String keyword);
    
    java.util.List<HoaDonDto> filterByTrangThai(String status);
    
    java.util.List<HoaDonDto> getHoaDonByMonth(int month, int year);
    
    // Auto create invoices for all active contracts in a month
    void autoCreateInvoices(int month, int year);
}
