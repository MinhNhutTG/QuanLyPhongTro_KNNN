package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.dto.KhachThueDto;
import com.quanlyphongtro.models.KhachThue;
import com.quanlyphongtro.repository.KhachThueRepository;
import com.quanlyphongtro.service.KhachThueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class KhachThueServiceImpl implements KhachThueService {

    @Autowired
    private KhachThueRepository khachThueRepository;

    @Override
    public List<KhachThueDto> getAllKhachThue() {
        return khachThueRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public KhachThueDto getKhachThueById(Integer id) {
        return khachThueRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public KhachThueDto addKhachThue(KhachThueDto dto) {
        validateDto(dto);
        if (dto.getCccd() != null && !dto.getCccd().isEmpty()) {
            // Check trùng CCCD (Optional: có thể thêm nếu cần)
            // if (isCccdExisted(dto.getCccd())) throw new RuntimeException("CCCD đã tồn tại!");
        }
        
        KhachThue entity = new KhachThue();
        updateEntityFromDto(entity, dto);
        
        KhachThue savedEntity = khachThueRepository.save(entity);
        return convertToDto(savedEntity);
    }

    @Override
    public KhachThueDto updateKhachThue(Integer id, KhachThueDto dto) {
        validateDto(dto);
        KhachThue entity = khachThueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách thuê với ID: " + id));
        
        updateEntityFromDto(entity, dto);
        
        KhachThue updatedEntity = khachThueRepository.save(entity);
        return convertToDto(updatedEntity);
    }

    @Override
    public void deleteKhachThue(Integer id) {
        if (!khachThueRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy khách thuê để xóa!");
        }
        khachThueRepository.deleteById(id);
    }

    @Override
    public List<KhachThueDto> searchKhachThue(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllKhachThue();
        }
        return khachThueRepository.searchKhachThue(keyword).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Autowired
    private com.quanlyphongtro.repository.ChiTietHopDongRepository chiTietHopDongRepository;

    @Override
    public List<KhachThueDto> filterByTrangThai(String trangThai) {
        if ("Tất cả trạng thái".equals(trangThai) || trangThai == null) {
            return getAllKhachThue();
        }
        return khachThueRepository.findByTrangThai(trangThai).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public java.util.List<com.quanlyphongtro.models.ChiTietHopDong> getContractsByGuest(Integer maKhach) {
        return chiTietHopDongRepository.findByKhachThue_MaKhach(maKhach);
    }

    // --- Helpers ---

    private void validateDto(KhachThueDto dto) {
        if (dto.getHoTen() == null || dto.getHoTen().trim().isEmpty()) {
            throw new RuntimeException("Họ tên không được để trống"); // C# message
        }
        
        // Date Check (C# Logic: g.NgaySinh >= DateTime.Now)
        if (dto.getNgaySinh() == null) {
             throw new RuntimeException("Ngày sinh khách không được để trống");
        }
        if (dto.getNgaySinh().isAfter(java.time.LocalDateTime.now())) {
             throw new RuntimeException("Ngày sinh khách không hợp lệ");
        }

        // CCCD Check (C# Logic: Length < 5 || > 13)
        if (dto.getCccd() == null || dto.getCccd().trim().isEmpty()) {
             // C# didn't explicitly check empty string separately from length in snippet but standard practice
             // actually snippet said: if (g.CCCD.Length < 5 ...)
             throw new RuntimeException("CCCD không hợp lệ");
        }
        if (dto.getCccd().length() < 5 || dto.getCccd().length() > 13) {
            throw new RuntimeException("CCCD không hợp lệ");
        }
        // Note: C# did NOT enforce numeric-only for CCCD in the snippet? 
        // "if (g.CCCD.Length < 5 || g.CCCD.Length > 13)"
        // Java was: matches("\\d{9,12}"). I will relax it to length check to be safe, 
        // but typically CCCD is numeric. I'll stick to length to allow flexibility if C# allowed it.

        // Phone Check (C# Logic: Length != 10)
        if (dto.getSoDienThoai() != null && !dto.getSoDienThoai().isEmpty()) {
            if (dto.getSoDienThoai().length() != 10) {
                throw new RuntimeException("Số điện thoại không hợp lệ");
            }
            // Optional: Check numeric
            if (!dto.getSoDienThoai().matches("\\d+")) {
                 throw new RuntimeException("Số điện thoại không hợp lệ");
            }
        }
        
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
             if (!dto.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                 throw new RuntimeException("Email không đúng định dạng!");
             }
        }
    }

    private KhachThueDto convertToDto(KhachThue entity) {
        KhachThueDto dto = new KhachThueDto();
        dto.setMaKhach(entity.getMaKhach());
        dto.setHoTen(entity.getHoTen());
        dto.setCccd(entity.getCccd());
        dto.setSoDienThoai(entity.getSoDienThoai());
        dto.setNgaySinh(entity.getNgaySinh());
        dto.setEmail(entity.getEmail());
        dto.setQueQuan(entity.getQueQuan());
        dto.setTrangThai(entity.getTrangThai());
        return dto;
    }

    private void updateEntityFromDto(KhachThue entity, KhachThueDto dto) {
        entity.setHoTen(dto.getHoTen());
        entity.setCccd(dto.getCccd());
        entity.setSoDienThoai(dto.getSoDienThoai());
        entity.setNgaySinh(dto.getNgaySinh());
        entity.setEmail(dto.getEmail());
        entity.setQueQuan(dto.getQueQuan());
        // Mặc định trạng thái nếu null khi thêm mới
        if (entity.getTrangThai() == null && dto.getTrangThai() == null) {
            entity.setTrangThai("Đang thuê");
        } else if (dto.getTrangThai() != null) {
            entity.setTrangThai(dto.getTrangThai());
        }
    }
}