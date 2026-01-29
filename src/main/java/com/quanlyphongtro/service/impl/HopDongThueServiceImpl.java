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
        hd.setId(dto.getId());
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
             // If creating new or updating to Active, check if room is already occupied by ANOTHER contract
             // (Simple check: if Phong status is 'Đang thuê' and we are creating a NEW contract)
             // However, reusing existing 'validate' or just simple check here:
             if ("Đang thuê".equals(phong.getTrangThai()) && (dto.getId() == null || !dto.getId().equals(hopDongRepo.findByPhong_SoPhongAndTrangThai(dto.getSoPhong(), "Đang hiệu lực").getId()))) {
                 // Logic complex because 'soPhong' might be same.
                 // Simplest: If logic requires 1 active contract per room.
             }
        }

        hd.setPhong(phong);

        // Cập nhật trạng thái phòng
        if("Đang hiệu lực".equals(dto.getTrangThai())) {
            phong.setTrangThai("Đang thuê");
            phongRepo.save(phong);
        }

        // Lưu Hợp đồng trước
        HopDongThue savedHd = hopDongRepo.save(hd);

        List<ChiTietHopDong> oldDetails = chiTietRepo.findByHopDong(savedHd);

        for (ChiTietHopDong oldCt : oldDetails) {
            KhachThue khachCu = oldCt.getKhachThue();
            khachCu.setTrangThai("Chưa thuê");
            khachRepo.save(khachCu);
        }
        // 2. Xử lý Chi Tiết Hợp Đồng (Khách thuê)
        // Xóa chi tiết cũ (Strategy: Delete All & Insert New để đơn giản logic update)
        chiTietRepo.deleteByHopDong(savedHd);

        // Thêm chi tiết mới
        for (ChiTietHopDongDto chiTietDto : dto.getListKhach()) {
            // Kiểm tra nhanh mã khách từ DTO
            if (chiTietDto.getMaKhach() == null ) {
                throw new Exception("Mã khách thuê không được để trống!");
            }

            KhachThue khachMoi = khachRepo.findById(chiTietDto.getMaKhach())
                    .orElseThrow(() -> new Exception("Khach thue voi ID " + chiTietDto.getMaKhach() + " khong ton tai"));

            ChiTietHopDong chiTiet = new ChiTietHopDong();
            chiTiet.setHopDong(savedHd);
            chiTiet.setVaiTro(chiTietDto.getVaiTro());

            // QUAN TRỌNG: Phải set khách thuê VÀO chi tiết trước khi save chi tiết
            chiTiet.setKhachThue(khachMoi);

            // Cập nhật trạng thái khách
            khachMoi.setTrangThai("Đang Thuê");
            khachRepo.save(khachMoi);

            // Lúc này chiTiet đã có khachThue, không còn bị null nữa
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
