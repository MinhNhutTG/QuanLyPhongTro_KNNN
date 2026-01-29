package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.dto.LoaiPhongDto;
import com.quanlyphongtro.models.LoaiPhong;
import com.quanlyphongtro.repository.LoaiPhongRepository;
import com.quanlyphongtro.service.LoaiPhongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoaiPhongServiceImpl implements LoaiPhongService {

    @Autowired
    private LoaiPhongRepository loaiPhongRepository;

    @Override
    public List<LoaiPhongDto> getAllLoaiPhong() {
        return loaiPhongRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void saveLoaiPhong(LoaiPhongDto dto) {
        LoaiPhong entity = new LoaiPhong();

        // Vì MaLoai là String (Khóa chính), nếu tồn tại JPA sẽ tự Update, chưa có sẽ Insert
        entity.setMaLoai(dto.getMaLoai());
        entity.setTenLoai(dto.getTenLoai());
        entity.setGia(dto.getGia());
        entity.setSoNguoiToiDa(dto.getSoNguoiToiDa());
        loaiPhongRepository.save(entity);
    }

    @Override
    public void deleteLoaiPhong(String maLoai) {
        // Kiểm tra tồn tại trước khi xóa để tránh lỗi không mong muốn
        if (loaiPhongRepository.existsById(maLoai)) {
            loaiPhongRepository.deleteById(maLoai);
        } else {
            throw new RuntimeException("Mã loại phòng không tồn tại: " + maLoai);
        }
    }

    // Helper: Chuyển đổi Entity sang DTO
    private LoaiPhongDto convertToDto(LoaiPhong entity) {
        return new LoaiPhongDto(
                entity.getMaLoai(),
                entity.getTenLoai(),
                entity.getGia(),
                entity.getSoNguoiToiDa()
        );
    }
}