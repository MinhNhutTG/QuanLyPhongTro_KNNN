package com.quanlyphongtro.ui.Invoice;

import com.quanlyphongtro.dto.HoaDonDto;
import com.quanlyphongtro.dto.DichVuPhongDto;
import com.quanlyphongtro.service.ChiTietHopDongService;
import com.quanlyphongtro.service.EmailService;
import com.quanlyphongtro.service.ConfigService;
import com.quanlyphongtro.models.KhachThue;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

public class InvoiceDetail extends JDialog {

    private HoaDonDto hoaDon;
    private final EmailService emailService;
    private final ChiTietHopDongService chiTietHopDongService;
    private final ConfigService configService;

    public InvoiceDetail(Frame parent, HoaDonDto hoaDon, EmailService emailService, ChiTietHopDongService chiTietHopDongService, ConfigService configService) {
        super(parent, "Chi tiết hóa đơn", true);
        this.hoaDon = hoaDon;
        this.emailService = emailService;
        this.chiTietHopDongService = chiTietHopDongService;
        this.configService = configService;

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
        btnEmail.setBounds(580, 10, 180, 30);
        btnEmail.addActionListener(e -> sendEmail());
        mainPanel.add(btnEmail);

        // Section: Hóa đơn Info
        JPanel pnlInfo = createGroupPanel("Thông tin Hóa đơn", 20, 50, 740, 80);
        mainPanel.add(pnlInfo);
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateStr = hoaDon.getNgayLapHoaDon() != null ? hoaDon.getNgayLapHoaDon().format(dtf) : "";

        addLabel(pnlInfo, "Mã hóa đơn: " + hoaDon.getIdHoaDon(), 20, 20, true);
        addLabel(pnlInfo, "Mã dịch vụ: " + (hoaDon.getDichVuPhong() != null ? hoaDon.getDichVuPhong().getId() : ""), 20, 50, true);
        
        addLabel(pnlInfo, "Trạng thái: " + hoaDon.getTrangThai(), 400, 20, false);
        addLabel(pnlInfo, "Ngày lập: " + dateStr, 400, 50, false);

        // Section: Dịch vụ (Amounts)
        JPanel pnlService = createGroupPanel("Chi phí", 20, 140, 740, 180);
        mainPanel.add(pnlService);
        
        DichVuPhongDto dv = hoaDon.getDichVuPhong();
        String room = dv != null ? dv.getSoPhong() : "";
        
        addLabel(pnlService, "Phòng: " + room, 40, 30, false);
        addLabel(pnlService, "Tiêu thụ Điện: " + hoaDon.getSoDien() + " kWh", 40, 70, false);
        addLabel(pnlService, "Tiêu thụ Nước: " + hoaDon.getSoNuoc() + " m3", 40, 110, false);

        addAmountRow(pnlService, "Tiền phòng:", hoaDon.getGiaPhong(), 30);
        addAmountRow(pnlService, "Tiền điện:", hoaDon.getTienDien(), 60);
        addAmountRow(pnlService, "Tiền nước:", hoaDon.getTienNuoc(), 90);
        addAmountRow(pnlService, "Tiền mạng:", dv != null ? dv.getTienMang() : BigDecimal.ZERO, 120);
        addAmountRow(pnlService, "Phí khác:", hoaDon.getPhiKhac(), 150);
        
        // Total row
        JLabel lblTotalLabel = new JLabel("Tổng cộng:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalLabel.setForeground(Color.RED);
        lblTotalLabel.setBounds(20, 330, 100, 30);
        mainPanel.add(lblTotalLabel);
        
        JLabel lblTotalValue = new JLabel(formatMoney(hoaDon.getTongTien()) + " VND");
        lblTotalValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalValue.setForeground(Color.RED);
        lblTotalValue.setBounds(130, 330, 250, 30);
        mainPanel.add(lblTotalValue);
        
        // Section: Chi tiết chỉ số
        JPanel pnlDetail = createGroupPanel("Chỉ số chi tiết", 20, 360, 740, 100);
        mainPanel.add(pnlDetail);
        
        if (dv != null) {
            addLabel(pnlDetail, "Điện cũ: " + dv.getSoDienCu(), 50, 30, false);
            addLabel(pnlDetail, "Nước cũ: " + dv.getSoNuocCu(), 50, 60, false);
            
            addLabel(pnlDetail, "Điện mới: " + dv.getSoDienMoi(), 300, 30, false);
            addLabel(pnlDetail, "Nước mới: " + dv.getSoNuocMoi(), 300, 60, false);
            
            addLabel(pnlDetail, "Giá điện: " + formatMoney(dv.getGiaDien()) + "/kWh", 550, 30, false);
            addLabel(pnlDetail, "Giá nước: " + formatMoney(dv.getGiaNuoc()) + "/m3", 550, 60, false);
        }

        // Section: Ghi chú
        JPanel pnlNote = createGroupPanel("Ghi chú", 20, 470, 740, 80);
        mainPanel.add(pnlNote);
        JTextArea txtNote = new JTextArea(hoaDon.getGhiChu());
        txtNote.setEditable(false);
        txtNote.setLineWrap(true);
        txtNote.setBorder(new LineBorder(Color.GRAY));
        txtNote.setBounds(20, 25, 700, 40);
        pnlNote.add(txtNote);
    }
    
    private void sendEmail() {
        new Thread(() -> {
            try {
                if (hoaDon.getDichVuPhong() == null || hoaDon.getDichVuPhong().getMaHopDong() == null) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin hợp đồng trong hóa đơn này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String maHopDong = hoaDon.getDichVuPhong().getMaHopDong();
                KhachThue khach = chiTietHopDongService.getKhachThueByHopDong(maHopDong);

                if (khach == null || khach.getEmail() == null || khach.getEmail().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Khách thuê không có email hoặc không tìm thấy khách thuê!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Get Config
                com.quanlyphongtro.models.Config config = configService.getConfig();
                if (config == null) {
                    JOptionPane.showMessageDialog(this, "Chưa cấu hình thông tin nhà trọ (Config)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                DichVuPhongDto hs = hoaDon.getDichVuPhong();

                // Safe data preparation
                String tenNhaTro = config.getTenNhaTro() != null ? config.getTenNhaTro() : "";
                String soTaiKhoan = config.getSoTaiKhoan() != null ? config.getSoTaiKhoan() : "";
                String tenNganHang = config.getTenNganHang() != null ? config.getTenNganHang() : "";
                String tenTaiKhoan = config.getTenTaiKhoan() != null ? config.getTenTaiKhoan() : "";
                
                String maHoaDon = hoaDon.getIdHoaDon() != null ? hoaDon.getIdHoaDon() : "";
                String trangThai = hoaDon.getTrangThai() != null ? hoaDon.getTrangThai() : "";
                String ngayLap = hoaDon.getNgayLapHoaDon() != null ? hoaDon.getNgayLapHoaDon().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
                
                String soPhong = (hs != null && hs.getSoPhong() != null) ? hs.getSoPhong() : "";
                
                String tienPhong = formatMoney(hoaDon.getGiaPhong());
                String tienDien = formatMoney(hoaDon.getTienDien());
                String tienNuoc = formatMoney(hoaDon.getTienNuoc());
                String tienMang = formatMoney(hs != null ? hs.getTienMang() : BigDecimal.ZERO);
                String phiKhac = formatMoney(hoaDon.getPhiKhac());
                String tongTien = formatMoney(hoaDon.getTongTien());
                String ghiChu = hoaDon.getGhiChu() != null ? hoaDon.getGhiChu() : "";

                String htmlContent = "<!DOCTYPE html>"
                        + "<html lang='vi'>"
                        + "<head>"
                        + "    <meta charset='UTF-8'>"
                        + "    <style>"
                        + "        body { font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px; }"
                        + "        .invoice-container { width: 600px; background: #fff; padding: 20px; margin: auto; border-radius: 10px; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); }"
                        + "        .header { text-align: center; font-size: 24px; font-weight: bold; color: #2e8b57; margin-bottom: 15px; }"
                        + "        .section { border: 1px solid #ddd; padding: 10px; border-radius: 8px; margin-bottom: 10px; }"
                        + "        .section h3 { color: #2e8b57; margin-top: 0; }"
                        + "        .status { color: white; background-color: red; padding: 5px 10px; border-radius: 5px; font-weight: bold; }"
                        + "        .total { color: red; font-weight: bold; font-size: 20px; }"
                        + "    </style>"
                        + "</head>"
                        + "<body>"
                        + "    <div class='invoice-container'>"
                        + "        <div class='header'>HÓA ĐƠN TIỀN TRỌ</div>"
                        + "        <div class='section'>"
                        + "            <h3>Thông tin nhà trọ</h3>"
                        + "            <p><strong>Tên trọ:</strong> " + tenNhaTro + " </p>"
                        + "            <p><strong>Số tài khoản:</strong> " + soTaiKhoan + " </p>"
                        + "            <p><strong>Ngân hàng:</strong> " + tenNganHang + "</p>"
                        + "            <p><strong>Chủ tài khoản:</strong> " + tenTaiKhoan + "</p>"
                        + "        </div>"
                        + "        <div class='section'>"
                        + "            <h3>Hóa đơn</h3>"
                        + "            <p><strong>Mã hóa đơn:</strong> " + maHoaDon + "</p>"
                        + "            <p><strong>Trạng thái:</strong> <span class='status'>" + trangThai + "</span></p>"
                        + "            <p><strong>Ngày lập:</strong> " + ngayLap + "</p>"
                        + "        </div>"
                        + "        <div class='section'>"
                        + "            <h3>Dịch vụ</h3>"
                        + "            <p><strong>Số phòng:</strong> " + soPhong + "</p>"
                        + "            <p><strong>Tiền phòng:</strong> " + tienPhong + " VNĐ</p>"
                        + "            <p><strong>Tiền điện:</strong> " + tienDien + " VNĐ</p>"
                        + "            <p><strong>Tiền nước:</strong> " + tienNuoc + " VNĐ</p>"
                        + "            <p><strong>Tiền mạng:</strong> " + tienMang + " VNĐ</p>"
                        + "            <p><strong>Phí khác:</strong> " + phiKhac + " VNĐ</p>"
                        + "            <p class='total'>Tổng tiền: " + tongTien + " VNĐ</p>"
                        + "            <p><strong>Ghi chú:</strong> " + ghiChu + "</p>"
                        + "        </div>"
                        + "    </div>"
                        + "</body>"
                        + "</html>";

                String subject = "Hóa Đơn Tiền Phòng";
                if(hoaDon.getNgayLapHoaDon() != null) {
                     subject += " - Tháng " + hoaDon.getNgayLapHoaDon().getMonthValue() + "/" + hoaDon.getNgayLapHoaDon().getYear();
                }
                
                emailService.sendMimeMessage(khach.getEmail(), subject, htmlContent, null);

                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Gửi hóa đơn thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE));

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    private void generatePDF(String path) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();

        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        com.itextpdf.text.Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

        Paragraph title = new Paragraph("HOA DON TIEN PHONG", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        document.add(new Paragraph("Ma Hoa Don: " + hoaDon.getIdHoaDon(), normalFont));
        document.add(new Paragraph("Ngay Lap: " + (hoaDon.getNgayLapHoaDon() != null ? hoaDon.getNgayLapHoaDon().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""), normalFont));

        String room = hoaDon.getDichVuPhong() != null ? hoaDon.getDichVuPhong().getSoPhong() : "";
        document.add(new Paragraph("Phong: " + room, normalFont));
        document.add(new Paragraph("------------------------------------------------", normalFont));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        addPdfRow(table, "Tien Phong", hoaDon.getGiaPhong());
        addPdfRow(table, "Tien Dien (" + hoaDon.getSoDien() + ")", hoaDon.getTienDien());
        addPdfRow(table, "Tien Nuoc (" + hoaDon.getSoNuoc() + ")", hoaDon.getTienNuoc());
        
        if (hoaDon.getDichVuPhong() != null) {
             addPdfRow(table, "Tien Mang", hoaDon.getDichVuPhong().getTienMang());
        }
        addPdfRow(table, "Phi Khac", hoaDon.getPhiKhac());

        document.add(table);

        Paragraph total = new Paragraph("TONG TIEN: " + formatMoney(hoaDon.getTongTien()) + " VND", titleFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);

        document.close();
    }
    
    private void addPdfRow(PdfPTable table, String name, BigDecimal value) {
        if (value == null) value = BigDecimal.ZERO;
        table.addCell(new PdfPCell(new Phrase(name)));
        PdfPCell vCell = new PdfPCell(new Phrase(formatMoney(value)));
        vCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(vCell);
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
