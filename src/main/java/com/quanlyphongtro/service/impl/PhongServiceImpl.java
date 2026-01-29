package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.dto.PhongDto;
import com.quanlyphongtro.models.LoaiPhong;
import com.quanlyphongtro.models.Phong;
import com.quanlyphongtro.repository.LoaiPhongRepository;
import com.quanlyphongtro.repository.PhongRepository;
import com.quanlyphongtro.service.PhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhongServiceImpl implements PhongService {
    @Autowired
    private  PhongRepository phongRepository;
    @Autowired
    private LoaiPhongRepository loaiPhongRepository;

    @Override
    public List<PhongDto> getAllPhong() {
        return phongRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PhongDto> searchPhong(String maLoai, String trangThai) {
        List<Phong> list = phongRepository.findAll();

        return list.stream()
                .filter(p -> maLoai.equals("Tất cả") || p.getLoaiPhong().getMaLoai().equals(maLoai))
                .filter(p -> trangThai.equals("Tất cả") || p.getTrangThai().equals(trangThai))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PhongDto getPhongBySoPhong(String soPhong) {
        Optional<Phong> phongOpt = phongRepository.findById(soPhong);
        return phongOpt.map(this::convertToDto).orElse(null);
    }

    @Override
    public void addPhong(PhongDto dto) {
        validatePhong(dto);
        if (phongRepository.existsById(dto.getSoPhong())) {
            throw new RuntimeException("Phòng đã tồn tại");
        }
        saveToDb(dto);
    }

    @Override
    public void updatePhong(PhongDto dto) {
        validatePhong(dto);
        if (!phongRepository.existsById(dto.getSoPhong())) {
             throw new RuntimeException("Phòng không tồn tại để cập nhật!");
        }
        saveToDb(dto);
    }

    private void saveToDb(PhongDto dto) {
        Phong phong = new Phong();
        phong.setSoPhong(dto.getSoPhong());
        phong.setTrangThai(dto.getTrangThai());
        phong.setGhiChu(dto.getGhiChu());

        LoaiPhong loai = loaiPhongRepository.findById(dto.getMaLoai())
                .orElseThrow(() -> new RuntimeException("Loại phòng không tồn tại"));
        phong.setLoaiPhong(loai);
        
        phongRepository.save(phong);
    }

    private void validatePhong(PhongDto dto) {
        if (dto.getSoPhong() == null || dto.getSoPhong().trim().isEmpty()) {
            throw new RuntimeException("Số phòng không được bỏ trống");
        }
        if (dto.getTrangThai() == null || dto.getTrangThai().trim().isEmpty()) {
            throw new RuntimeException("Trạng thái phòng không được bỏ trống");
        }
        if (dto.getMaLoai() == null || dto.getMaLoai().equals("L")) { // Basic check
             throw new RuntimeException("Cần lựa loại phòng phù hợp");
        }
    }

    @Override
    public void deletePhong(String soPhong) {
        Phong p = phongRepository.findById(soPhong).orElse(null);
        if(p != null && "Đang thuê".equals(p.getTrangThai())) {
            throw new RuntimeException("Không thể xóa phòng đang có người ở!");
        }
        phongRepository.deleteById(soPhong);
    }
    
    private PhongDto convertToDto(Phong entity) {
        PhongDto dto = new PhongDto();
        dto.setSoPhong(entity.getSoPhong());
        dto.setTrangThai(entity.getTrangThai());
        dto.setGhiChu(entity.getGhiChu());
        if(entity.getLoaiPhong() != null) {
            dto.setMaLoai(entity.getLoaiPhong().getMaLoai());
            dto.setTenLoai(entity.getLoaiPhong().getTenLoai());
            dto.setGiaTien(entity.getLoaiPhong().getGia());
        }
        return dto;
    }
}
