package com.quanlyphongtro.ui.Room;

import com.quanlyphongtro.config.SpringContext;
import com.quanlyphongtro.dto.LoaiPhongDto;
import com.quanlyphongtro.dto.PhongDto;
import com.quanlyphongtro.service.LoaiPhongService;
import com.quanlyphongtro.service.PhongService;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class add_edit_room extends JFrame {
    private static final long serialVersionUID = 1L;

    private final PhongService phongService;
    private final LoaiPhongService loaiPhongService;
    private final Runnable onCloseCallback;
    private boolean isEditMode = false;
    private String soPhongGoc; // ID gốc khi sửa

    // UI
    private JTextField txtSoPhong;
    private JComboBox<LoaiPhongDto> cbLoaiPhong;
    private JRadioButton rbTrong, rbDangThue, rbSuaChua;
    private JTextArea txtGhiChu;

    public add_edit_room(String soPhongCanSua, Runnable onCloseCallback) {
        this.phongService = SpringContext.getBean(PhongService.class);
        this.loaiPhongService = SpringContext.getBean(LoaiPhongService.class);
        this.onCloseCallback = onCloseCallback;

        // Logic xác định Mode
        this.isEditMode = (soPhongCanSua != null);
        this.soPhongGoc = soPhongCanSua;
        setTitle(isEditMode ? "Cập Nhật Phòng" : "Thêm Phòng Mới");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. TOOLBAR ---
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        toolbar.setBackground(new Color(191, 205, 226));
        JButton btnSave = new JButton("💾 Lưu");
        JButton btnCancel = new JButton("❌ Thoát");
        btnSave.setBackground(new Color(0, 120, 215));
        btnSave.setForeground(Color.WHITE);
        toolbar.add(btnSave);
        toolbar.add(btnCancel);
        add(toolbar, BorderLayout.NORTH);

        // --- 2. MAIN CONTENT ---
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(new Color(245, 245, 245));

        // Group: Số phòng
        JPanel pnlSoPhong = createGroupBox("Số phòng (Mã)", 40, 30, 250, 60);
        txtSoPhong = new JTextField();
        txtSoPhong.setBounds(15, 20, 220, 25);
        txtSoPhong.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)));
        if(isEditMode) {
            txtSoPhong.setEditable(false); // Khóa ID khi sửa
            txtSoPhong.setBackground(new Color(230, 230, 230));

        }
        pnlSoPhong.add(txtSoPhong);
        mainPanel.add(pnlSoPhong);

        // Group: Loại Phòng
        JPanel pnlLoaiPhong = createGroupBox("Loại Phòng", 40, 110, 250, 60);
        cbLoaiPhong = new JComboBox<>();
        cbLoaiPhong.setBounds(15, 20, 220, 25);
        loadLoaiPhongCombo();
        pnlLoaiPhong.add(cbLoaiPhong);
        mainPanel.add(pnlLoaiPhong);

        // Group: Trạng thái
        JPanel pnlTrangThai = createGroupBox("Trạng thái", 320, 30, 330, 140);
        pnlTrangThai.setLayout(new GridLayout(3, 1, 5, 5));
        rbTrong = new JRadioButton("Trống");
        rbDangThue = new JRadioButton("Đang thuê");
        rbSuaChua = new JRadioButton("Bảo trì");
        ButtonGroup groupTrangThai = new ButtonGroup();
        groupTrangThai.add(rbTrong); groupTrangThai.add(rbDangThue); groupTrangThai.add(rbSuaChua);
        rbTrong.setSelected(true);
        pnlTrangThai.add(rbTrong); pnlTrangThai.add(rbDangThue); pnlTrangThai.add(rbSuaChua);
        mainPanel.add(pnlTrangThai);

        // Group: Ghi chú
        JPanel pnlGhiChu = createGroupBox("Ghi chú", 40, 190, 610, 150);
        txtGhiChu = new JTextArea();
        txtGhiChu.setBorder(new LineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollGhiChu = new JScrollPane(txtGhiChu);
        scrollGhiChu.setBounds(15, 25, 580, 110);
        pnlGhiChu.add(scrollGhiChu);
        mainPanel.add(pnlGhiChu);

        add(mainPanel, BorderLayout.CENTER);

        // Nếu là Edit thì load data
        if (isEditMode) {
            loadDataToForm(soPhongCanSua);
        }

        // Events
        btnSave.addActionListener(e -> saveData());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadLoaiPhongCombo() {
        List<LoaiPhongDto> list = loaiPhongService.getAllLoaiPhong();
        for (LoaiPhongDto lp : list) {
            cbLoaiPhong.addItem(lp);
        }
    }

    private void loadDataToForm(String soPhong) {
        PhongDto dto = phongService.getPhongBySoPhong(soPhong);
        if (dto != null) {
            txtSoPhong.setText(dto.getSoPhong());
            txtGhiChu.setText(dto.getGhiChu());

            // Check null để tránh lỗi nếu DTO thiếu dữ liệu
            if (dto.getMaLoai() != null) {
                for (int i = 0; i < cbLoaiPhong.getItemCount(); i++) {
                    LoaiPhongDto item = cbLoaiPhong.getItemAt(i);
                    // So sánh mã loại
                    if (item.getMaLoai().equals(dto.getMaLoai())) {
                        cbLoaiPhong.setSelectedIndex(i);
                        break;
                    }
                }
            }

            String tt = dto.getTrangThai();
            if ("Đang thuê".equals(tt)) rbDangThue.setSelected(true);
            else if ("Bảo trì".equals(tt)) rbSuaChua.setSelected(true);
            else rbTrong.setSelected(true);
        }
    }

    private void saveData() {
        try {



            PhongDto dto = new PhongDto();
            String soPhong = soPhongGoc;
            // Validate sơ bộ
            if(!isEditMode){
                if(txtSoPhong.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Số phòng không được để trống!");
                    return;
                }else{
                    soPhong = txtSoPhong.getText().trim();
                }
            }
            dto.setSoPhong(soPhong);
            dto.setGhiChu(txtGhiChu.getText());

            LoaiPhongDto selectedType = (LoaiPhongDto) cbLoaiPhong.getSelectedItem();
            if (selectedType != null) {
                dto.setMaLoai(selectedType.getMaLoai());
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn loại phòng!");
                return;
            }

            if (rbDangThue.isSelected()) dto.setTrangThai("Đang thuê");
            else if (rbSuaChua.isSelected()) dto.setTrangThai("Bảo trì");
            else dto.setTrangThai("Trống");

            if (isEditMode) {
                phongService.updatePhong(dto);
            } else {
                phongService.addPhong(dto);
            }

            JOptionPane.showMessageDialog(this, isEditMode ? "Cập nhật thành công!" : "Thêm mới thành công!");

            // Gọi callback để refresh bảng cha
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
            dispose(); // Đóng form
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private JPanel createGroupBox(String title, int x, int y, int width, int height) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), title);
        border.setTitleFont(new Font("Arial", Font.PLAIN, 14));
        panel.setBorder(border);
        return panel;
    }
}