package com.quanlyphongtro.service.impl;

import com.quanlyphongtro.models.DichVu;
import com.quanlyphongtro.repository.DichVuRepository;
import com.quanlyphongtro.service.DichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DichVuServiceImpl implements DichVuService {

    @Autowired
    private DichVuRepository dichVuRepository;

    @Override
    public List<DichVu> getAllDichVu() {
        return dichVuRepository.findAll();
    }

    @Override
    public DichVu saveDichVu(DichVu dichVu) {
        return dichVuRepository.save(dichVu);
    }

    @Override
    public void deleteDichVu(Long id) {
        dichVuRepository.deleteById(id);
    }
}
