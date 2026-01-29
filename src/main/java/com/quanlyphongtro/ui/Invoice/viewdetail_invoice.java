package com.quanlyphongtro.ui.Invoice;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class viewdetail_invoice extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					viewdetail_invoice frame = new viewdetail_invoice();
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
	public viewdetail_invoice() {
		setTitle("Bill Detail");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // --- PANEL TRÊN CÙNG: NÚT GỬI EMAIL ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        JButton btnEmail = new JButton("Gửi hóa đơn qua Email");
        btnEmail.setBackground(new Color(21, 115, 71)); // Màu xanh lục
        btnEmail.setForeground(Color.WHITE);
        btnEmail.setPreferredSize(new Dimension(200, 35));
        pnlTop.add(btnEmail);
        add(pnlTop, BorderLayout.NORTH);

        // --- PANEL CHÍNH ---
        JPanel pnlMain = new JPanel();
        pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
        pnlMain.setBorder(new EmptyBorder(10, 20, 10, 20));
        add(pnlMain, BorderLayout.CENTER);

        // 1. Nhóm Hóa đơn
        JPanel pnlInfo = createGroupPanel("Hóa đơn");
        pnlInfo.setLayout(new GridLayout(2, 2, 10, 10));
        pnlInfo.add(createLabelPair("Mã hóa đơn:", "HD263104", Color.RED));
        pnlInfo.add(createLabelPair("Trạng thái hóa đơn:", "Chưa Thanh Toán", Color.BLACK));
        pnlInfo.add(createLabelPair("Mã dịch vụ:", "42203", Color.BLACK));
        pnlInfo.add(createLabelPair("Ngày lập hóa đơn:", "3/26/2025 3:30:31 PM", Color.BLACK));
        pnlMain.add(pnlInfo);

        // 2. Nhóm Dịch vụ (Tổng hợp tiền)
        JPanel pnlService = createGroupPanel("Dịch vụ");
        pnlService.setLayout(new GridLayout(6, 2, 10, 5));
        pnlService.add(createLabelPair("Số phòng:", "104", Color.BLACK));
        pnlService.add(createLabelPair("Tiền phòng:", "1,500,000 VND", Color.BLACK));
        pnlService.add(createLabelPair("Số điện:", "8", Color.BLACK));
        pnlService.add(createLabelPair("Tiền điện:", "32,000 VND", Color.BLACK));
        pnlService.add(createLabelPair("Số nước:", "2", Color.BLACK));
        pnlService.add(createLabelPair("Tiền nước:", "24,000 VND", Color.BLACK));
        pnlService.add(new JLabel("")); // Empty cell
        pnlService.add(createLabelPair("Tiền mạng:", "30,000 VND", Color.BLACK));
        pnlService.add(new JLabel("")); // Empty cell
        pnlService.add(createLabelPair("Tiền khác:", "0 VND", Color.BLACK));
        pnlService.add(new JLabel("")); // Empty cell
        pnlService.add(createLabelPair("Tổng tiền:", "1,586,000 VND", Color.RED, true));
        pnlMain.add(pnlService);

        // 3. Nhóm Chi tiết chỉ số
        JPanel pnlSubDetail = createGroupPanel("Chi tiết dịch vụ");
        pnlSubDetail.setLayout(new GridLayout(2, 3, 10, 10));
        pnlSubDetail.add(createLabelPair("Số điện cũ:", "21", Color.BLACK));
        pnlSubDetail.add(createLabelPair("Số điện mới:", "29", Color.BLACK));
        pnlSubDetail.add(createLabelPair("Giá điện:", "4,000 VND", Color.BLACK));
        pnlSubDetail.add(createLabelPair("Số nước cũ:", "4", Color.BLACK));
        pnlSubDetail.add(createLabelPair("Số nước mới:", "6", Color.BLACK));
        pnlSubDetail.add(createLabelPair("Giá nước:", "12,000 VND", Color.BLACK));
        pnlMain.add(pnlSubDetail);

        // 4. Nhóm Ghi chú
        JPanel pnlNote = createGroupPanel("Ghi chú");
        JTextArea txtNote = new JTextArea(3, 20);
        txtNote.setBorder(new LineBorder(Color.LIGHT_GRAY));
        pnlNote.add(new JScrollPane(txtNote));
        pnlMain.add(pnlNote);
	}
	private JPanel createGroupPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        TitledBorder border = new TitledBorder(title);
        border.setTitleColor(new Color(0, 153, 0)); // Xanh lá
        p.setBorder(border);
        p.setMaximumSize(new Dimension(850, 200));
        return p;
    }

    // Hàm tạo cặp nhãn Label - Value
    private JPanel createLabelPair(String key, String value, Color valueColor) {
        return createLabelPair(key, value, valueColor, false);
    }

    private JPanel createLabelPair(String key, String value, Color valueColor, boolean isBold) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        JLabel lblKey = new JLabel(key);
        lblKey.setPreferredSize(new Dimension(150, 25));
        lblKey.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setForeground(valueColor);
        if (isBold) lblValue.setFont(new Font("SansSerif", Font.BOLD, 16));
        else lblValue.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        p.add(lblKey);
        p.add(lblValue);
        return p;
    }

}
