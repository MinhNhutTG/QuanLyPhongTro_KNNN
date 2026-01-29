package com.quanlyphongtro.ui.Room;

import com.quanlyphongtro.config.SpringContext;
import com.quanlyphongtro.dto.LoaiPhongDto;
import com.quanlyphongtro.service.LoaiPhongService;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;


public class add_edit_room_typeroom extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private final LoaiPhongService loaiPhongService;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtMaLoai, txtTenLoai, txtGia,txtSoLuongToiDa;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					add_edit_room_typeroom frame = new add_edit_room_typeroom();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public add_edit_room_typeroom() {
        this.loaiPhongService = SpringContext.getBean(LoaiPhongService.class);
		setTitle("ModifyTypeRoom");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- CỘT TRÁI: BẢNG DỮ LIỆU ---
        String[] cols = {"Mã Loại", "Tên Loại", "Giá" , "Số lượng người tối đa"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.6; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(scroll, gbc);

        // --- CỘT PHẢI: FORM NHẬP ---
        JPanel rightPanel = new JPanel(null);
        gbc.gridx = 1; gbc.weightx = 0.4;
        add(rightPanel, gbc);

        // Các ô Input dùng TitledBorder
        txtMaLoai = createInput(rightPanel, "Mã Loại", 10);
        txtTenLoai = createInput(rightPanel, "Tên Loại", 80);
        txtGia = createInput(rightPanel, "Giá", 150);
        txtSoLuongToiDa = createInput(rightPanel,"Số lượng tối đa",220);

        // Nút bấm
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(0, 120, 215));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setBounds(10, 300, 100, 40);

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(0, 120, 215));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBounds(120, 300, 100, 40);

        JButton btnReset = new JButton("🔄");
        btnReset.setBounds(230, 300, 50, 40);

        rightPanel.add(btnLuu);
        rightPanel.add(btnXoa);
        rightPanel.add(btnReset);

        loadData();

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMaLoai.setText(model.getValueAt(row, 0).toString());
                txtTenLoai.setText(model.getValueAt(row, 1).toString());
                txtGia.setText(model.getValueAt(row, 2).toString().replace(",", ""));
                txtMaLoai.setEditable(false); // Không cho sửa mã khi update
                txtSoLuongToiDa.setText(model.getValueAt(row,3).toString());
            }
        });
        // Sự kiện Lưu
        btnLuu.addActionListener(e -> {
            try {
                LoaiPhongDto dto = new LoaiPhongDto();
                dto.setMaLoai(txtMaLoai.getText());
                dto.setTenLoai(txtTenLoai.getText());
                dto.setGia(new BigDecimal(txtGia.getText()));
                dto.setSoNguoiToiDa(Integer.parseInt(txtSoLuongToiDa.getText().trim()));

                loaiPhongService.saveLoaiPhong(dto);
                JOptionPane.showMessageDialog(this, "Lưu thành công!");
                loadData();
                resetForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });
        // Sự kiện Xóa
        btnXoa.addActionListener(e -> {
            String maLoai = txtMaLoai.getText();
            if (maLoai.isEmpty()) return;

            int confirm = JOptionPane.showConfirmDialog(this, "Xóa loại phòng này?");
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    loaiPhongService.deleteLoaiPhong(maLoai);
                    loadData();
                    resetForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Không thể xóa (có thể đang được sử dụng).");
                }
            }
        });

        btnReset.addActionListener(e -> resetForm());
    }
    private JTextField createInput(JPanel panel, String title, int y) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(title));
        p.setBounds(10, y, 280, 55);
        JTextField txt = new JTextField();
        p.add(txt);
        panel.add(p);
        return txt;
    }
    private void loadData() {
        model.setRowCount(0);
        List<LoaiPhongDto> listLoai = loaiPhongService.getAllLoaiPhong();
        for (LoaiPhongDto dto : listLoai) {
            model.addRow(new Object[]{dto.getMaLoai(), dto.getTenLoai(), dto.getGia(),dto.getSoNguoiToiDa()});
        }
    }
    private void resetForm() {
        txtMaLoai.setText("");
        txtMaLoai.setEditable(true);
        txtTenLoai.setText("");
        txtGia.setText("");
        txtSoLuongToiDa.setText("");
        table.clearSelection();
    }
    }



