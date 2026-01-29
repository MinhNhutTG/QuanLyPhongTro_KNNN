package com.quanlyphongtro.ui.Guest;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.List;
import java.util.Date;
import java.time.ZoneId;

import com.quanlyphongtro.dto.KhachThueDto;
import com.quanlyphongtro.service.KhachThueService;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component
public class panel_guest extends JPanel {

    private static final long serialVersionUID = 1L;
    private final KhachThueService khachThueService;

    private JTextField txtSearch, txtGuestID, txtFullName, txtIDCard, txtPhone, txtEmail, txtQueQuan;
    private JTable tableGuests, tableContracts;
    private DefaultTableModel modelGuests, modelContracts;
    private JComboBox<String> cbxStatusFilter, cbxStatusDetail;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;
    private JSpinner spinnerDOB;

    // Hệ thống màu sắc
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private final Color CARD_COLOR = Color.WHITE;
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    @Autowired
    public panel_guest(KhachThueService khachThueService) {
        this.khachThueService = khachThueService;

        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel panelContent = new JPanel(new BorderLayout(20, 0));
        panelContent.setOpaque(false);
        panelContent.add(createTableSection(), BorderLayout.CENTER);
        panelContent.add(createFormSection(), BorderLayout.EAST);

        add(panelContent, BorderLayout.CENTER);

        loadData();
        setupEvents();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel lblTitle = new JLabel("Quản Lý Khách Thuê");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));
        header.add(lblTitle, BorderLayout.WEST);

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchContainer.setOpaque(false);

        cbxStatusFilter = new JComboBox<>(new String[]{"Tất cả trạng thái", "Đang Thuê", "Chưa Thuê"});
        cbxStatusFilter.setPreferredSize(new Dimension(150, 35));

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(200, 35));

        JButton btnSearch = createStyledButton("Tìm kiếm", PRIMARY_COLOR, Color.WHITE);
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            updateTable(khachThueService.searchKhachThue(keyword));
        });

        searchContainer.add(new JLabel("Lọc:"));
        searchContainer.add(cbxStatusFilter);
        searchContainer.add(txtSearch);
        searchContainer.add(btnSearch);

        header.add(searchContainer, BorderLayout.EAST);
        return header;
    }

    private JPanel createTableSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);

        btnAdd = createStyledButton("Thêm mới ", SUCCESS_COLOR, Color.WHITE);
        btnUpdate = createStyledButton(" Cập nhật ", WARNING_COLOR, new Color(50, 50, 50));
        btnDelete = createStyledButton(" Xóa khách ", DANGER_COLOR, Color.WHITE);
        btnRefresh = createStyledButton(" Làm mới ", new Color(108, 117, 125), Color.WHITE);

        actions.add(btnAdd);
        actions.add(btnUpdate);
        actions.add(btnDelete);
        actions.add(btnRefresh);

        modelGuests = new DefaultTableModel(new String[]{"ID", "Họ tên", "CCCD", "SĐT", "Trạng thái"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableGuests = new JTable(modelGuests);
        styleTable(tableGuests);

        panel.add(actions, BorderLayout.NORTH);
        panel.add(new JScrollPane(tableGuests), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFormSection() {
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setPreferredSize(new Dimension(420, 0));
        formWrapper.setBackground(CARD_COLOR);
        formWrapper.setBorder(new CompoundBorder(new LineBorder(new Color(218, 220, 224), 1), new EmptyBorder(20, 20, 20, 20)));

        JPanel formBody = new JPanel();
        formBody.setLayout(new BoxLayout(formBody, BoxLayout.Y_AXIS));
        formBody.setOpaque(false);

        JLabel lblTitle = new JLabel("CHI TIẾT KHÁCH THUÊ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(PRIMARY_COLOR);
        formBody.add(lblTitle);
        formBody.add(Box.createVerticalStrut(20));

        // Lưới nhập liệu 2 cột
        JPanel gridFields = new JPanel(new GridLayout(0, 2, 15, 12));
        gridFields.setOpaque(false);

        addInputToGrid(gridFields, "Mã khách", txtGuestID = new JTextField());
        txtGuestID.setEditable(false);
        txtGuestID.setBackground(new Color(245, 245, 245));

        cbxStatusDetail = new JComboBox<>(new String[]{"Đang Thuê", "Chưa Thuê"});
        addInputToGrid(gridFields, "Trạng thái", cbxStatusDetail);
        addInputToGrid(gridFields, "Họ và tên", txtFullName = new JTextField());

        // Panel riêng cho Spinner Ngày sinh
        JPanel pnlDOB = new JPanel(new BorderLayout(0, 5));
        pnlDOB.setOpaque(false);
        JLabel lblDOB = new JLabel("Ngày sinh");
        lblDOB.setFont(new Font("Segoe UI", Font.BOLD, 13));
        spinnerDOB = new JSpinner(new SpinnerDateModel());
        spinnerDOB.setEditor(new JSpinner.DateEditor(spinnerDOB, "dd/MM/yyyy"));
        spinnerDOB.setPreferredSize(new Dimension(0, 35));
        pnlDOB.add(lblDOB, BorderLayout.NORTH);
        pnlDOB.add(spinnerDOB, BorderLayout.CENTER);
        gridFields.add(pnlDOB);

        addInputToGrid(gridFields, "Số CCCD", txtIDCard = new JTextField());
        addInputToGrid(gridFields, "Số điện thoại", txtPhone = new JTextField());
        addInputToGrid(gridFields, "Email", txtEmail = new JTextField());
        addInputToGrid(gridFields, "Quê quán", txtQueQuan = new JTextField());

        formBody.add(gridFields);
        formBody.add(Box.createVerticalStrut(20));
        formBody.add(new JSeparator());
        formBody.add(Box.createVerticalStrut(15));

        JLabel lblHD = new JLabel("HỢP ĐỒNG LIÊN QUAN");
        lblHD.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formBody.add(lblHD);
        formBody.add(Box.createVerticalStrut(10));

        modelContracts = new DefaultTableModel(new String[]{"Số HD", "Phòng", "Loại"}, 0);
        tableContracts = new JTable(modelContracts);
        styleTable(tableContracts);
        JScrollPane scrollHD = new JScrollPane(tableContracts);
        scrollHD.setPreferredSize(new Dimension(0, 150));
        formBody.add(scrollHD);

        formWrapper.add(formBody, BorderLayout.NORTH);
        return formWrapper;
    }

    private void addInputToGrid(JPanel container, String labelText, JComponent field) {
        JPanel group = new JPanel(new BorderLayout(0, 5));
        group.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        field.setPreferredSize(new Dimension(0, 35));
        if (field instanceof JTextField) {
            ((JTextField) field).setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(206, 212, 218), 1), new EmptyBorder(0, 8, 0, 8)));
        }
        group.add(label, BorderLayout.NORTH);
        group.add(field, BorderLayout.CENTER);
        container.add(group);
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35);
        table.setFont(MAIN_FONT);
        table.setSelectionBackground(new Color(232, 241, 249));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    // --- LOGIC XỬ LÝ ---

    private void loadData() {
        updateTable(khachThueService.getAllKhachThue());
    }

    private void updateTable(List<KhachThueDto> list) {
        modelGuests.setRowCount(0);
        for (KhachThueDto dto : list) {
            modelGuests.addRow(new Object[]{dto.getMaKhach(), dto.getHoTen(), dto.getCccd(), dto.getSoDienThoai(), dto.getTrangThai()});
        }
    }

    private void fillForm(int row) {
        if (row < 0) return;
        Integer id = (Integer) modelGuests.getValueAt(row, 0);
        KhachThueDto dto = khachThueService.getKhachThueById(id);
        if (dto != null) {
            txtGuestID.setText(String.valueOf(dto.getMaKhach()));
            txtFullName.setText(dto.getHoTen());
            txtIDCard.setText(dto.getCccd());
            txtPhone.setText(dto.getSoDienThoai());
            txtEmail.setText(dto.getEmail());
            txtQueQuan.setText(dto.getQueQuan());
            cbxStatusDetail.setSelectedItem(dto.getTrangThai());
            if (dto.getNgaySinh() != null) {
                spinnerDOB.setValue(Date.from(dto.getNgaySinh().atZone(ZoneId.systemDefault()).toInstant()));
            }

            modelContracts.setRowCount(0);
            List<com.quanlyphongtro.models.ChiTietHopDong> contracts = khachThueService.getContractsByGuest(id);
            for (com.quanlyphongtro.models.ChiTietHopDong ct : contracts) {
                if (ct.getHopDong() != null && ct.getHopDong().getPhong() != null) {
                    modelContracts.addRow(new Object[]{ct.getHopDong().getId(), ct.getHopDong().getPhong().getSoPhong(),
                            ct.getHopDong().getPhong().getLoaiPhong() != null ? ct.getHopDong().getPhong().getLoaiPhong().getTenLoai() : "N/A"});
                }
            }
        }
    }

    private KhachThueDto getFormDto() {
        KhachThueDto dto = new KhachThueDto();
        if (!txtGuestID.getText().isEmpty()) dto.setMaKhach(Integer.parseInt(txtGuestID.getText()));
        dto.setHoTen(txtFullName.getText().trim());
        dto.setCccd(txtIDCard.getText().trim());
        dto.setSoDienThoai(txtPhone.getText().trim());
        dto.setEmail(txtEmail.getText().trim());
        dto.setQueQuan(txtQueQuan.getText().trim());
        dto.setTrangThai((String) cbxStatusDetail.getSelectedItem());
        Date date = (Date) spinnerDOB.getValue();
        if (date != null) dto.setNgaySinh(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return dto;
    }

    private void setupEvents() {
        tableGuests.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { fillForm(tableGuests.getSelectedRow()); }
        });

        btnAdd.addActionListener(e -> {
            try {
                KhachThueDto dto = getFormDto();
                dto.setMaKhach(null);
                khachThueService.addKhachThue(dto);
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
                clearForm();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        btnUpdate.addActionListener(e -> {
            try {
                if (txtGuestID.getText().isEmpty()) throw new Exception("Chưa chọn khách!");
                khachThueService.updateKhachThue(Integer.parseInt(txtGuestID.getText()), getFormDto());
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        btnDelete.addActionListener(e -> {
            try {
                if (txtGuestID.getText().isEmpty()) throw new Exception("Chưa chọn khách!");
                if(JOptionPane.showConfirmDialog(this, "Xóa khách này?") == JOptionPane.YES_OPTION){
                    khachThueService.deleteKhachThue(Integer.parseInt(txtGuestID.getText()));
                    loadData();
                    clearForm();
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage()); }
        });

        btnRefresh.addActionListener(e -> { clearForm(); loadData(); });
        cbxStatusFilter.addActionListener(e -> {
            String status = (String) cbxStatusFilter.getSelectedItem();
            updateTable(khachThueService.filterByTrangThai(status));
        });
    }

    private void clearForm() {
        txtGuestID.setText(""); txtFullName.setText(""); txtIDCard.setText("");
        txtPhone.setText(""); txtEmail.setText(""); txtQueQuan.setText("");
        spinnerDOB.setValue(new Date()); tableGuests.clearSelection();
        modelContracts.setRowCount(0);
    }
}