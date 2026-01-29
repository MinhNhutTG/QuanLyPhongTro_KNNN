package com.quanlyphongtro.ui.Service;

import com.quanlyphongtro.models.DichVu;
import com.quanlyphongtro.models.DichVuPhong;
import com.quanlyphongtro.models.HopDongThue;
import com.quanlyphongtro.models.Phong;
import com.quanlyphongtro.service.DichVuPhongService;
import com.quanlyphongtro.service.DichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.time.ZoneId;

@Component
public class panel_service extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private final DichVuPhongService dichVuPhongService;
    private final DichVuService dichVuService;
    private final com.quanlyphongtro.service.HopDongThueSevice hopDongService;

    private JTextField txtSoPhong;
    private JComboBox<String> cbStatus; // Input Form Status
    private JTextField txtKi, txtDienCu, txtDienMoi, txtGiaDien, txtNuocCu, txtNuocMoi, txtGiaNuoc, txtTienMang;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblMaHD;
    private JSpinner spinDate;
    private JComboBox<String> cbFilterStatus;
    
    // Navigation State
    private List<String> activeRoomNumbers = new ArrayList<>();
    private int currentRoomIndex = 0;
    private Integer currentEditId = null; 

    // Colors
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);

    @Autowired
    public panel_service(DichVuPhongService dichVuPhongService, DichVuService dichVuService, com.quanlyphongtro.service.HopDongThueSevice hopDongService) {
        this.dichVuPhongService = dichVuPhongService;
        this.dichVuService = dichVuService;
        this.hopDongService = hopDongService;

        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- HEADER ---
        add(createHeader(), BorderLayout.NORTH);

        // --- MAIN CONTENT ---
        JPanel panelMain = new JPanel(new BorderLayout(0, 20));
        panelMain.setOpaque(false);
        panelMain.add(createInputForm(), BorderLayout.NORTH);
        panelMain.add(createTableSection(), BorderLayout.CENTER);
        add(panelMain, BorderLayout.CENTER);

        // --- FOOTER ---
        add(createFooterNav(), BorderLayout.SOUTH);
        
        add(createFooterNav(), BorderLayout.SOUTH);
        
        // Auto-refresh when tab is opened
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                loadActiveRooms();
                // loadTableData();
            }
        });
        
        loadActiveRooms();
        // loadTableData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel("Quản Lý Dịch Vụ Phòng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));
        header.add(lblTitle, BorderLayout.WEST);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        toolbar.setOpaque(false);

        cbFilterStatus = new JComboBox<>(new String[]{"Tất cả trạng thái", "Chờ lập hoá đơn", "Đã thanh toán"});
        cbFilterStatus.setPreferredSize(new Dimension(180, 35));
        cbFilterStatus.addActionListener(e -> filterTable());
        toolbar.add(cbFilterStatus);

        JButton btnPrice = createStyledButton("Quản lý giá", new Color(155, 89, 182), Color.WHITE);
        btnPrice.addActionListener(e -> {
            new PriceManagementDialog((JFrame)SwingUtilities.getWindowAncestor(this), dichVuService).setVisible(true);
            loadDefaultPrices(); // Refresh prices immediately
        });
        toolbar.add(btnPrice);

        header.add(toolbar, BorderLayout.EAST);
        return header;
    }

    private JPanel createInputForm() {
        JPanel card = new JPanel(new BorderLayout(20, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Grid
        JPanel gridFields = new JPanel(new GridBagLayout());
        gridFields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 1
        gbc.gridy = 0;
        gbc.gridx = 0; gridFields.add(new JLabel("Mã dịch vụ:"), gbc);
        lblMaHD = new JLabel("---");
        lblMaHD.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblMaHD.setForeground(PRIMARY_COLOR);
        gbc.gridx = 1; gridFields.add(lblMaHD, gbc);

        gbc.gridx = 2; gridFields.add(new JLabel("Phòng đang thuê:"), gbc);
        txtSoPhong = createStyledTextField();
        txtSoPhong.setEditable(false);
        txtSoPhong.setBackground(new Color(245, 245, 245));
        gbc.gridx = 3; gridFields.add(txtSoPhong, gbc);

        gbc.gridx = 4; gridFields.add(new JLabel("Kỳ sử dụng (MM/yyyy):"), gbc);
        txtKi = createStyledTextField(); 
        gbc.gridx = 5; gridFields.add(txtKi, gbc);

        // Row 2
        gbc.gridy = 1;
        gbc.gridx = 0; gridFields.add(new JLabel("Số điện cũ:"), gbc);
        txtDienCu = createStyledTextField(); txtDienCu.setEditable(false);
        gbc.gridx = 1; gridFields.add(txtDienCu, gbc);

        gbc.gridx = 2; gridFields.add(new JLabel("Số điện mới:"), gbc);
        txtDienMoi = createStyledTextField(); 
        gbc.gridx = 3; gridFields.add(txtDienMoi, gbc);

        gbc.gridx = 4; gridFields.add(new JLabel("Giá điện:"), gbc);
        txtGiaDien = createStyledTextField(); 
        gbc.gridx = 5; gridFields.add(txtGiaDien, gbc);

        // Row 3
        gbc.gridy = 2;
        gbc.gridx = 0; gridFields.add(new JLabel("Số nước cũ:"), gbc);
        txtNuocCu = createStyledTextField(); txtNuocCu.setEditable(false);
        gbc.gridx = 1; gridFields.add(txtNuocCu, gbc);

        gbc.gridx = 2; gridFields.add(new JLabel("Số nước mới:"), gbc);
        txtNuocMoi = createStyledTextField(); 
        gbc.gridx = 3; gridFields.add(txtNuocMoi, gbc);

        gbc.gridx = 4; gridFields.add(new JLabel("Giá nước:"), gbc);
        txtGiaNuoc = createStyledTextField(); 
        gbc.gridx = 5; gridFields.add(txtGiaNuoc, gbc);

        // Row 4
        gbc.gridy = 3;
        gbc.gridx = 0; gridFields.add(new JLabel("Tiền mạng:"), gbc);
        txtTienMang = createStyledTextField(); 
        gbc.gridx = 1; gridFields.add(txtTienMang, gbc);

        gbc.gridx = 2; gridFields.add(new JLabel("Ngày tạo:"), gbc);
        spinDate = new JSpinner(new SpinnerDateModel());
        spinDate.setEditor(new JSpinner.DateEditor(spinDate, "dd/MM/yyyy"));
        spinDate.setPreferredSize(new Dimension(0, 35));
        gbc.gridx = 3; gridFields.add(spinDate, gbc);

        gbc.gridx = 4; gridFields.add(new JLabel("Trạng thái:"), gbc);
        cbStatus = new JComboBox<>(new String[]{"Chờ lập hoá đơn", "Đã thanh toán"});
        cbStatus.setPreferredSize(new Dimension(0, 35));
        gbc.gridx = 5; gridFields.add(cbStatus, gbc);

        // Buttons
        JPanel btnAction = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnAction.setOpaque(false);
        
        JButton btnSave = createStyledButton("Lưu Dịch Vụ", SUCCESS_COLOR, Color.WHITE);
        btnSave.addActionListener(e -> saveService());
        btnAction.add(btnSave);
        
        JButton btnDelete = createStyledButton("Xóa", DANGER_COLOR, Color.WHITE);
        btnDelete.addActionListener(e -> deleteService());
        btnAction.add(btnDelete);
        
        JButton btnClear = createStyledButton("Làm mới", new Color(108, 117, 125), Color.WHITE);
        btnClear.addActionListener(e -> clearForm());
        btnAction.add(btnClear);

        card.add(gridFields, BorderLayout.CENTER);
        card.add(btnAction, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createTableSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = {"Mã dịch vụ", "Mã HD", "Số Phòng", "Kì", "Số điện cũ", "Số điện mới", "Số nước cũ", "Số nước mới", "Giá điện", "Giá nước", "Trạng Thái", "Ngày tạo", "Tiền mạng"};
        model = new DefaultTableModel(columns, 0) {
              @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                     Integer id = (Integer) model.getValueAt(row, 0);
                     // Set edit mode
                     loadServiceToForm(id);
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(218, 220, 224), 1));
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFooterNav() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        footer.setOpaque(false);
        
        JButton btnPrev = createStyledButton("<< Phòng trước", Color.WHITE, Color.DARK_GRAY);
        btnPrev.setBorder(new LineBorder(new Color(200, 200, 200)));
        btnPrev.addActionListener(e -> navigateRooms(-1));
        
        JButton btnNext = createStyledButton("Phòng tiếp theo >>", Color.WHITE, Color.DARK_GRAY);
        btnNext.setBorder(new LineBorder(new Color(200, 200, 200)));
        btnNext.addActionListener(e -> navigateRooms(1));

        footer.add(btnPrev);
        footer.add(btnNext);
        return footer;
    }

    // --- LOGIC METHODS ---
    
    private void loadActiveRooms() {
        activeRoomNumbers.clear();
        List<com.quanlyphongtro.dto.HopDongDto> contracts = hopDongService.getHopDongByStatus("Đang hiệu lực");
        for (com.quanlyphongtro.dto.HopDongDto hd : contracts) {
            String room = hd.getSoPhong(); // DTO has getSoPhong
            if (!activeRoomNumbers.contains(room)) {
                activeRoomNumbers.add(room);
            }
        }
        if (!activeRoomNumbers.isEmpty()) {
            currentRoomIndex = 0;
            txtSoPhong.setText(activeRoomNumbers.get(currentRoomIndex));
            loadTableDataForRoom(activeRoomNumbers.get(currentRoomIndex));
        } else {
            txtSoPhong.setText("");
        }
    }
    
    private void navigateRooms(int direction) {
        if (activeRoomNumbers.isEmpty()) return;
        
        currentRoomIndex += direction;
        
        if (currentRoomIndex < 0) currentRoomIndex = activeRoomNumbers.size() - 1;
        if (currentRoomIndex >= activeRoomNumbers.size()) currentRoomIndex = 0;
        
        String room = activeRoomNumbers.get(currentRoomIndex);
        txtSoPhong.setText(room);
        
        // Load New Entry Form
        if (currentEditId == null) {
            loadNewEntryForRoom(room);
        }
        // Filter Table (Legacy Logic: Changing Room updates List)
        loadTableDataForRoom(room);
    }
    
    private void loadNewEntryForRoom(String room) {
        currentEditId = null; // New Mode
        lblMaHD.setText("Tự động tạo...");
        
        // Auto fill Period
        java.time.LocalDate now = java.time.LocalDate.now();
        txtKi.setText(now.getMonthValue() + "/" + now.getYear());
        
        DichVuPhong last = dichVuPhongService.findLastUsage(room);
        if (last != null) {
            // Found last usage, pre-fill Old Index but keep editable
            txtDienCu.setText(String.valueOf(last.getSoDienMoi() != null ? last.getSoDienMoi() : 0));
            txtNuocCu.setText(String.valueOf(last.getSoNuocMoi() != null ? last.getSoNuocMoi() : 0));
            txtTienMang.setText("0"); 
        } else {
             // New room, default 0
             txtDienCu.setText("0");
             txtNuocCu.setText("0");
             txtTienMang.setText("0");
        }
        
        // Make fields editable as per user request
        txtDienCu.setEditable(true);
        txtNuocCu.setEditable(true);
        
        // Load prices
        loadDefaultPrices();
        
        txtDienMoi.setText("");
        txtNuocMoi.setText("");
        txtDienMoi.requestFocus();
    }

    private void loadDefaultPrices() {
        if (currentEditId != null) return; // Don't override if editing
        
        List<DichVu> services = dichVuService.getAllDichVu();
        BigDecimal dien = BigDecimal.ZERO;
        BigDecimal nuoc = BigDecimal.ZERO;
        BigDecimal mang = BigDecimal.ZERO;
        
        for (DichVu dv : services) {
            String name = dv.getTenDichVu().toLowerCase();
            if (name.contains("điện")) dien = dv.getGia();
            else if (name.contains("nước")) nuoc = dv.getGia();
            else if (name.contains("mạng") || name.contains("wifi") || name.contains("internet")) mang = dv.getGia();
        }
        
        txtGiaDien.setText(new DecimalFormat("#").format(dien));
        txtGiaNuoc.setText(new DecimalFormat("#").format(nuoc));
        txtTienMang.setText(new DecimalFormat("#").format(mang));
    }
    
    private void saveService() {
        try {
            String roomNo = txtSoPhong.getText();
            String ki = txtKi.getText();

            // Duplicate Check
            if (currentEditId == null) {
                if (dichVuPhongService.checkDuplicateForPeriod(roomNo, ki)) {
                    JOptionPane.showMessageDialog(this, "Dịch vụ cho kỳ này (" + ki + ") tại phòng " + roomNo + " đã tồn tại!");
                    return;
                }
            }

            if (currentEditId != null) {
                DichVuPhong dv = dichVuPhongService.getById(currentEditId);
                
                dv.setKi(ki);
            
                 try {
                String dienCuStr = txtDienCu.getText().trim();
                String nuocCuStr = txtNuocCu.getText().trim();
                if (dienCuStr.isEmpty() || nuocCuStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Chỉ số điện/nước cũ không được để trống!"); return; }
                int dienCu = Integer.parseInt(dienCuStr);
                int nuocCu = Integer.parseInt(nuocCuStr);
                if (dienCu == 0 || nuocCu == 0) { JOptionPane.showMessageDialog(this, "Chỉ số điện/nước cũ không được bằng 0!"); return; }
                dv.setSoDienCu(dienCu);
                dv.setSoNuocCu(nuocCu);

                String dienMoiStr = txtDienMoi.getText().trim();
                if (!dienMoiStr.isEmpty()) {
                    int dienMoi = Integer.parseInt(dienMoiStr);
                    if (dienMoi < dienCu) { JOptionPane.showMessageDialog(this, "Chỉ số điện MỚI phải lớn hơn hoặc bằng chỉ số CŨ!"); return; }
                    dv.setSoDienMoi(dienMoi);
                } else { dv.setSoDienMoi(null); }
                
                String nuocMoiStr = txtNuocMoi.getText().trim();
                if (!nuocMoiStr.isEmpty()) {
                    int nuocMoi = Integer.parseInt(nuocMoiStr);
                    if (nuocMoi < nuocCu) { JOptionPane.showMessageDialog(this, "Chỉ số nước MỚI phải lớn hơn hoặc bằng chỉ số CŨ!"); return; }
                    dv.setSoNuocMoi(nuocMoi);
                } else { dv.setSoNuocMoi(null); }

                dv.setGiaDien(new BigDecimal(txtGiaDien.getText()));
                dv.setGiaNuoc(new BigDecimal(txtGiaNuoc.getText()));
                dv.setTienMang(new BigDecimal(txtTienMang.getText()));
                dv.setTrangThai((String) cbStatus.getSelectedItem()); // Update Status

                 } catch (NumberFormatException e) {
                     JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
                     return;
                 }
                
                dichVuPhongService.save(dv);

            } else {
                // CREATE NEW using Service
                 // Validate inputs
                 Integer dienCu = null, nuocCu = null, dienMoi = null, nuocMoi = null;
                 BigDecimal giaDien = null, giaNuoc = null, tienMang = null;
                 
                 try {
                    String dienCuStr = txtDienCu.getText().trim();
                    String nuocCuStr = txtNuocCu.getText().trim();
                    if (dienCuStr.isEmpty() || nuocCuStr.isEmpty()) { JOptionPane.showMessageDialog(this, "Chỉ số điện/nước cũ không được để trống!"); return; }
                    dienCu = Integer.parseInt(dienCuStr);
                    nuocCu = Integer.parseInt(nuocCuStr);
                    if (dienCu == 0 || nuocCu == 0) { JOptionPane.showMessageDialog(this, "Chỉ số điện/nước cũ không được bằng 0!"); return; }

                    String dienMoiStr = txtDienMoi.getText().trim();
                    if (!dienMoiStr.isEmpty()) {
                        dienMoi = Integer.parseInt(dienMoiStr);
                        if (dienMoi < dienCu) { JOptionPane.showMessageDialog(this, "Chỉ số điện MỚI phải lớn hơn hoặc bằng chỉ số CŨ!"); return; }
                    }

                    String nuocMoiStr = txtNuocMoi.getText().trim();
                    if (!nuocMoiStr.isEmpty()) {
                        nuocMoi = Integer.parseInt(nuocMoiStr);
                        if (nuocMoi < nuocCu) { JOptionPane.showMessageDialog(this, "Chỉ số nước MỚI phải lớn hơn hoặc bằng chỉ số CŨ!"); return; }
                    }

                    giaDien = new BigDecimal(txtGiaDien.getText());
                    giaNuoc = new BigDecimal(txtGiaNuoc.getText());
                    tienMang = new BigDecimal(txtTienMang.getText());
                 } catch (NumberFormatException e) {
                     JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
                     return;
                 }

                String trangThai = (String) cbStatus.getSelectedItem();
                dichVuPhongService.addDichVu(roomNo, ki, dienCu, dienMoi, nuocCu, nuocMoi, giaDien, giaNuoc, tienMang, trangThai);
            }
             
             JOptionPane.showMessageDialog(this, "Lưu thành công!");
             
             clearForm(); 


        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void deleteService() {
        if (currentEditId == null) {
             JOptionPane.showMessageDialog(this, "Chọn dịch vụ để xóa");
             return;
        }
        if (JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?") == JOptionPane.YES_OPTION) {
            try {
                dichVuPhongService.delete(currentEditId);
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "không thể xóa dịch vụ khi đã lập hóa đơn!");
                ex.printStackTrace();
            }
        }
    }
    
    // private void loadTableData() {
    //     // Legacy behavior: Show ALL records sorted by Ki DESC
    //     List<com.quanlyphongtro.dto.DichVuPhongDto> list = dichVuPhongService.getAllSortedByKiDesc();
    //     updateTable(list);
    // }

    private void loadTableDataForRoom(String room) {
        // Legacy behavior: Filter by room, sorted by Ki DESC
        List<com.quanlyphongtro.dto.DichVuPhongDto> list = dichVuPhongService.getByRoomSortedByKiDesc(room);
        updateTable(list);
    }
    
    private void filterTable() {
        String status = (String) cbFilterStatus.getSelectedItem();
        if ("Đang hiệu lực".equals(status) || "Tất cả trạng thái".equals(status)) {
            // loadTableData(); // Show all sorted by Ki DESC
        } else {
            // Use service method for filtering by status
            List<com.quanlyphongtro.dto.DichVuPhongDto> list = dichVuPhongService.filterByStatus(status);
            updateTable(list);
        }
    }
    
    private void updateTable(List<com.quanlyphongtro.dto.DichVuPhongDto> list) {
        model.setRowCount(0);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (com.quanlyphongtro.dto.DichVuPhongDto d : list) {
            model.addRow(new Object[]{
                d.getId(), 
                d.getMaHopDong(),
                d.getSoPhong(), 
                d.getKi(),
                d.getSoDienCu(),
                d.getSoDienMoi(),
                d.getSoNuocCu(),
                d.getSoNuocMoi(),
                d.getGiaDien(),
                d.getGiaNuoc(),
                d.getTrangThai(),
                d.getNgayTao() != null ? d.getNgayTao().format(dtf) : "",
                d.getTienMang()
            });
        }
    }
    
    private void loadServiceToForm(Integer id) {
        DichVuPhong dv = dichVuPhongService.getById(id);
        if (dv != null) {
            currentEditId = dv.getId();
            lblMaHD.setText(String.valueOf(dv.getId()));
            
            // Set textfield to show the room
            txtSoPhong.setText(dv.getPhong().getSoPhong());
            
            txtKi.setText(dv.getKi());
            
            txtDienCu.setText(String.valueOf(dv.getSoDienCu()));
            txtDienMoi.setText(String.valueOf(dv.getSoDienMoi()));
            txtGiaDien.setText(String.valueOf(dv.getGiaDien()));
            
            txtNuocCu.setText(String.valueOf(dv.getSoNuocCu()));
            txtNuocMoi.setText(String.valueOf(dv.getSoNuocMoi()));
            txtGiaNuoc.setText(String.valueOf(dv.getGiaNuoc()));
            
            txtTienMang.setText(String.valueOf(dv.getTienMang()));
            if (dv.getNgayTao() != null)
                spinDate.setValue(Date.from(dv.getNgayTao().atZone(ZoneId.systemDefault()).toInstant()));
            
            if (dv.getTrangThai() != null) cbStatus.setSelectedItem(dv.getTrangThai());
        }
    }
    
    private void clearForm() {
        currentEditId = null;
        lblMaHD.setText("---");

        // Determine current room from text field or navigation index
        String currentRoom = txtSoPhong.getText();
        if (currentRoom == null || currentRoom.isEmpty() || !activeRoomNumbers.contains(currentRoom)) {
             if (!activeRoomNumbers.isEmpty()) {
                 currentRoom = activeRoomNumbers.get(currentRoomIndex);
             }
        } else {
            // Update index to match current text (if user clicked table row)
            int idx = activeRoomNumbers.indexOf(currentRoom);
            if (idx >= 0) currentRoomIndex = idx;
        }

        if (currentRoom != null && !currentRoom.isEmpty()) {
            txtSoPhong.setText(currentRoom);
            loadNewEntryForRoom(currentRoom);
            loadTableDataForRoom(currentRoom);
        } else {
             model.setRowCount(0);
        }
        
        cbStatus.setSelectedIndex(0); 
    }

    // --- HELPERS ---
    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(0, 35));
        tf.setBorder(new CompoundBorder(new LineBorder(new Color(206, 212, 218)), new EmptyBorder(0, 8, 0, 8)));
        return tf;
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 40));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void styleTable(JTable table) {
        com.quanlyphongtro.utils.TableUtils.applyStandardStyling(table);
        // Cols: "Mã dịch vụ", "Mã HD", "Số Phòng", "Kì", "Số điện cũ", "Số điện mới", "Số nước cũ", "Số nước mới", "Giá điện", "Giá nước", "Trạng Thái", "Ngày tạo", "Tiền mạng"
        if (table.getColumnCount() >= 13) {
             table.getColumnModel().getColumn(0).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(1).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(2).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(3).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             
             table.getColumnModel().getColumn(4).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getRightRenderer());
             table.getColumnModel().getColumn(5).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getRightRenderer());
             table.getColumnModel().getColumn(6).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getRightRenderer());
             table.getColumnModel().getColumn(7).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getRightRenderer());
             
             table.getColumnModel().getColumn(8).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
             table.getColumnModel().getColumn(9).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
             
             table.getColumnModel().getColumn(10).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(11).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getDateRenderer());
             
             table.getColumnModel().getColumn(12).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer()); // Tien Mang matches legacy
        }
    }
}