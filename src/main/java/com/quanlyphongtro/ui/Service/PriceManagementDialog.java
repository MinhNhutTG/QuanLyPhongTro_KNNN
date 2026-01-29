package com.quanlyphongtro.ui.Service;

import com.quanlyphongtro.models.DichVu;
import com.quanlyphongtro.service.DichVuService;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class PriceManagementDialog extends JDialog {

    private DichVuService dichVuService;
    private JTextField txtTenDichVu;
    private JTextField txtGiaDichVu;
    private JTable table;
    private DefaultTableModel model;
    private Long currentId = null;

    public PriceManagementDialog(Frame parent, DichVuService dichVuService) {
        super(parent, "Quản lý giá dịch vụ", true);
        this.dichVuService = dichVuService;

        setSize(700, 450);
        setLocationRelativeTo(parent);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 245, 245));

        // --- LEFT: TABLE ---
        JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.setBounds(20, 20, 300, 370);
        pnlTable.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        
        // Header Label
        JLabel lblHeader = new JLabel("Giá dịch vụ", SwingConstants.CENTER);
        lblHeader.setOpaque(true);
        lblHeader.setBackground(new Color(0, 120, 215)); // Blue color from image
        lblHeader.setForeground(Color.WHITE);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblHeader.setPreferredSize(new Dimension(300, 30));
        pnlTable.add(lblHeader, BorderLayout.NORTH);

        // Table
        String[] columns = {"Tên dịch vụ", "Giá dịch vụ"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        pnlTable.add(new JScrollPane(table), BorderLayout.CENTER);
        
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtTenDichVu.setText(model.getValueAt(row, 0).toString());
                    String priceStr = model.getValueAt(row, 1).toString().replace(",", "");
                    txtGiaDichVu.setText(priceStr);
                    // Find ID by name/price or store hidden ID?
                    // Better to re-fetch or store list. For now, find by name matching (assuming unique names)
                    // Or retrieve from service list at corresponding index.
                    loadDataToId(row);
                }
            }
        });

        add(pnlTable);

        // --- RIGHT: FORM ---
        int rightX = 350;
        
        // Ten dich vu
        JLabel lblName = new JLabel("Tên dịch vụ");
        lblName.setForeground(new Color(0, 120, 215));
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblName.setBounds(rightX, 50, 100, 25);
        add(lblName);
        
        txtTenDichVu = new JTextField();
        txtTenDichVu.setBounds(rightX, 80, 250, 30);
        add(txtTenDichVu);

        // Gia dich vu
        JLabel lblPrice = new JLabel("Giá dịch vụ");
        lblPrice.setForeground(new Color(0, 120, 215));
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPrice.setBounds(rightX, 130, 100, 25);
        add(lblPrice);
        
        txtGiaDichVu = new JTextField();
        txtGiaDichVu.setBounds(rightX, 160, 250, 30);
        add(txtGiaDichVu);

        // Buttons
        JButton btnSave = createButton("Lưu", new Color(0, 120, 215));
        btnSave.setBounds(rightX + 40, 250, 80, 35);
        add(btnSave);

        JButton btnDelete = createButton("Xóa", new Color(0, 120, 215)); // Same blue color
        btnDelete.setBounds(rightX + 140, 250, 80, 35);
        add(btnDelete);

        JButton btnRefresh = new JButton("↺");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnRefresh.setBounds(rightX + 240, 250, 40, 35);
        btnRefresh.setBackground(Color.WHITE);
        btnRefresh.setBorder(null);
        add(btnRefresh);

        // Events
        btnSave.addActionListener(e -> save());
        btnDelete.addActionListener(e -> delete());
        btnRefresh.addActionListener(e -> refresh());

        loadData();
    }
    
    // Store IDs corresponding to table rows
    private List<DichVu> dataList;

    private void loadData() {
        dataList = dichVuService.getAllDichVu();
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        
        for (DichVu dv : dataList) {
            model.addRow(new Object[]{
                dv.getTenDichVu(),
                df.format(dv.getGia())
            });
        }
    }
    
    private void loadDataToId(int row) {
        if (row >= 0 && row < dataList.size()) {
            currentId = dataList.get(row).getId();
        }
    }

    private void save() {
        try {
            String name = txtTenDichVu.getText().trim();
            String priceStr = txtGiaDichVu.getText().trim();
            
            if (name.isEmpty() || priceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            
            DichVu dv = new DichVu();
            if (currentId != null) dv.setId(currentId);
            dv.setTenDichVu(name);
            dv.setGia(new BigDecimal(priceStr));
            
            dichVuService.saveDichVu(dv);
            JOptionPane.showMessageDialog(this, "Lưu thành công!");
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void delete() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dịch vụ để xóa!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?") == JOptionPane.YES_OPTION) {
            dichVuService.deleteDichVu(currentId);
            refresh();
        }
    }

    private void refresh() {
        txtTenDichVu.setText("");
        txtGiaDichVu.setText("");
        currentId = null;
        loadData();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }
}
