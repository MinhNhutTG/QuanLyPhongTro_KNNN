package com.quanlyphongtro.ui.Service;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class add_edit_service extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					add_edit_service frame = new add_edit_service();
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
	public add_edit_service() {
		setTitle("PriceService");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        // --- BẢNG GIÁ DỊCH VỤ (Bên trái) ---
        String[] columns = {"Tên dịch vụ", "Giá dịch vụ"};
        Object[][] data = {
            {"Điện", "4,000"},
            {"Nước", "12,000"},
            {"wifi", "30,000"}
        };
        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable table = new JTable(model);
        
        // Header màu xanh như trong ảnh
        table.getTableHeader().setBackground(new Color(0, 120, 215));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 20, 380, 420);
        getContentPane().add(scrollPane);

        // --- FORM NHẬP LIỆU (Bên phải) ---
        // Tên dịch vụ
        JPanel pnlTen = createGroup("Tên dịch vụ", 420, 30, 340, 60);
        JTextField txtTen = new JTextField();
        txtTen.setBounds(10, 25, 320, 25);
        pnlTen.add(txtTen);
        getContentPane().add(pnlTen);

        // Giá dịch vụ
        JPanel pnlGia = createGroup("Giá dịch vụ", 420, 110, 340, 60);
        JTextField txtGia = new JTextField();
        txtGia.setBounds(10, 25, 320, 25);
        pnlGia.add(txtGia);
        getContentPane().add(pnlGia);

        // --- NÚT BẤM ---
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setBackground(new Color(0, 120, 215));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setBounds(450, 200, 110, 40);
        getContentPane().add(btnLuu);

        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(0, 120, 215));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBounds(570, 200, 110, 40);
        getContentPane().add(btnXoa);

        JButton btnReset = new JButton("🔄");
        btnReset.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        btnReset.setBounds(690, 200, 50, 40);
        btnReset.setContentAreaFilled(false); // Làm nút trong suốt như icon
        getContentPane().add(btnReset);

	}
	private JPanel createGroup(String title, int x, int y, int w, int h) {
        JPanel p = new JPanel(null);
        p.setBounds(x, y, w, h);
        TitledBorder border = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), title);
        border.setTitleColor(new Color(0, 120, 215));
        p.setBorder(border);
        return p;
    }

}
