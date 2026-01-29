package com.quanlyphongtro.ui.Contract;

import com.quanlyphongtro.config.SpringContext;
import com.quanlyphongtro.dto.*;
import com.quanlyphongtro.service.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class add_edit_contract extends JFrame {

    private static final long serialVersionUID = 1L;
    private ContractMode mode;
    private JButton btnSave; // đưa btnSave thành biến global

    // Services
    private final HopDongThueSevice hopDongService;
    private final KhachThueService khachThueService;
    private final PhongService phongService;
    private final Runnable callback; // Callback để reload bảng cha
    private boolean isEditMode = false;

    // UI Components
    private JTable tableTop; // Danh sách tất cả khách (Source)
    private DefaultTableModel modelTop;
    private JTable tableBottom; // Danh sách khách đã chọn (Destination)
    private DefaultTableModel modelBottom;

    private JTextField txtMaHD;
    private JComboBox<String> cbTrangThai;
    private JTextField txtNgayTao, txtNgayThue, txtHanThue;
    private JComboBox<String> cbSoPhong;
    private JLabel lblLoaiPhong;
    private JTextField txtGiaThue;

    // Data list
    private List<ChiTietHopDongDto> selectedTenants = new ArrayList<>();

    public add_edit_contract(String contractId, ContractMode mode, Runnable callback) {
        this.mode = mode;
        this.callback = callback;

        this.hopDongService = SpringContext.getBean(HopDongThueSevice.class);
        this.khachThueService = SpringContext.getBean(KhachThueService.class);
        this.phongService = SpringContext.getBean(PhongService.class);

        this.isEditMode = (mode == ContractMode.EDIT);

        setTitle(
                mode == ContractMode.ADD ? "Thêm Hợp Đồng Mới" :
                        mode == ContractMode.EDIT ? "Cập Nhật Hợp Đồng" :
                                "Xem Chi Tiết Hợp Đồng"
        );

        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initLeftPanel();
        initRightPanel();

        loadAllKhachThue();
        loadAllPhong();

        if (mode != ContractMode.ADD) {
            loadContractData(contractId);
        }

        if (mode == ContractMode.VIEW) {
            applyViewMode();
        }
    }


    // --- PANEL TRÁI: QUẢN LÝ KHÁCH THUÊ ---
    private void initLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        leftPanel.setPreferredSize(new Dimension(400, 0));

        // 1. Bảng Source (Tất cả khách)
        String[] cols1 = {"Mã", "Tên Khách", "CCCD","Trạng Thái"};
        modelTop = new DefaultTableModel(cols1, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTop = new JTable(modelTop);
        JPanel pnlTable1 = createTitledPanel("Danh sách khách có sẵn", new JScrollPane(tableTop));

        // 2. Nút điều hướng
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("⬇ Thêm vào HĐ");
        JButton btnRemove = new JButton("⬆ Loại bỏ");
        btnPanel.add(btnAdd);
        btnPanel.add(btnRemove);

        // 3. Bảng Destination (Khách đã chọn)
        String[] cols2 = {"Mã", "Tên Khách", "Vai Trò"};
        modelBottom = new DefaultTableModel(cols2, 0) {
            // Cho phép sửa cột "Vai trò"
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
        };
        tableBottom = new JTable(modelBottom);
        JPanel pnlTable2 = createTitledPanel("Khách trong hợp đồng (Sửa vai trò tại đây)", new JScrollPane(tableBottom));

        leftPanel.add(pnlTable1);
        leftPanel.add(btnPanel);
        leftPanel.add(pnlTable2);
        add(leftPanel, BorderLayout.WEST);

        // --- SỰ KIỆN NÚT CHUYỂN DANH SÁCH ---
        btnAdd.addActionListener(e -> {
            int row = tableTop.getSelectedRow();
            if (row >= 0) {
                Integer maKhach = (Integer) modelTop.getValueAt(row, 0);
                String tenKhach = (String) modelTop.getValueAt(row, 1);

                // Check trùng
                for (ChiTietHopDongDto dto : selectedTenants) {
                    if (dto.getMaKhach().equals(maKhach)) return;
                }

                // Thêm vào list & table dưới
                ChiTietHopDongDto newGuest = new ChiTietHopDongDto(maKhach, tenKhach, "Thành viên");
                selectedTenants.add(newGuest);
                modelBottom.addRow(new Object[]{maKhach, tenKhach, "Thành viên"});
                normalizeRoles();
            }
        });

        btnRemove.addActionListener(e -> {
            int row = tableBottom.getSelectedRow();
            if (row >= 0) {
                Integer maKhach = (Integer) modelBottom.getValueAt(row, 0);
                selectedTenants.removeIf(dto -> dto.getMaKhach().equals(maKhach));
                modelBottom.removeRow(row);
            }
            normalizeRoles();
        });
    }

    // Thay thế đoạn initRightPanel cũ bằng đoạn này
    private void initRightPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(new TitledBorder("Thông tin chi tiết"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Khởi tạo các Component
        txtMaHD = new JTextField();
        if (isEditMode) txtMaHD.setEditable(false);

        cbTrangThai = new JComboBox<>(new String[]{"Đang hiệu lực", "Đã thanh lý", "Quá hạn"});
        txtNgayTao = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtNgayThue = new JTextField();
        txtHanThue = new JTextField();

        cbSoPhong = new JComboBox<>();
        lblLoaiPhong = new JLabel("---");

        txtGiaThue = new JTextField();
        txtGiaThue.setEditable(false); // Không cho nhập tay
        txtGiaThue.setBackground(new Color(245, 245, 245));

        // --- SỰ KIỆN QUAN TRỌNG: Chọn phòng -> Load giá ---
        cbSoPhong.addActionListener(e -> {
            String soPhong = (String) cbSoPhong.getSelectedItem();
            // System.out.println("Đang chọn phòng: " + soPhong); // Bật dòng này để debug

            if (soPhong != null && !soPhong.isEmpty()) {
                PhongDto p = phongService.getPhongBySoPhong(soPhong);

                if (p != null) {
                    // 1. Set Loại phòng
                    String tenLoai = p.getTenLoai() != null ? p.getTenLoai() : "Chưa cập nhật";
                    lblLoaiPhong.setText(tenLoai);

                    // 2. Set Giá phòng
                    if (p.getGiaTien() != null) {
                        // Format số nguyên (bỏ .00 nếu là BigDecimal)
                        txtGiaThue.setText(String.format("%.0f", p.getGiaTien()));
                    } else {
                        txtGiaThue.setText("0");
                    }
                } else {
                    // System.out.println("Không tìm thấy thông tin phòng!");
                }
            }
        });

        // Layout Components
        addFormItem(rightPanel, gbc, 0, "Mã Hợp Đồng:", txtMaHD);
        addFormItem(rightPanel, gbc, 1, "Trạng Thái:", cbTrangThai);
        addFormItem(rightPanel, gbc, 2, "Ngày Tạo (dd/MM/yyyy):", txtNgayTao);
        addFormItem(rightPanel, gbc, 3, "Ngày Thuê (dd/MM/yyyy):", txtNgayThue);
        addFormItem(rightPanel, gbc, 4, "Hạn Thuê (dd/MM/yyyy):", txtHanThue);
        addFormItem(rightPanel, gbc, 5, "Số Phòng:", cbSoPhong);
        addFormItem(rightPanel, gbc, 6, "Loại Phòng:", lblLoaiPhong);
        addFormItem(rightPanel, gbc, 7, "Giá Thuê (VND):", txtGiaThue);

        btnSave = new JButton("Lưu Hợp Đồng");
        btnSave.setBackground(new Color(21, 115, 71));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(150, 40));
        btnSave.addActionListener(e -> saveContract());

        if (mode == ContractMode.VIEW) {
            btnSave.setVisible(false);
        }


        gbc.gridx = 1; gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(btnSave, gbc);
        add(rightPanel, BorderLayout.CENTER);
    }

    // --- LOGIC LOAD DATA ---
    private void loadAllKhachThue() {
        List<KhachThueDto> list = khachThueService.getAllKhachThue();
        modelTop.setRowCount(0);
        for(KhachThueDto k : list) {
            modelTop.addRow(new Object[]{k.getMaKhach(), k.getHoTen(), k.getCccd(),k.getTrangThai()});
        }
    }
    private void applyViewMode() {
        txtMaHD.setEditable(false);
        cbTrangThai.setEnabled(false);
        txtNgayTao.setEditable(false);
        txtNgayThue.setEditable(false);
        txtHanThue.setEditable(false);
        cbSoPhong.setEnabled(false);

        // Bảng khách thuê
        tableTop.setEnabled(false);
        tableBottom.setEnabled(false);
    }

    private void loadAllPhong() {
        List<PhongDto> list = phongService.getAllPhong();
        cbSoPhong.removeAllItems();
        for(PhongDto p : list) {
            // Load phòng trống hoặc phòng của hợp đồng đang sửa
            if ("Trống".equals(p.getTrangThai()) || isEditMode) {
                cbSoPhong.addItem(p.getSoPhong());
            }
        }
    }
    private void normalizeRoles() {
        if (selectedTenants.size() == 1) {
            // Chỉ còn 1 khách → auto Khách chính
            selectedTenants.get(0).setVaiTro("Khách chính");
            modelBottom.setValueAt("Khách chính", 0, 2);
            return;
        }

        // Nếu >= 2 khách → đảm bảo chỉ có 1 Khách chính
        boolean hasMain = selectedTenants.stream()
                .anyMatch(ct -> "Khách chính".equals(ct.getVaiTro()));

        if (!hasMain && !selectedTenants.isEmpty()) {
            selectedTenants.get(0).setVaiTro("Khách chính");
            modelBottom.setValueAt("Khách chính", 0, 2);
        }
    }
    private void loadContractData(String id) {
        HopDongDto dto = hopDongService.getHopDongById(id);
        if(dto != null) {
            txtMaHD.setText(dto.getId());
            cbTrangThai.setSelectedItem(dto.getTrangThai());

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtNgayTao.setText(dto.getNgayTao().format(fmt));
            txtNgayThue.setText(dto.getNgayThue().format(fmt));
            txtHanThue.setText(dto.getHanThue().format(fmt));

            // Việc setSelectedItem sẽ kích hoạt ActionEvent ở trên
            // Giá phòng sẽ được load theo giá hiện tại của phòng
            cbSoPhong.setSelectedItem(dto.getSoPhong());

            // Nếu muốn giữ giá cũ của hợp đồng (trong trường hợp giá phòng đã tăng sau khi ký)
            // thì phải set lại text giá sau khi set item combo box
            if (dto.getGiaPhong() != null) {
                txtGiaThue.setText(String.format("%.0f", dto.getGiaPhong()));
            }

            selectedTenants = dto.getListKhach();
            modelBottom.setRowCount(0);
            for(ChiTietHopDongDto ct : selectedTenants) {
                modelBottom.addRow(new Object[]{ct.getMaKhach(), ct.getTenKhach(), ct.getVaiTro()});
            }
        }
    }

    private void saveContract() {
        try {
            if(selectedTenants.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Hợp đồng phải có ít nhất 1 khách thuê!");
                return;
            }

            HopDongDto dto = new HopDongDto();
            dto.setId(txtMaHD.getText());
            dto.setSoPhong((String) cbSoPhong.getSelectedItem());
            dto.setTrangThai((String) cbTrangThai.getSelectedItem());

            // Xử lý giá thuê (đã loại bỏ dấu phẩy nếu có)
            String giaStr = txtGiaThue.getText().replace(",", "").trim();
            dto.setGiaPhong(giaStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(giaStr));

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dto.setNgayTao(LocalDate.parse(txtNgayTao.getText(), fmt));
            dto.setNgayThue(LocalDate.parse(txtNgayThue.getText(), fmt));
            dto.setHanThue(LocalDate.parse(txtHanThue.getText(), fmt));

            // Cập nhật vai trò từ bảng
            for(int i=0; i<modelBottom.getRowCount(); i++) {
                Integer mk = (Integer) modelBottom.getValueAt(i, 0);
                String role = (String) modelBottom.getValueAt(i, 2);
                for(ChiTietHopDongDto ct : selectedTenants) {
                    if(ct.getMaKhach().equals(mk)) {
                        ct.setVaiTro(role);
                    }
                }
            }
            dto.setListKhach(selectedTenants);


            hopDongService.saveHopDong(dto);

            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            if(callback != null) callback.run();
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private JPanel createTitledPanel(String title, JComponent component) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(title));
        p.add(component);
        return p;
    }

    private void addFormItem(JPanel p, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = row;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        p.add(comp, gbc);
    }
}