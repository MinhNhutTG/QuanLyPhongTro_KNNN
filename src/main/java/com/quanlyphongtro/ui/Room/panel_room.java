package com.quanlyphongtro.ui.Room;

import com.quanlyphongtro.config.SpringContext;
import com.quanlyphongtro.dto.LoaiPhongDto;
import com.quanlyphongtro.dto.PhongDto;
import com.quanlyphongtro.service.LoaiPhongService;
import com.quanlyphongtro.service.PhongService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

@org.springframework.stereotype.Component
public class panel_room extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;

    // Components
    private final PhongService phongService;
    private final LoaiPhongService loaiPhongService; // [THÊM MỚI] Để load filter

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnTypeMgmt;

    // Filters
    private JComboBox<Object> cbxType; // Object để chứa cả String "Tất cả" và LoaiPhongDto
    private JComboBox<String> cbxStatus;

    // Colors & Fonts (Giữ nguyên)
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public panel_room() {
        this.phongService = SpringContext.getBean(PhongService.class);
        this.loaiPhongService = SpringContext.getBean(LoaiPhongService.class);

        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        add(createTopPanel(), BorderLayout.NORTH);

        JPanel panelMain = new JPanel(new BorderLayout(0, 15));
        panelMain.setOpaque(false);
        panelMain.setBorder(new EmptyBorder(15, 0, 0, 0));

        panelMain.add(createToolbar(), BorderLayout.NORTH);
        panelMain.add(createTableSection(), BorderLayout.CENTER);

        add(panelMain, BorderLayout.CENTER);

        // [SỬA] Load filter trước rồi mới load data
        loadFilterData();
        loadPhongFromDB();
        setupEvent();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("Quản Lý Danh Sách Phòng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        filterPanel.setOpaque(false);

        // [SỬA] Filter Loại Phòng (Dùng JComboBox<Object>)
        filterPanel.add(new JLabel("Loại:"));
        cbxType = new JComboBox<>();
        cbxType.setPreferredSize(new Dimension(150, 35));
        filterPanel.add(cbxType);

        // Filter Trạng Thái
        filterPanel.add(new JLabel("Trạng thái:"));
        cbxStatus = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đang thuê", "Bảo trì"});
        cbxStatus.setPreferredSize(new Dimension(130, 35));
        filterPanel.add(cbxStatus);

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(filterPanel, BorderLayout.EAST);
        return panel;
    }

    // [THÊM MỚI] Load dữ liệu vào Filter ComboBox
    private void loadFilterData() {
        cbxType.removeAllItems();
        cbxType.addItem("Tất cả");
        List<LoaiPhongDto> types = loaiPhongService.getAllLoaiPhong();
        for (LoaiPhongDto type : types) {
            cbxType.addItem(type); // Add DTO, toString hiển thị tên
        }
    }

    // [GIỮ NGUYÊN] Layout toolbar
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);

        btnAdd = createStyledButton(" Thêm mới ", SUCCESS_COLOR, Color.WHITE);
        btnUpdate = createStyledButton(" Cập nhật ", PRIMARY_COLOR, new Color(50, 50, 50));
        btnDelete = createStyledButton(" Xóa phòng ", DANGER_COLOR, Color.WHITE); // Đổi tên cho đúng ngữ nghĩa
        btnTypeMgmt = createStyledButton("  Quản lý loại phòng ", new Color(108, 117, 125), Color.WHITE);

        toolbar.add(btnAdd);
        toolbar.add(btnUpdate);
        toolbar.add(btnDelete);
        toolbar.add(btnTypeMgmt);
        return toolbar;
    }

    // [GIỮ NGUYÊN] Layout Table
    private JScrollPane createTableSection() {
        // [SỬA] Cập nhật cột theo Legacy: Số phòng, Loại phòng, Trạng thái, Giá phòng, Ghi chú
        String[] columns = {"Số phòng", "Loại phòng", "Trạng thái", "Giá phòng", "Ghi chú"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(218, 220, 224), 1));
        return scrollPane;
    }

    private void styleTable(JTable table) {
        com.quanlyphongtro.utils.TableUtils.applyStandardStyling(table);
        
        // Col 0: So Phong (Center)
        table.getColumnModel().getColumn(0).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
        
        // Col 1: Ten Loai (Left - Default)
        
        // Col 2: Trang Thai (Center)
        table.getColumnModel().getColumn(2).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
        
        // Col 3: Gia Phong (Currency Right)
        table.getColumnModel().getColumn(3).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
        
        // Col 4: Ghi Chu (Left - Default)
        // Keep default left align
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        return btn;
    }

    // [QUAN TRỌNG] Gắn sự kiện logic
    private void setupEvent() {
        // 1. THÊM MỚI: Truyền NULL vào soPhongCanSua
        btnAdd.addActionListener(e -> {
            // Callback: () -> loadPhongFromDB() nghĩa là khi form đóng và lưu thành công, sẽ gọi hàm load lại bảng
            new add_edit_room(null, this::loadPhongFromDB).setVisible(true);
        });

        // 2. CẬP NHẬT: Truyền Số Phòng đang chọn vào
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa!");
                return;
            }
            // Lấy cột 0 là Số Phòng
            String soPhong = model.getValueAt(row, 0).toString();

            // Mở form với ID phòng cần sửa
            new add_edit_room(soPhong, this::loadPhongFromDB).setVisible(true);
        });

        // 3. Xóa phòng
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần xóa!");
                return;
            }
            String soPhong = model.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa phòng " + soPhong + "?\nHành động này không thể hoàn tác.",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    phongService.deletePhong(soPhong);
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadPhongFromDB();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                }
            }
        });

        // 4. Quản lý loại phòng
        btnTypeMgmt.addActionListener(e -> {
            new add_edit_room_typeroom().setVisible(true);
            // Khi đóng quản lý loại phòng, cần reload lại filter loại phòng bên ngoài
            loadFilterData();
        });

        // 5. Sự kiện Filter
        ActionListener filterAction = e -> loadPhongFromDB();
        cbxType.addActionListener(filterAction);
        cbxStatus.addActionListener(filterAction);
    }

    // [THÊM MỚI] Logic Load data kết hợp Filter
    private void loadPhongFromDB() {
        model.setRowCount(0);

        // Lấy giá trị filter
        String selectedStatus = (String) cbxStatus.getSelectedItem();

        Object typeObj = cbxType.getSelectedItem();
        String selectedTypeID = "Tất cả";
        if (typeObj instanceof LoaiPhongDto) {
            selectedTypeID = ((LoaiPhongDto) typeObj).getMaLoai();
        }

        // Gọi Service tìm kiếm
        List<PhongDto> list = phongService.searchPhong(selectedTypeID, selectedStatus);

        for (PhongDto dto : list) {
            model.addRow(new Object[]{
                    dto.getSoPhong(),
                    dto.getTenLoai(), // Hiển thị tên loại
                    dto.getTrangThai(), // Trạng thái
                    dto.getGiaTien(), // Giá tiền
                    dto.getGhiChu() != null ? dto.getGhiChu() : "" // Ghi chú
            });
        }
    }
}