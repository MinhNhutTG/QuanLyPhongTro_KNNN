package com.quanlyphongtro.ui.Invoice;

import com.quanlyphongtro.models.HoaDon;
import com.quanlyphongtro.service.HoaDonService;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.Font; // Explicit import for UI Font
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

@Component
public class panel_invoice extends JPanel {

    private static final long serialVersionUID = 1L;
    private final HoaDonService hoaDonService;
    private final com.quanlyphongtro.service.HopDongThueSevice hopDongService;
    private final com.quanlyphongtro.service.DichVuPhongService dichVuPhongService;
    private final com.quanlyphongtro.service.PhongService phongService;

    private JTextField txtSearch;
    private JTable table;
    private DefaultTableModel model;
    private JSpinner spinnerDateFilter;
    
    // Hệ thống màu sắc đồng bộ
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Blue
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);    // Green
    private final Color DANGER_COLOR = new Color(231, 76, 60);     // Red
    private final Color WARNING_COLOR = new Color(241, 196, 15);   // Yellow
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    @Autowired
    public panel_invoice(HoaDonService hoaDonService, com.quanlyphongtro.service.HopDongThueSevice hopDongService, com.quanlyphongtro.service.DichVuPhongService dichVuPhongService, com.quanlyphongtro.service.PhongService phongService) {
        this.hoaDonService = hoaDonService;
        this.hopDongService = hopDongService;
        this.dichVuPhongService = dichVuPhongService;
        this.phongService = phongService;

        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // --- 1. TOP: TIÊU ĐỀ VÀ BỘ LỌC TỔNG QUÁT ---
        add(createHeaderPanel(), BorderLayout.NORTH);

        // --- 2. CENTER: BẢNG DỮ LIỆU VÀ THANH CÔNG CỤ ---
        JPanel panelContent = new JPanel(new BorderLayout(20, 0));
        panelContent.setOpaque(false);
        
        panelContent.add(createTableSection(), BorderLayout.CENTER);
        panelContent.add(createActionSidebar(), BorderLayout.EAST);

        add(panelContent, BorderLayout.CENTER);

        loadData();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        // Tiêu đề
        JLabel lblTitle = new JLabel("Quản Lý Hóa Đơn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(new Color(33, 37, 41));
        header.add(lblTitle, BorderLayout.WEST);

        // Bộ lọc nhanh
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        filterPanel.setOpaque(false);

        filterPanel.add(new JLabel("Tháng/Năm:"));
        spinnerDateFilter = new JSpinner(new SpinnerDateModel());
        spinnerDateFilter.setEditor(new JSpinner.DateEditor(spinnerDateFilter, "MM/yyyy"));
        spinnerDateFilter.setPreferredSize(new Dimension(120, 35));
        filterPanel.add(spinnerDateFilter);
        
        JButton btnFilterDate = new JButton("Lọc"); // Small filter button
        btnFilterDate.addActionListener(e -> {
             Date date = (Date) spinnerDateFilter.getValue();
             Calendar cal = Calendar.getInstance(); cal.setTime(date);
             int m = cal.get(Calendar.MONTH) + 1;
             int y = cal.get(Calendar.YEAR);
             updateTable(hoaDonService.getHoaDonByMonth(m, y));
        });
        filterPanel.add(btnFilterDate);

        txtSearch = new JTextField(18);
        txtSearch.setPreferredSize(new Dimension(200, 35));
        txtSearch.setToolTipText("Tìm mã HD, tên khách...");
        filterPanel.add(txtSearch);

        JButton btnSearch = createStyledButton("Tìm kiếm", PRIMARY_COLOR, Color.WHITE);
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            updateTable(hoaDonService.searchHoaDon(keyword));
        });
        filterPanel.add(btnSearch);

        header.add(filterPanel, BorderLayout.EAST);
        return header;
    }

    private JPanel createTableSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);

        // Filter trạng thái
        JPanel statusCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statusCard.setBackground(Color.WHITE);
        statusCard.setBorder(new LineBorder(new Color(218, 220, 224), 1));

        JLabel lblStatus = new JLabel("Trạng thái thanh toán:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusCard.add(lblStatus);

        JRadioButton rdAll = new JRadioButton("Tất cả", true);
        JRadioButton rdPaid = new JRadioButton("Đã Thanh Toán");
        JRadioButton rdUnpaid = new JRadioButton("Chưa Thanh Toán");
        
        ButtonGroup bg = new ButtonGroup();
        ActionListener filterStatus = e -> {
            String status = e.getActionCommand();
            updateTable(hoaDonService.filterByTrangThai(status));
        };

        rdAll.setActionCommand("Tất cả"); rdAll.addActionListener(filterStatus);
        rdPaid.setActionCommand("Đã Thanh Toán"); rdPaid.addActionListener(filterStatus);
        rdUnpaid.setActionCommand("Chưa Thanh Toán"); rdUnpaid.addActionListener(filterStatus);

        for (JRadioButton rd : new JRadioButton[]{rdAll, rdPaid, rdUnpaid}) {
            rd.setBackground(Color.WHITE);
            rd.setFont(MAIN_FONT);
            bg.add(rd);
            statusCard.add(rd);
        }

        // Bảng dữ liệu
        String[] columns = {"Mã hóa đơn", "Mã dịch vụ", "Số phòng", "Số điện", "Tiền điện", "Số nước", "Tiền nước", "Phí Khác", "Tổng tiền", "Trạng thái", "Giá phòng", "Ngày lập hóa đơn", "Ghi chú"};
        model = new DefaultTableModel(columns, 0) {
              @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        styleTable(table);
        
        // Double click to view details
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                     viewDetail();
                }
            }
        });
        
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(new Color(218, 220, 224), 1));
        scroll.getViewport().setBackground(Color.WHITE);

        panel.add(statusCard, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActionSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(Color.WHITE);
        sidebar.setBorder(new CompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        sidebar.setLayout(new GridLayout(7, 1, 0, 12));

        JLabel lblAction = new JLabel("THAO TÁC");
        lblAction.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAction.setHorizontalAlignment(SwingConstants.CENTER);
        sidebar.add(lblAction);

        JButton btnAuto = createStyledButton("Tạo HD tự động", SUCCESS_COLOR, Color.WHITE);
        btnAuto.setToolTipText("Tạo hóa đơn cho các dịch vụ đang chờ");
        btnAuto.addActionListener(e -> {
             try {
                // Call auto create - logic updated to find "Chờ lập hoá đơn"
                hoaDonService.autoCreateInvoices(0, 0); 
                JOptionPane.showMessageDialog(this, "Đã tạo hóa đơn cho các dịch vụ đang chờ!");
                loadData();
             } catch (Exception ex) {
                 JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
                 ex.printStackTrace();
             }
        });
        sidebar.add(btnAuto);

        JButton btnAdd = createStyledButton("Tạo hóa đơn", new Color(46, 204, 113), Color.WHITE);
        btnAdd.addActionListener(e -> {
            AddEditInvoice dialog = new AddEditInvoice((Frame) SwingUtilities.getWindowAncestor(this), hoaDonService, hopDongService, dichVuPhongService, phongService, null, this::loadData);
            dialog.setVisible(true);
        });
        sidebar.add(btnAdd);

        JButton btnEdit = createStyledButton("Chỉnh sửa", WARNING_COLOR, new Color(50, 50, 50));
        btnEdit.addActionListener(e -> {
            String id = getSelectedId();
            if (id == null) { JOptionPane.showMessageDialog(this, "Chọn hóa đơn để sửa"); return; }
            com.quanlyphongtro.dto.HoaDonDto hd = hoaDonService.getHoaDonById(id);
            if (hd != null) {
                new AddEditInvoice((JFrame)SwingUtilities.getWindowAncestor(this), hoaDonService, hopDongService, dichVuPhongService, phongService, hd, this::loadData).setVisible(true);
            }
        });
        sidebar.add(btnEdit);

        JButton btnDetail = createStyledButton("Xem chi tiết",  new Color(23, 162, 184), Color.WHITE);
        btnDetail.addActionListener(e -> viewDetail());
        sidebar.add(btnDetail);

        JButton btnDelete = createStyledButton("Xóa hóa đơn", DANGER_COLOR, Color.WHITE);
        btnDelete.addActionListener(e -> deleteInvoice());
        sidebar.add(btnDelete);

        JButton btnPrint = createStyledButton("In hóa đơn", new Color(108, 117, 125), Color.WHITE);
        btnPrint.addActionListener(e -> printInvoice());
        sidebar.add(btnPrint);

        return sidebar;
    }
    
    private void loadData() {
        updateTable(hoaDonService.getAllHoaDon());
    }
    
    private void updateTable(List<com.quanlyphongtro.dto.HoaDonDto> list) {
        model.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#,###");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        list.sort((h1, h2) -> {
            if (h1.getNgayLapHoaDon() == null || h2.getNgayLapHoaDon() == null) return 0;
            return h2.getNgayLapHoaDon().compareTo(h1.getNgayLapHoaDon());
        });

        for (com.quanlyphongtro.dto.HoaDonDto hd : list) {
            String room = (hd.getDichVuPhong() != null && hd.getDichVuPhong().getSoPhong() != null) 
                          ? hd.getDichVuPhong().getSoPhong() : "N/A";
            Integer idDichVu = (hd.getDichVuPhong() != null) ? hd.getDichVuPhong().getId() : null;
            
            model.addRow(new Object[]{
                hd.getIdHoaDon(),
                idDichVu,
                room,
                hd.getSoDien(),
                hd.getTienDien(),
                hd.getSoNuoc(),
                hd.getTienNuoc(),
                hd.getPhiKhac(),
                hd.getTongTien(),
                hd.getTrangThai(),
                hd.getGiaPhong(),
                hd.getNgayLapHoaDon() != null ? hd.getNgayLapHoaDon().format(dtf) : "",
                hd.getGhiChu()
            });
        }
    }
    
    private String getSelectedId() {
        int row = table.getSelectedRow();
        if (row == -1) return null;
        return model.getValueAt(row, 0).toString();
    }
    
    private void viewDetail() {
        String id = getSelectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return;
        }
        com.quanlyphongtro.dto.HoaDonDto hd = hoaDonService.getHoaDonById(id);
        if (hd != null) {
            InvoiceDetail dialog = new InvoiceDetail((Frame) SwingUtilities.getWindowAncestor(this), hd);
            dialog.setVisible(true);
        }
    }
    
    private void deleteInvoice() {
        String id = getSelectedId();
        if (id == null) {
             JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
             return;
        }
        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hóa đơn " + id + "?") == JOptionPane.YES_OPTION) {
            hoaDonService.deleteHoaDon(id);
            loadData();
        }
    }
    
    private void printInvoice() {
        String id = getSelectedId();
        if (id == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần in!");
            return;
        }
        
        com.quanlyphongtro.dto.HoaDonDto hd = hoaDonService.getHoaDonById(id);
        if (hd == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu hóa đơn PDF");
        fileChooser.setSelectedFile(new File("HoaDon_" + id + ".pdf"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.endsWith(".pdf")) path += ".pdf";
                
                generatePDF(hd, path);
                
                JOptionPane.showMessageDialog(this, "Xuất PDF thành công!\nĐường dẫn: " + path);
                
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(path));
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất PDF: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
    private void generatePDF(com.quanlyphongtro.dto.HoaDonDto hd, String path) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        com.itextpdf.text.Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
        
        Paragraph title = new Paragraph("HOA DON TIEN PHONG", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        document.add(new Paragraph("Ma Hoa Don: " + hd.getIdHoaDon(), normalFont));
        document.add(new Paragraph("Ngay Lap: " + (hd.getNgayLapHoaDon() != null ? hd.getNgayLapHoaDon().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""), normalFont));
        
        String room = hd.getDichVuPhong() != null ? hd.getDichVuPhong().getSoPhong() : "";
        document.add(new Paragraph("Phong: " + room, normalFont));
        document.add(new Paragraph("------------------------------------------------", normalFont));
        
        PdfPCell vCell;
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);
        
        addPdfRow(table, "Tien Phong", hd.getGiaPhong());
        addPdfRow(table, "Tien Dien (" + hd.getSoDien() + ")", hd.getTienDien());
        addPdfRow(table, "Tien Nuoc (" + hd.getSoNuoc() + ")", hd.getTienNuoc());
        
        if (hd.getDichVuPhong() != null) {
             addPdfRow(table, "Tien Mang", hd.getDichVuPhong().getTienMang());
        }
        addPdfRow(table, "Phi Khac", hd.getPhiKhac());
        
        document.add(table);
        
        Paragraph total = new Paragraph("TONG TIEN: " + new DecimalFormat("#,###").format(hd.getTongTien()) + " VND", titleFont);
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(total);
        
        document.close();
    }
    
    private void addPdfRow(PdfPTable table, String name, BigDecimal value) {
        if (value == null) value = BigDecimal.ZERO;
        table.addCell(new PdfPCell(new Phrase(name)));
        PdfPCell vCell = new PdfPCell(new Phrase(new DecimalFormat("#,###").format(value)));
        vCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(vCell);
    }

    // Styles helpers
    private void styleTable(JTable table) {
        com.quanlyphongtro.utils.TableUtils.applyStandardStyling(table);
        
        // "Mã hóa đơn", "Mã dịch vụ", "Số phòng", "Số điện", "Tiền điện", "Số nước", "Tiền nước", "Phí Khác", "Tổng tiền", "Trạng thái", "Giá phòng", "Ngày lập hóa đơn", "Ghi chú"
        if (table.getColumnCount() >= 13) {
             table.getColumnModel().getColumn(0).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(1).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(2).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             
             table.getColumnModel().getColumn(3).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(4).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
             
             table.getColumnModel().getColumn(5).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(6).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
             
             table.getColumnModel().getColumn(7).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
             table.getColumnModel().getColumn(8).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
             
             table.getColumnModel().getColumn(9).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCenterRenderer());
             table.getColumnModel().getColumn(10).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getCurrencyRenderer());
             
             table.getColumnModel().getColumn(11).setCellRenderer(com.quanlyphongtro.utils.TableUtils.getDateRenderer());
             // Ghi chu index 12 - default left align is fine
        }
    }

    private JButton createStyledButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(5, 10, 5, 10));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }
}