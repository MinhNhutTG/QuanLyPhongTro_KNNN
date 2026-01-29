package com.quanlyphongtro.service;

import com.quanlyphongtro.dto.KhachThueDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface KhachThueService {
    List<KhachThueDto> getAllKhachThue();
    KhachThueDto getKhachThueById(Integer id);
    
    // Create & Update
    KhachThueDto addKhachThue(KhachThueDto dto);
    KhachThueDto updateKhachThue(Integer id, KhachThueDto dto);
    
    // Delete
    void deleteKhachThue(Integer id);
    
    // Search & Filter
    List<KhachThueDto> searchKhachThue(String keyword);
    java.util.List<KhachThueDto> filterByTrangThai(String trangThai);
    
    java.util.List<com.quanlyphongtro.models.ChiTietHopDong> getContractsByGuest(Integer maKhach);
}
