package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.dto.ChiTietHopDongDto;
import com.quanlyphongtro.dto.HopDongDto;
import com.quanlyphongtro.models.*;
import com.quanlyphongtro.repository.ChiTietHopDongRepository;
import com.quanlyphongtro.repository.HopDongThueRepository;
import com.quanlyphongtro.repository.KhachThueRepository;
import com.quanlyphongtro.repository.PhongRepository;
import com.quanlyphongtro.service.HopDongThueSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HopDongThueServiceImpl implements HopDongThueSevice {
    @Autowired
    private HopDongThueRepository hopDongRepo;
    @Autowired
    private ChiTietHopDongRepository chiTietRepo;
    @Autowired
    private PhongRepository phongRepo;
    @Autowired
    private KhachThueRepository khachRepo;

    @Override
    public List<HopDongDto> getAllHopDong() {
        return hopDongRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<HopDongDto> getHopDongByStatus(String status) {
        if ("Tất cả hợp đồng".equals(status)) return getAllHopDong();
        return hopDongRepo.findAll().stream()
                .filter(hd -> hd.getTrangThai().equalsIgnoreCase(status))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public HopDongDto getHopDongById(String id) {
        return hopDongRepo.findById(id).map(this::toDto).orElse(null);
    }

    @Transactional
    @Override
    public void saveHopDong(HopDongDto dto) throws Exception {
        // 1. Map DTO -> Entity HopDong
        HopDongThue hd = new HopDongThue();
        
        // AUTO GENERATE ID if null
        if (dto.getId() == null || dto.getId().isEmpty()) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            String autoId = "HD" + now.format(java.time.format.DateTimeFormatter.ofPattern("MMddHHmm"));
            hd.setId(autoId);
        } else {
            hd.setId(dto.getId());
        }

        hd.setNgayThue(dto.getNgayThue().atStartOfDay());
        hd.setHanThue(dto.getHanThue().atStartOfDay());
        hd.setNgayTaoHopDong(dto.getNgayTao().atStartOfDay());
        hd.setGiaPhong(dto.getGiaPhong());
        hd.setTrangThai(dto.getTrangThai());

        Integer numberGuest = dto.getListKhach().size();


        if (dto.getNgayThue().isAfter(dto.getHanThue())) {
            throw new Exception("Ngày thuê phải trước hạn thuê!");
        }
        
        Phong phong = phongRepo.findById(dto.getSoPhong())
                .orElseThrow(() -> new Exception("Phòng không tồn tại"));
        LoaiPhong lp = phong.getLoaiPhong();

        if (numberGuest > lp.getSoNguoiToiDa()){
            throw new Exception("Số lượng khách vượt quá sức chứa");
        }

        // Check room availability for new active contracts
        if ("Đang hiệu lực".equals(dto.getTrangThai())) {
             // Logic check phòng trống (giữ nguyên logic cũ nếu có, ở đây đang comment hoặc làm đơn giản)
        }

        hd.setPhong(phong);

        // Cập nhật trạng thái phòng
        if("Đang hiệu lực".equals(dto.getTrangThai())) {
            phong.setTrangThai("Đang thuê");
            phongRepo.save(phong);
        }

        // Lưu Hợp đồng trước
        HopDongThue savedHd = hopDongRepo.save(hd);

        // --- XỬ LÝ KHÁCH THUÊ ---
        // 1. Reset trạng thái khách cũ về "Chưa thuê"
        // Tìm các chi tiết cũ của hợp đồng này (nếu đang update)
        List<ChiTietHopDong> oldDetails = chiTietRepo.findByHopDong(savedHd);
        for (ChiTietHopDong oldCt : oldDetails) {
            KhachThue khachCu = oldCt.getKhachThue();
            if (khachCu != null) {
                khachCu.setTrangThai("Chưa thuê"); // Reset trước, lát nữa ai còn trong list mới sẽ được set lại là Đang thuê
                khachRepo.save(khachCu);
            }
        }
        
        // 2. Xóa chi tiết cũ
        chiTietRepo.deleteByHopDong(savedHd);

        // 3. Thêm chi tiết mới & Cập nhật trạng thái "Đang thuê"
        for (ChiTietHopDongDto chiTietDto : dto.getListKhach()) {
            if (chiTietDto.getMaKhach() == null ) {
                throw new Exception("Mã khách thuê không được để trống!");
            }

            KhachThue khachMoi = khachRepo.findById(chiTietDto.getMaKhach())
                    .orElseThrow(() -> new Exception("Khach thue voi ID " + chiTietDto.getMaKhach() + " khong ton tai"));

            ChiTietHopDong chiTiet = new ChiTietHopDong();
            chiTiet.setHopDong(savedHd);
            chiTiet.setVaiTro(chiTietDto.getVaiTro());
            chiTiet.setKhachThue(khachMoi);

            // Cập nhật trạng thái khách -> "Đang thuê"
            // Lưu ý: Nếu khách này vừa bị reset thành "Chưa thuê" ở bước 1, giờ sẽ được set lại thành "Đang thuê" -> Đúng logic
            khachMoi.setTrangThai("Đang thuê"); // Sửa lại đúng chính tả "Đang thuê" thay vì "Đang Thuê" cho đồng bộ
            khachRepo.save(khachMoi);

            chiTietRepo.save(chiTiet);
        }
    }

    @Override
    public void deleteHopDong(String id) {
        HopDongThue hd = hopDongRepo.findById(id).orElse(null);
        if(hd != null) {
            // 1. Cập nhật lại phòng thành "Trống"
            Phong p = hd.getPhong();
            if (p != null) {
                p.setTrangThai("Trống");
                phongRepo.save(p);
            }

            // 2. Cập nhật trạng thái tất cả khách trong hợp đồng thành "Chưa thuê"
            List<ChiTietHopDong> listChiTiet = chiTietRepo.findByHopDong(hd);
            for (ChiTietHopDong ct : listChiTiet) {
                KhachThue khach = ct.getKhachThue();
                if (khach != null) {
                    khach.setTrangThai("Chưa thuê");
                    khachRepo.save(khach);
                }
            }

            // 3. Xóa chi tiết hợp đồng trước để tránh lỗi ràng buộc khóa ngoại (Foreign Key)
            chiTietRepo.deleteByHopDong(hd);

            // 4. Xóa hợp đồng chính
            hopDongRepo.delete(hd);
        }
    }

    private HopDongDto toDto(HopDongThue entity) {
        HopDongDto dto = new HopDongDto();
        dto.setId(entity.getId());
        dto.setSoPhong(entity.getPhong().getSoPhong());
        dto.setGiaPhong(entity.getGiaPhong());
        dto.setNgayThue(entity.getNgayThue().toLocalDate());
        dto.setHanThue(entity.getHanThue().toLocalDate());
        dto.setNgayTao(entity.getNgayTaoHopDong().toLocalDate());
        dto.setTrangThai(entity.getTrangThai());

        // Load danh sách khách
        List<ChiTietHopDong> listCT = chiTietRepo.findByHopDong(entity);
        List<ChiTietHopDongDto> listDto = new ArrayList<>();
        for(ChiTietHopDong ct : listCT) {
            listDto.add(new ChiTietHopDongDto(
                    ct.getKhachThue().getMaKhach(),
                    ct.getKhachThue().getHoTen(),
                    ct.getVaiTro()
            ));
        }
        dto.setListKhach(listDto);
        return dto;
    }
}
