package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.models.DichVuPhong;
import com.quanlyphongtro.models.HoaDon;
import com.quanlyphongtro.models.HopDongThue;
import com.quanlyphongtro.models.Phong;
import com.quanlyphongtro.repository.DichVuPhongRepository;
import com.quanlyphongtro.repository.HoaDonRepository;
import com.quanlyphongtro.repository.HopDongThueRepository;
import com.quanlyphongtro.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HopDongThueRepository hopDongThueRepository;

    @Autowired
    private DichVuPhongRepository dichVuPhongRepository;

    @Override
    public long countHoaDonInMonth(int month, int year) {
        return hoaDonRepository.countHoaDonInMonth(month, year);
    }

    @Override
    public BigDecimal sumDoanhThuInMonth(int month, int year) {
        return hoaDonRepository.sumDoanhThuInMonth(month, year);
    }

    @Override
    public List<com.quanlyphongtro.dto.HoaDonDto> getAllHoaDon() {
        return hoaDonRepository.findAll().stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public com.quanlyphongtro.dto.HoaDonDto getHoaDonById(String id) {
        return hoaDonRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public com.quanlyphongtro.dto.HoaDonDto saveHoaDon(com.quanlyphongtro.dto.HoaDonDto dto) throws Exception {
        HoaDon entity = toEntity(dto);
        // Calculate totals before saving (Business Logic)
        calculateTotal(entity);
        HoaDon saved = hoaDonRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public void deleteHoaDon(String id) {
        hoaDonRepository.deleteById(id);
    }

    @Override
    public List<com.quanlyphongtro.dto.HoaDonDto> searchHoaDon(String keyword) {
        return hoaDonRepository.searchHoaDon(keyword).stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<com.quanlyphongtro.dto.HoaDonDto> filterByTrangThai(String status) {
        if ("Tất cả".equals(status) || status == null) {
            return getAllHoaDon();
        }
        return hoaDonRepository.findByTrangThai(status).stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<com.quanlyphongtro.dto.HoaDonDto> getHoaDonByMonth(int month, int year) {
        return hoaDonRepository.findByThangNam(month, year).stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public void autoCreateInvoices(int month, int year) {
        // ... (Logic remains mostly same, just internal entity usage)
        List<DichVuPhong> pendingServices = dichVuPhongRepository.findByTrangThai("Chờ lập hoá đơn");

        for (DichVuPhong dvp : pendingServices) {
            HoaDon hoaDon = new HoaDon();
            StringBuilder sbId = new StringBuilder("HD");
            Random rand = new Random();
            for (int i = 0; i < 8; i++) {
                sbId.append(rand.nextInt(10));
            }
            hoaDon.setIdHoaDon(sbId.toString());
            hoaDon.setDichVuPhong(dvp);
            hoaDon.setNgayLapHoaDon(LocalDateTime.now());
            hoaDon.setTrangThai("Chưa Thanh Toán");
            
            HopDongThue hd = hopDongThueRepository.findById(dvp.getMaHopDong()).orElse(null);
            if (hd != null) {
                hoaDon.setGiaPhong(hd.getGiaPhong());
            } else {
                hoaDon.setGiaPhong(BigDecimal.ZERO);
            }

            int dienCu = dvp.getSoDienCu() != null ? dvp.getSoDienCu() : 0;
            int dienMoi = dvp.getSoDienMoi() != null ? dvp.getSoDienMoi() : dienCu;
            int usageDien = Math.max(0, dienMoi - dienCu);
            hoaDon.setSoDien(usageDien);

            int nuocCu = dvp.getSoNuocCu() != null ? dvp.getSoNuocCu() : 0;
            int nuocMoi = dvp.getSoNuocMoi() != null ? dvp.getSoNuocMoi() : nuocCu;
            int usageNuoc = Math.max(0, nuocMoi - nuocCu);
            hoaDon.setSoNuoc(usageNuoc);

            calculateTotal(hoaDon);
            
            hoaDonRepository.save(hoaDon);
            
            dvp.setTrangThai("Đã lập hoá đơn");
            dichVuPhongRepository.save(dvp);
        }
    }

    private void calculateTotal(HoaDon hd) {
        BigDecimal total = BigDecimal.ZERO;
        
        if (hd.getGiaPhong() != null) {
            total = total.add(hd.getGiaPhong());
        }

        if (hd.getDichVuPhong() != null) {
             if (hd.getDichVuPhong().getGiaDien() != null && hd.getSoDien() != null) {
                BigDecimal tienDien = hd.getDichVuPhong().getGiaDien().multiply(new BigDecimal(hd.getSoDien()));
                hd.setTienDien(tienDien);
                total = total.add(tienDien);
            }
            if (hd.getDichVuPhong().getGiaNuoc() != null && hd.getSoNuoc() != null) {
                BigDecimal tienNuoc = hd.getDichVuPhong().getGiaNuoc().multiply(new BigDecimal(hd.getSoNuoc()));
                hd.setTienNuoc(tienNuoc);
                total = total.add(tienNuoc);
            }
            if (hd.getDichVuPhong().getTienMang() != null) {
                total = total.add(hd.getDichVuPhong().getTienMang());
            }
        }
        
        if (hd.getPhiKhac() != null) {
            total = total.add(hd.getPhiKhac());
        }

        hd.setTongTien(total);
    }
    
    // Mapping Helpers
    private com.quanlyphongtro.dto.HoaDonDto toDto(HoaDon entity) {
        if (entity == null) return null;
        com.quanlyphongtro.dto.DichVuPhongDto dvDto = null;
        if (entity.getDichVuPhong() != null) {
             DichVuPhong dv = entity.getDichVuPhong();
             dvDto = new com.quanlyphongtro.dto.DichVuPhongDto(
                dv.getId(), dv.getPhong().getSoPhong(), dv.getMaHopDong(), dv.getKi(),
                dv.getSoDienCu(), dv.getSoDienMoi(), dv.getSoNuocCu(), dv.getSoNuocMoi(),
                dv.getGiaDien(), dv.getGiaNuoc(), dv.getTienMang(), dv.getNgayTao(), dv.getTrangThai()
             );
        }
        
        return new com.quanlyphongtro.dto.HoaDonDto(
            entity.getIdHoaDon(),
            dvDto,
            entity.getSoDien(),
            entity.getSoNuoc(),
            entity.getTienDien(),
            entity.getTienNuoc(),
            entity.getPhiKhac(),
            entity.getTongTien(),
            entity.getGiaPhong(),
            entity.getNgayLapHoaDon(),
            entity.getTrangThai(),
            entity.getGhiChu()
        );
    }
    
    private HoaDon toEntity(com.quanlyphongtro.dto.HoaDonDto dto) {
        HoaDon entity = new HoaDon();
        if (dto.getIdHoaDon() != null && !dto.getIdHoaDon().isEmpty()) {
             // Try to fetch existing if update? Or just set ID?
             // If we just set ID, JPA might try to insert if not managed. 
             // Best to fetch if exists.
             Optional<HoaDon> existing = hoaDonRepository.findById(dto.getIdHoaDon());
             if (existing.isPresent()) {
                 entity = existing.get();
             } else {
                 entity.setIdHoaDon(dto.getIdHoaDon());
             }
        }
        
        // Map fields
        entity.setSoDien(dto.getSoDien());
        entity.setSoNuoc(dto.getSoNuoc());
        entity.setTienDien(dto.getTienDien());
        entity.setTienNuoc(dto.getTienNuoc());
        entity.setPhiKhac(dto.getPhiKhac());
        entity.setTongTien(dto.getTongTien());
        entity.setGiaPhong(dto.getGiaPhong());
        entity.setNgayLapHoaDon(dto.getNgayLapHoaDon());
        entity.setTrangThai(dto.getTrangThai());
        entity.setGhiChu(dto.getGhiChu());
        
        // Map Relation DichVuPhong
        // If DTO has DichVuPhongDto, we need to link it.
        // Usually UI passes DTO with ID.
        if (dto.getDichVuPhong() != null && dto.getDichVuPhong().getId() != null) {
            DichVuPhong dv = dichVuPhongRepository.findById(dto.getDichVuPhong().getId()).orElse(null);
            entity.setDichVuPhong(dv);
        }
        
        return entity;
    }
}
