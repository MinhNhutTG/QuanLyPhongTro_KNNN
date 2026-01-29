package com.quanlyphongtro.service;

import com.quanlyphongtro.dto.LoaiPhongDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoaiPhongService {
    List<LoaiPhongDto> getAllLoaiPhong();
    void saveLoaiPhong(LoaiPhongDto dto);
    void deleteLoaiPhong(String maLoai);
}
