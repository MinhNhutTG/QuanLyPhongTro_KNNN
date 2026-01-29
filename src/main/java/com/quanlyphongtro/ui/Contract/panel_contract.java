package com.quanlyphongtro.ui.Contract;

import com.quanlyphongtro.config.SpringContext;
import com.quanlyphongtro.dto.HopDongDto;
import com.quanlyphongtro.service.HopDongThueSevice;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class panel_contract extends JPanel {


    private static final long serialVersionUID = 1L;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh,btnView;
    private JComboBox<String> cbxStatusFilter;
    private final HopDongThueSevice hopDongService;
    // Hệ thống màu sắc đồng bộ (Modern UI)
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public panel_contract() {
        // Cấu hình tổng thể Panel chính
        this.hopDongService = SpringContext.getBean(HopDongThueSevice.class);
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- 1. TIÊU ĐỀ VÀ BỘ LỌC (NORTH) ---
        add(createTopPanel(), BorderLayout.NORTH);

        // --- 2. VÙNG NỘI DUNG CHÍNH (CENTER) ---
        JPanel panelMain = new JPanel(new BorderLayout(0, 15));
        panelMain.setOpaque(false);
        panelMain.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Thanh công cụ nút bấm (Phía trên của panelMain)
        panelMain.add(createToolbar(), BorderLayout.NORTH);

        // Bảng danh sách hợp đồng (Trung tâm của panelMain - để tự giãn nở)
        panelMain.add(createTableSection(), BorderLayout.CENTER);

        add(panelMain, BorderLayout.CENTER);
        setupEvent();
        loadData();
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("Quản Lý Hợp Đồng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setOpaque(false);

        JLabel lblFilter = new JLabel("Trạng thái: ");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 13));

        cbxStatusFilter = new JComboBox<>(new String[]{"Tất cả hợp đồng", "Đang hiệu lực", "Đã thanh lý", "Quá hạn"});
        cbxStatusFilter.setPreferredSize(new Dimension(180, 35));

        filterPanel.add(lblFilter);
        filterPanel.add(cbxStatusFilter);

        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(filterPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        toolbar.setOpaque(false);
        btnAdd = createStyledButton(" Thêm mới ", SUCCESS_COLOR, Color.WHITE);
        btnUpdate = createStyledButton(" Sửa hợp đồng ", PRIMARY_COLOR, new Color(50, 50, 50));
        btnDelete = createStyledButton(" Xóa hợp đồng ", DANGER_COLOR, Color.WHITE);
        btnView = createStyledButton(" Xem chi tiết ", new Color(52, 73, 94), Color.WHITE);
        btnRefresh = createStyledButton(" Làm mới ", new Color(108, 117, 125), Color.WHITE);
        toolbar.add(btnAdd);
        toolbar.add(btnUpdate);
        toolbar.add(btnDelete);
        toolbar.add(btnView);
        toolbar.add(btnRefresh);
        return toolbar;
    }

    private JScrollPane createTableSection() {
        String[] columnNames = {"Mã hợp đồng", "Ngày thuê", "Hạn thuê", "Số phòng", "Giá phòng", "Trạng thái", "Ngày tạo hợp đồng"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        return new JScrollPane(table);
    }

    private void styleTable(JTable table) {
        com.quanlyphongtro.utils.TableUtils.applyStandardStyling(table);
        
        // Col names: "Mã hợp đồng", "Ngày thuê", "Hạn thuê", "Số phòng", "Giá phòng", "Trạng thái", "Ngày tạo hợp đồng"
        if (table.getColumnCount() >= 7) {
             table.getColumnModel().getColumn(0).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(1).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getDateRenderer());
             table.getColumnModel().getColumn(2).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getDateRenderer());
             
             table.getColumnModel().getColumn(3).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer()); // So Phong
             
             table.getColumnModel().getColumn(4).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer()); // Gia Phong
             
             table.getColumnModel().getColumn(5).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer()); // Status
             table.getColumnModel().getColumn(6).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getDateRenderer());
        }
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        return btn;
    }

    private void setupEvent() {
        // Thêm mới
        btnAdd.addActionListener(e -> {
            new add_edit_contract(null, ContractMode.ADD, this::loadData).setVisible(true);

        });

        // Cập nhật
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn hợp đồng để sửa!");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            new add_edit_contract(id, ContractMode.EDIT, this::loadData).setVisible(true);

        });

        // Xóa
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn hợp đồng để xóa!");
                return;
            }
            String id = (String) model.getValueAt(row, 0);
            int opt = JOptionPane.showConfirmDialog(this, "Xóa hợp đồng " + id + "?\nCác thông tin liên quan sẽ bị xóa!", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if(opt == JOptionPane.YES_OPTION) {
                hopDongService.deleteHopDong(id);
                loadData();
                JOptionPane.showMessageDialog(this, "Đã xóa!");
            }
        });

        btnView.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Chọn hợp đồng để xem!");
                return;
            }

            String id = (String) model.getValueAt(row, 0);

            new add_edit_contract(id, ContractMode.VIEW, null)
                    .setVisible(true);
        });

        // Refresh
        btnRefresh.addActionListener(e -> {
            cbxStatusFilter.setSelectedIndex(0);
            loadData();
        });

        // Filter
        cbxStatusFilter.addActionListener(e -> loadData());
    }

    private void loadData() {
        model.setRowCount(0);
        String status = (String) cbxStatusFilter.getSelectedItem();

        List<HopDongDto> list = hopDongService.getHopDongByStatus(status);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(HopDongDto hd : list) {
            model.addRow(new Object[]{
                    hd.getId(),
                    hd.getNgayThue().format(fmt), // New index 1
                    hd.getHanThue().format(fmt),  // New index 2
                    hd.getSoPhong(),              // New index 3
                    hd.getGiaPhong(),             // New index 4
                    hd.getTrangThai(),
                    hd.getNgayTao().format(fmt)
            });
        }
    }
}