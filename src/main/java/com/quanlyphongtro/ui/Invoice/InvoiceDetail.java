package com.quanlyphongtro.ui.Invoice;

import com.quanlyphongtro.dto.HoaDonDto;
import com.quanlyphongtro.dto.DichVuPhongDto;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InvoiceDetail extends JDialog {

    private HoaDonDto hoaDon;

    public InvoiceDetail(Frame parent, HoaDonDto hoaDon) {
        super(parent, "Bill Detail", true);
        this.hoaDon = hoaDon;

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        add(mainPanel, BorderLayout.CENTER);

        // Top Button
        JButton btnEmail = new JButton("Gửi hóa đơn qua Email");
        btnEmail.setBackground(new Color(40, 167, 69));
        btnEmail.setForeground(Color.WHITE);
        btnEmail.setBounds(600, 10, 160, 30);
        mainPanel.add(btnEmail);

        // Section: Hóa đơn Info
        JPanel pnlInfo = createGroupPanel("Hóa đơn", 20, 50, 740, 80);
        mainPanel.add(pnlInfo);
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dateStr = hoaDon.getNgayLapHoaDon() != null ? hoaDon.getNgayLapHoaDon().format(dtf) : "";

        addLabel(pnlInfo, "Mã hóa đơn: " + hoaDon.getIdHoaDon(), 20, 20, true);
        addLabel(pnlInfo, "Mã dịch vụ: " + (hoaDon.getDichVuPhong() != null ? hoaDon.getDichVuPhong().getId() : ""), 20, 50, true);
        
        addLabel(pnlInfo, "Trạng thái hóa đơn : " + hoaDon.getTrangThai(), 400, 20, false);
        addLabel(pnlInfo, "Ngày lập hóa đơn: " + dateStr, 400, 50, false);

        // Section: Dịch vụ (Amounts)
        JPanel pnlService = createGroupPanel("Dịch vụ", 20, 140, 740, 180);
        mainPanel.add(pnlService);
        
        DichVuPhongDto dv = hoaDon.getDichVuPhong();
        String room = dv != null ? dv.getSoPhong() : "";
        
        addLabel(pnlService, "Số phòng: " + room, 40, 30, false);
        addLabel(pnlService, "Số điện : " + hoaDon.getSoDien(), 40, 70, false);
        addLabel(pnlService, "Số nước : " + hoaDon.getSoNuoc(), 40, 110, false);

        addAmountRow(pnlService, "Tiền phòng:", hoaDon.getGiaPhong(), 30);
        addAmountRow(pnlService, "Tiền điện:", hoaDon.getTienDien(), 60);
        addAmountRow(pnlService, "Tiền nước:", hoaDon.getTienNuoc(), 90);
        addAmountRow(pnlService, "Tiền mạng:", dv != null ? dv.getTienMang() : BigDecimal.ZERO, 120);
        addAmountRow(pnlService, "Tiền khác:", hoaDon.getPhiKhac(), 150);
        
        // Total row at bottom of pnlService section
        JLabel lblTotalLabel = new JLabel("Tổng tiền:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalLabel.setForeground(Color.RED);
        lblTotalLabel.setBounds(20, 330, 100, 25);
        mainPanel.add(lblTotalLabel);
        
        JLabel lblTotalValue = new JLabel(formatMoney(hoaDon.getTongTien()) + " VND");
        lblTotalValue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalValue.setForeground(Color.RED);
        lblTotalValue.setBounds(130, 330, 200, 25);
        mainPanel.add(lblTotalValue);
        
        // Section: Chi tiết dịch vụ (Indexes)
        JPanel pnlDetail = createGroupPanel("Chi tiết dịch vụ", 20, 360, 740, 100);
        mainPanel.add(pnlDetail);
        
        if (dv != null) {
            addLabel(pnlDetail, "Số điện cũ :   " + dv.getSoDienCu(), 50, 30, false);
            addLabel(pnlDetail, "Số nước cũ :   " + dv.getSoNuocCu(), 50, 60, false);
            
            addLabel(pnlDetail, "Số điện mới : " + dv.getSoDienMoi(), 300, 30, false);
            addLabel(pnlDetail, "Số nước mới : " + dv.getSoNuocMoi(), 300, 60, false);
            
            addLabel(pnlDetail, "Giá điện :   " + formatMoney(dv.getGiaDien()) + " VND", 550, 30, false);
            addLabel(pnlDetail, "Giá nước :   " + formatMoney(dv.getGiaNuoc()) + " VND", 550, 60, false);
        }

        // Section: Ghi chú
        JPanel pnlNote = createGroupPanel("Ghi chú", 20, 470, 740, 80);
        mainPanel.add(pnlNote);
        JTextArea txtNote = new JTextArea(hoaDon.getGhiChu());
        txtNote.setEditable(false);
        txtNote.setBorder(new LineBorder(Color.GRAY));
        txtNote.setBounds(20, 20, 700, 40);
        pnlNote.add(txtNote);
        
        btnEmail.addActionListener(e -> JOptionPane.showMessageDialog(this, "Tính năng gửi email đang phát triển!"));
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
            new Color(40, 167, 69)
        ));
        p.setBackground(Color.WHITE);
        return p;
    }

    private void addLabel(JPanel p, String text, int x, int y, boolean isRed) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", isRed ? Font.BOLD : Font.PLAIN, 13));
        if (isRed && text.startsWith("Mã")) l.setForeground(new Color(220, 53, 69));
        l.setBounds(x, y, 300, 20);
        p.add(l);
    }

    private void addAmountRow(JPanel p, String label, BigDecimal amount, int y) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setBounds(450, y, 100, 20);
        p.add(l);
        
        JLabel v = new JLabel(formatMoney(amount) + " VND");
        v.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        v.setBounds(550, y, 150, 20);
        p.add(v);
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0";
        return new DecimalFormat("#,###").format(amount);
    }
}
