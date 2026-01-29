package com.quanlyphongtro.service;

import com.quanlyphongtro.dto.HopDongDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HopDongThueSevice {
    List<HopDongDto> getAllHopDong();
    List<HopDongDto> getHopDongByStatus(String status);
    HopDongDto getHopDongById(String id);
    void saveHopDong(HopDongDto dto) throws Exception;
    void deleteHopDong(String id);
}
