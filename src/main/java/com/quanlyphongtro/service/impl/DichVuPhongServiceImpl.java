package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.models.DichVuPhong;
import com.quanlyphongtro.repository.DichVuPhongRepository;
import com.quanlyphongtro.service.DichVuPhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class DichVuPhongServiceImpl implements DichVuPhongService {

    @Autowired
    private DichVuPhongRepository repository;

    @Override
    public List<com.quanlyphongtro.dto.DichVuPhongDto> getAll() {
        // Chuyển đổi từ Entity sang DTO để trả về UI
        return repository.findAll().stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    private com.quanlyphongtro.dto.DichVuPhongDto toDto(DichVuPhong entity) {
        return new com.quanlyphongtro.dto.DichVuPhongDto(
            entity.getId(),
            entity.getPhong().getSoPhong(),
            entity.getMaHopDong(),
            entity.getKi(),
            entity.getSoDienCu(),
            entity.getSoDienMoi(),
            entity.getSoNuocCu(),
            entity.getSoNuocMoi(),
            entity.getGiaDien(),
            entity.getGiaNuoc(),
            entity.getTienMang(),
            entity.getNgayTao(),
            entity.getTrangThai()
        );
    }

    @Override
    public DichVuPhong save(DichVuPhong dv) {
        return repository.save(dv);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public DichVuPhong getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<com.quanlyphongtro.dto.DichVuPhongDto> getByRoom(String soPhong) {
        return repository.findByPhong_SoPhong(soPhong).stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public DichVuPhong findByPhongAndMonth(String soPhong, int month, int year) {
        return repository.findByPhongAndMonthYear(soPhong, month, year);
    }

    @Override
    public DichVuPhong findLastUsage(String soPhong) {
        return repository.findTopByPhong_SoPhongOrderByNgayTaoDesc(soPhong);
    }

    @Override
    public boolean checkDuplicateForPeriod(String soPhong, String ki) {
        return repository.existsByPhong_SoPhongAndKi(soPhong, ki);
    }

    @Autowired
    private com.quanlyphongtro.repository.PhongRepository phongRepository;
    @Autowired
    private com.quanlyphongtro.repository.HopDongThueRepository hopDongRepository; // Service calling Service better, but Repo acceptable here in Service Layer

    @Override
    public DichVuPhong addDichVu(String soPhong, String ki, Integer soDienCu, Integer soDienMoi,
                                 Integer soNuocCu, Integer soNuocMoi,
                                 java.math.BigDecimal giaDien, java.math.BigDecimal giaNuoc,
                                 java.math.BigDecimal tienMang, String trangThai) {
        
        // 1. Kiểm tra phòng tồn tại
        com.quanlyphongtro.models.Phong phong = phongRepository.findById(soPhong)
                .orElseThrow(() -> new RuntimeException("Phòng không tồn tại: " + soPhong));

        // 2. Tìm hợp đồng đang hiệu lực để lấy mã hợp đồng
        com.quanlyphongtro.models.HopDongThue hd = hopDongRepository.findByTrangThai("Đang hiệu lực").stream()
                .filter(h -> h.getPhong().getSoPhong().equals(soPhong))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hợp đồng hiệu lực cho phòng: " + soPhong));

        if (soDienMoi != null && soDienMoi < soDienCu) {
            throw new RuntimeException("Số điện mới phải lớn hơn hoặc bằng số cũ");
        }
        if (soNuocMoi != null && soNuocMoi < soNuocCu) {
            throw new RuntimeException("Số nước mới phải lớn hơn hoặc bằng số cũ");
        }

        DichVuPhong dv = new DichVuPhong();
        
        // Manual ID Generation
        Integer maxId = repository.findMaxId();
        dv.setId(maxId == null ? 1 : maxId + 1);

        dv.setPhong(phong);
        dv.setMaHopDong(hd.getId());
        dv.setKi(ki);
        dv.setSoDienCu(soDienCu);
        dv.setSoDienMoi(soDienMoi);
        dv.setSoNuocCu(soNuocCu);
        dv.setSoNuocMoi(soNuocMoi);
        dv.setGiaDien(giaDien);
        dv.setGiaNuoc(giaNuoc);
        dv.setTienMang(tienMang);
        dv.setNgayTao(java.time.LocalDateTime.now());
        dv.setTrangThai(trangThai != null ? trangThai : "Chờ lập hoá đơn");

        return repository.save(dv);
    }

    @Override
    public java.util.List<com.quanlyphongtro.dto.DichVuPhongDto> getAllSortedByKiDesc() {
        return repository.findAllByOrderByKiDesc().stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.List<com.quanlyphongtro.dto.DichVuPhongDto> getByRoomSortedByKiDesc(String soPhong) {
        return repository.findByPhong_SoPhongOrderByKiDesc(soPhong).stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.List<com.quanlyphongtro.dto.DichVuPhongDto> filterByStatus(String status) {
        return repository.findByTrangThaiOrderByKiDesc(status).stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }
}
