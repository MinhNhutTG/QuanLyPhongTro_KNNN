package com.quanlyphongtro.ui.Invoice;

import com.quanlyphongtro.models.HoaDon;
import com.quanlyphongtro.dto.HoaDonDto;
import com.quanlyphongtro.models.DichVuPhong;
import com.quanlyphongtro.models.Phong;
import com.quanlyphongtro.service.HoaDonService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class AddEditInvoice extends JDialog {

    private HoaDonService hoaDonService;
    private com.quanlyphongtro.service.HopDongThueSevice hopDongService;
    private com.quanlyphongtro.service.DichVuPhongService dichVuPhongService;
    private com.quanlyphongtro.service.PhongService phongService;
    private com.quanlyphongtro.dto.HoaDonDto currentHoaDon;
    private Runnable onSuccess;

    private JTextField txtMaHD, txtGiaPhong, txtSoDien, txtThanhTienDien, txtSoNuoc, txtThanhTienNuoc, txtTienMang, txtTienKhac;
    private JTextField txtSoDienCu, txtSoDienMoi, txtSoNuocCu, txtSoNuocMoi;
    private JComboBox<String> cbxTrangThai;
    private JComboBox<String> cbxPhong;
    private JSpinner spinnerNgayLap;
    private JTextArea txtGhiChu;
    private JLabel lblTongTien;
    
    // UI Labels for Refinement
    private JLabel lblLoaiPhong;
    private JLabel lblMaLichSu;

    private JTable tblHistory;
    private DefaultTableModel modelHistory;
    // Dịch vụ List for the current room
    private List<com.quanlyphongtro.dto.DichVuPhongDto> currentRoomServices;
    private com.quanlyphongtro.dto.DichVuPhongDto selectedDichVu;
    
    private JRadioButton rdTienPhong;
    private JRadioButton rdDichVu;

    // Helper to store contracts for room selection
    private List<com.quanlyphongtro.dto.HopDongDto> activeContracts;

    public AddEditInvoice(Frame parent, HoaDonService hoaDonService, com.quanlyphongtro.service.HopDongThueSevice hdService, com.quanlyphongtro.service.DichVuPhongService dvService, com.quanlyphongtro.service.PhongService pService, com.quanlyphongtro.dto.HoaDonDto hoaDon, Runnable onSuccess) {
        super(parent, "Bill", true);
        this.hoaDonService = hoaDonService;
        this.hopDongService = hdService;
        this.dichVuPhongService = dvService;
        this.phongService = pService;
        this.currentHoaDon = hoaDon;
        this.onSuccess = onSuccess;

        setSize(900, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null); // Using Absolute layout as per complex requirements or nested layouts
        mainPanel.setBackground(new Color(245, 245, 245));
        add(mainPanel, BorderLayout.CENTER);

        // --- TOP: MODE SELECTION ---
        rdTienPhong = new JRadioButton("Tính hóa đơn tiền phòng", true);
        rdDichVu = new JRadioButton("Tính hóa đơn dịch vụ khác");
        ButtonGroup bg = new ButtonGroup(); bg.add(rdTienPhong); bg.add(rdDichVu);
        rdTienPhong.setBounds(50, 10, 200, 30); rdTienPhong.setOpaque(false);
        rdDichVu.setBounds(300, 10, 250, 30); rdDichVu.setOpaque(false);
        mainPanel.add(rdTienPhong); mainPanel.add(rdDichVu);

        // --- LEFT COLUMN ---
        
        // Group: Hóa đơn
        JPanel pnlInfo = createGroupPanel("Hóa đơn", 50, 50, 350, 140);
        mainPanel.add(pnlInfo);
        
        addLabelAndField(pnlInfo, "Mã hóa đơn", 10, 30, txtMaHD = new JTextField());
        txtMaHD.setEditable(false);
        JButton btnReloadID = new JButton("↻");
        btnReloadID.setBounds(300, 30, 40, 25);
        pnlInfo.add(btnReloadID);

        addLabelAndComponent(pnlInfo, "Trạng thái", 10, 65, cbxTrangThai = new JComboBox<>(new String[]{"Chưa Thanh Toán", "Đã Thanh Toán", "Đã Hủy"}));

        addLabelAndComponent(pnlInfo, "Ngày lập", 10, 100, spinnerNgayLap = new JSpinner(new SpinnerDateModel()));
        spinnerNgayLap.setEditor(new JSpinner.DateEditor(spinnerNgayLap, "dd/MM/yyyy"));

        // Group: Chọn phòng
        JPanel pnlRoom = createGroupPanel("Chọn phòng", 50, 210, 350, 70);
        mainPanel.add(pnlRoom);
        addLabelAndComponent(pnlRoom, "Phòng", 10, 30, cbxPhong = new JComboBox<>());

        // Group: Lịch sử (Service History Table)
        JPanel pnlHistory = createGroupPanel("Lịch sử sử dụng dịch vụ", 50, 300, 350, 200);
        mainPanel.add(pnlHistory);
        
        String[] historyCols = {"Mã DV", "Kì", "Ngày tạo"};
        modelHistory = new DefaultTableModel(historyCols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblHistory = new JTable(modelHistory);
        tblHistory.setRowHeight(25);
        tblHistory.getSelectionModel().addListSelectionListener(e -> {
             if (!e.getValueIsAdjusting() && tblHistory.getSelectedRow() != -1) {
                 fillServiceDetails(tblHistory.getSelectedRow());
             }
        });
        
        JScrollPane scrollHistory = new JScrollPane(tblHistory);
        scrollHistory.setBounds(10, 20, 330, 170);
        pnlHistory.add(scrollHistory);

        // Group: Ghi chú
        JLabel lblNote = new JLabel("Ghi chú");
        lblNote.setBounds(50, 510, 100, 20);
        mainPanel.add(lblNote);
        txtGhiChu = new JTextArea();
        txtGhiChu.setBorder(new LineBorder(Color.GRAY));
        txtGhiChu.setBounds(50, 530, 350, 60);
        mainPanel.add(txtGhiChu);

        // --- RIGHT COLUMN ---

        // Group: Phòng
        JPanel pnlPhongInfo = createGroupPanel("Phòng", 450, 50, 380, 80);
        mainPanel.add(pnlPhongInfo);
        lblLoaiPhong = new JLabel("Loại phòng: ..."); 
        lblLoaiPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLoaiPhong.setBounds(40, 20, 300, 20); pnlPhongInfo.add(lblLoaiPhong);
        addLabelAndField(pnlPhongInfo, "Giá tiền:", 40, 45, txtGiaPhong = new JTextField());

        // Group: Dịch vụ đã sử dụng
        JPanel pnlServiceUsed = createGroupPanel("Dịch vụ đã sử dụng", 450, 140, 380, 50);
        mainPanel.add(pnlServiceUsed);
        lblMaLichSu = new JLabel("Mã lịch sử: ...");
        lblMaLichSu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMaLichSu.setBounds(60, 20, 200, 20);
        pnlServiceUsed.add(lblMaLichSu);
        
        // Group: Dịch vụ điện
        JPanel pnlElec = createGroupPanel("Dịch vụ điện", 450, 200, 380, 120);
        mainPanel.add(pnlElec);
        
        JLabel lblDienCu = new JLabel("Số cũ:"); lblDienCu.setBounds(40, 20, 50, 25); pnlElec.add(lblDienCu);
        txtSoDienCu = new JTextField(); txtSoDienCu.setBounds(80, 20, 60, 25); pnlElec.add(txtSoDienCu);
        
        JLabel lblDienMoi = new JLabel("Số mới:"); lblDienMoi.setBounds(160, 20, 50, 25); pnlElec.add(lblDienMoi);
        txtSoDienMoi = new JTextField(); txtSoDienMoi.setBounds(210, 20, 60, 25); pnlElec.add(txtSoDienMoi);

        addLabelAndField(pnlElec, "Số kí điện:", 40, 55, txtSoDien = new JTextField());
        txtSoDien.setEditable(false);
        addLabelAndField(pnlElec, "Thành tiền:", 40, 85, txtThanhTienDien = new JTextField());
        txtThanhTienDien.setForeground(Color.RED);

        // Group: Dịch vụ nước
        JPanel pnlWater = createGroupPanel("Dịch vụ nước", 450, 330, 380, 120);
        mainPanel.add(pnlWater);
        
        JLabel lblNuocCu = new JLabel("Số cũ:"); lblNuocCu.setBounds(40, 20, 50, 25); pnlWater.add(lblNuocCu);
        txtSoNuocCu = new JTextField(); txtSoNuocCu.setBounds(80, 20, 60, 25); pnlWater.add(txtSoNuocCu);
        
        JLabel lblNuocMoi = new JLabel("Số mới:"); lblNuocMoi.setBounds(160, 20, 50, 25); pnlWater.add(lblNuocMoi);
        txtSoNuocMoi = new JTextField(); txtSoNuocMoi.setBounds(210, 20, 60, 25); pnlWater.add(txtSoNuocMoi);

        addLabelAndField(pnlWater, "Số khối nước:", 40, 55, txtSoNuoc = new JTextField());
        txtSoNuoc.setEditable(false);
        addLabelAndField(pnlWater, "Thành tiền:", 40, 85, txtThanhTienNuoc = new JTextField());
        txtThanhTienNuoc.setForeground(Color.RED);

        // Group: Dịch vụ khác
        JPanel pnlOther = createGroupPanel("Các dịch vụ khác", 450, 460, 380, 100);
        mainPanel.add(pnlOther);
        addLabelAndField(pnlOther, "Tiền mạng :", 40, 30, txtTienMang = new JTextField());
        addLabelAndField(pnlOther, "Tiền dịch vụ khác:", 40, 60, txtTienKhac = new JTextField());

        // --- BOTTOM ACTIONS ---
        JButton btnReloadTotal = new JButton("↺");
        btnReloadTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnReloadTotal.setBounds(480, 570, 50, 40);
        mainPanel.add(btnReloadTotal);

        lblTongTien = new JLabel("Tổng tiền : 0 VND");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(Color.RED);
        lblTongTien.setBounds(550, 570, 200, 40);
        mainPanel.add(lblTongTien);

        JButton btnSave = new JButton("Lưu");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.setBounds(760, 570, 80, 40);
        mainPanel.add(btnSave);

        // --- EVENTS ---
        cbxPhong.addActionListener(e -> {
             if (cbxPhong.getSelectedItem() != null) {
                 loadServiceHistory((String)cbxPhong.getSelectedItem());
                 // Get Room Price
                 if (rdTienPhong.isSelected()) {
                     updateRoomPrice((String)cbxPhong.getSelectedItem());
                 }
             }
        });
        
        rdTienPhong.addActionListener(e -> {
            if (cbxPhong.getSelectedItem() != null) updateRoomPrice((String)cbxPhong.getSelectedItem());
            calculate();
        });
        
        rdDichVu.addActionListener(e -> {
            txtGiaPhong.setText("0");
            calculate();
        });

        loadRoomData();
        if (currentHoaDon != null) {
            fillData();
        } else {
             // New Invoice logic: HD + 8 random digits
             txtMaHD.setText(generateShortID());
        }

        btnReloadID.addActionListener(e -> txtMaHD.setText(generateShortID()));
        btnReloadTotal.addActionListener(e -> calculate());
        btnSave.addActionListener(e -> save());
    }

    private String generateShortID() {
        StringBuilder sb = new StringBuilder("HD");
        java.util.Random rand = new java.util.Random();
        for (int i = 0; i < 8; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    private JPanel createGroupPanel(String title, int x, int y, int w, int h) {
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds(x, y, w, h);
        p.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            title, 
            TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, 
            new Font("Segoe UI", Font.BOLD, 12), 
            new Color(40, 167, 69) // Green title
        ));
        p.setBackground(Color.WHITE);
        return p;
    }

    private void addLabelAndField(JPanel p, String text, int x, int y, JTextField field) {
        JLabel l = new JLabel(text);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setBounds(x, y, 100, 25);
        field.setBounds(x + 110, y, 180, 25);
        p.add(l);
        p.add(field);
    }

    private void addLabelAndComponent(JPanel p, String text, int x, int y, JComponent comp) {
        JLabel l = new JLabel(text);
        l.setHorizontalAlignment(SwingConstants.RIGHT);
        l.setBounds(x, y, 100, 25);
        comp.setBounds(x + 110, y, 180, 25);
        p.add(l);
        p.add(comp);
    }
    
    private void loadRoomData() {
        // Load active contracts to populate room combobox
        activeContracts = hopDongService.getHopDongByStatus("Đang thuê");
        if (activeContracts.isEmpty()) { 
             activeContracts = hopDongService.getHopDongByStatus("Đang hiệu lực"); 
        }
        for (com.quanlyphongtro.dto.HopDongDto hd : activeContracts) {
            cbxPhong.addItem(hd.getSoPhong());
        }
    }

    private void calculate() {
        try {
            // Electric
            int oldElec = txtSoDienCu.getText().isEmpty() ? 0 : Integer.parseInt(txtSoDienCu.getText());
            int newElec = txtSoDienMoi.getText().isEmpty() ? 0 : Integer.parseInt(txtSoDienMoi.getText());
            int usageElec = Math.max(0, newElec - oldElec);
            txtSoDien.setText(String.valueOf(usageElec));
            BigDecimal priceElec = new BigDecimal(4000); // Should fetch via service/config
            BigDecimal totalElec = priceElec.multiply(new BigDecimal(usageElec));
            txtThanhTienDien.setText(formatMoney(totalElec));

            // Water
            int oldWater = txtSoNuocCu.getText().isEmpty() ? 0 : Integer.parseInt(txtSoNuocCu.getText());
            int newWater = txtSoNuocMoi.getText().isEmpty() ? 0 : Integer.parseInt(txtSoNuocMoi.getText());
            int usageWater = Math.max(0, newWater - oldWater);
            txtSoNuoc.setText(String.valueOf(usageWater));
            BigDecimal priceWater = new BigDecimal(12000); 
            BigDecimal totalWater = priceWater.multiply(new BigDecimal(usageWater));
            txtThanhTienNuoc.setText(formatMoney(totalWater));

            // Room Price
            BigDecimal roomPrice = new BigDecimal(txtGiaPhong.getText().isEmpty() ? "0" : txtGiaPhong.getText().replace(",", ""));
            
            // Other
            BigDecimal netPrice = new BigDecimal(txtTienMang.getText().isEmpty() ? "0" : txtTienMang.getText().replace(",", ""));
            BigDecimal otherPrice = new BigDecimal(txtTienKhac.getText().isEmpty() ? "0" : txtTienKhac.getText().replace(",", ""));

            BigDecimal grandTotal = roomPrice.add(totalElec).add(totalWater).add(netPrice).add(otherPrice);
            lblTongTien.setText("Tổng tiền : " + formatMoney(grandTotal) + " VND");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tính toán: " + ex.getMessage());
        }
    }

    private void save() {
        try {
            calculate(); // Ensure calculated
            
            if (currentHoaDon == null) currentHoaDon = new com.quanlyphongtro.dto.HoaDonDto();
            currentHoaDon.setIdHoaDon(txtMaHD.getText());
            currentHoaDon.setTrangThai((String)cbxTrangThai.getSelectedItem());
            currentHoaDon.setNgayLapHoaDon(((Date)spinnerNgayLap.getValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            currentHoaDon.setGhiChu(txtGhiChu.getText());
            
            // Map calculated fields to DTO
            currentHoaDon.setSoDien(Integer.parseInt(txtSoDien.getText().isEmpty() ? "0" : txtSoDien.getText()));
            currentHoaDon.setSoNuoc(Integer.parseInt(txtSoNuoc.getText().isEmpty() ? "0" : txtSoNuoc.getText()));
            currentHoaDon.setTienDien(parseSafely(txtThanhTienDien.getText()));
            currentHoaDon.setTienNuoc(parseSafely(txtThanhTienNuoc.getText()));
            currentHoaDon.setGiaPhong(parseSafely(txtGiaPhong.getText()));
            currentHoaDon.setPhiKhac(parseSafely(txtTienKhac.getText()));
            // ... set other fields
            
            // Link DichVuPhong
            if (this.selectedDichVu != null) {
                currentHoaDon.setDichVuPhong(this.selectedDichVu);
            }
            
            hoaDonService.saveHoaDon(currentHoaDon);
            
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            if (onSuccess != null) onSuccess.run();
            // dispose();
            setVisible(false);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace(); // Log stack trace
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu: " + (ex.getMessage() != null ? ex.getMessage() : ex.toString()));
        }
    }

    private void fillData() {
        txtMaHD.setText(currentHoaDon.getIdHoaDon());
        cbxTrangThai.setSelectedItem(currentHoaDon.getTrangThai());
        if (currentHoaDon.getNgayLapHoaDon() != null)
             spinnerNgayLap.setValue(Date.from(currentHoaDon.getNgayLapHoaDon().atZone(ZoneId.systemDefault()).toInstant()));
        
        txtGiaPhong.setText(formatMoney(currentHoaDon.getGiaPhong()));
        
        // Fill from nested DTO
        if (currentHoaDon.getDichVuPhong() != null) {
            com.quanlyphongtro.dto.DichVuPhongDto dv = currentHoaDon.getDichVuPhong();
            cbxPhong.setSelectedItem(dv.getSoPhong());
            
            txtSoDienCu.setText(String.valueOf(dv.getSoDienCu()));
            txtSoDienMoi.setText(String.valueOf(dv.getSoDienMoi()));
            txtSoNuocCu.setText(String.valueOf(dv.getSoNuocCu()));
            txtSoNuocMoi.setText(String.valueOf(dv.getSoNuocMoi()));
            txtTienMang.setText(formatMoney(dv.getTienMang()));
        }
        
        txtTienKhac.setText(formatMoney(currentHoaDon.getPhiKhac()));
        calculate();
    }
    
    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0";
        return new DecimalFormat("#,###").format(amount);
    }

    private void loadServiceHistory(String room) {
        if (dichVuPhongService == null) return;
        currentRoomServices = dichVuPhongService.getByRoom(room);
        modelHistory.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (currentRoomServices != null) {
            for (com.quanlyphongtro.dto.DichVuPhongDto dv : currentRoomServices) {
                modelHistory.addRow(new Object[]{
                    dv.getId(),
                    dv.getKi(),
                    dv.getNgayTao() != null ? dv.getNgayTao().format(dtf) : ""
                });
            }
        }
    }

    private void updateRoomPrice(String room) {
        if (phongService != null) {
             com.quanlyphongtro.dto.PhongDto p = phongService.getPhongBySoPhong(room);
             if (p != null) {
                 lblLoaiPhong.setText("Loại phòng: " + p.getTenLoai());
             }
        }
    
        if (activeContracts == null) return;
        txtGiaPhong.setText("0");
        for (com.quanlyphongtro.dto.HopDongDto hd : activeContracts) {
            if (hd.getSoPhong().equals(room)) {
                txtGiaPhong.setText(new DecimalFormat("#").format(hd.getGiaPhong()));
                return;
            }
        }
    }

    private void fillServiceDetails(int row) {
        if (currentRoomServices == null || row >= currentRoomServices.size()) return;
        com.quanlyphongtro.dto.DichVuPhongDto dv = currentRoomServices.get(row);
        this.selectedDichVu = dv;
        lblMaLichSu.setText("Mã lịch sử: " + dv.getId());
        
        int dienCu = dv.getSoDienCu() != null ? dv.getSoDienCu() : 0;
        int dienMoi = dv.getSoDienMoi() != null ? dv.getSoDienMoi() : 0;
        txtSoDienCu.setText(String.valueOf(dienCu));
        txtSoDienMoi.setText(String.valueOf(dienMoi));
        int slDien = Math.max(0, dienMoi - dienCu);
        txtSoDien.setText(String.valueOf(slDien));
        BigDecimal giaDien = dv.getGiaDien() != null ? dv.getGiaDien() : BigDecimal.ZERO;
        txtThanhTienDien.setText(new DecimalFormat("#").format(giaDien.multiply(new BigDecimal(slDien))));
        
        int nuocCu = dv.getSoNuocCu() != null ? dv.getSoNuocCu() : 0;
        int nuocMoi = dv.getSoNuocMoi() != null ? dv.getSoNuocMoi() : 0;
        txtSoNuocCu.setText(String.valueOf(nuocCu));
        txtSoNuocMoi.setText(String.valueOf(nuocMoi));
        int slNuoc = Math.max(0, nuocMoi - nuocCu);
        txtSoNuoc.setText(String.valueOf(slNuoc));
        BigDecimal giaNuoc = dv.getGiaNuoc() != null ? dv.getGiaNuoc() : BigDecimal.ZERO;
        txtThanhTienNuoc.setText(new DecimalFormat("#").format(giaNuoc.multiply(new BigDecimal(slNuoc))));
        
        txtTienMang.setText(new DecimalFormat("#").format(dv.getTienMang() != null ? dv.getTienMang() : BigDecimal.ZERO));
        
        calculate();
    }

    private java.math.BigDecimal parseSafely(String text) {
        if (text == null || text.trim().isEmpty()) return java.math.BigDecimal.ZERO;
        try {
            return new java.math.BigDecimal(text.replace(",", "").trim());
        } catch (Exception e) {
            return java.math.BigDecimal.ZERO;
        }
    }
}
