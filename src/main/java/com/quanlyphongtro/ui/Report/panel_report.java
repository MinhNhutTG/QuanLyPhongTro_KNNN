package com.quanlyphongtro.ui.Report;

import com.quanlyphongtro.config.SpringContext;
import com.quanlyphongtro.dto.ThongKeDto;
import com.quanlyphongtro.service.ThongKeService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.axis.NumberAxis;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Calendar;
import java.util.List;

@Component
public class panel_report extends JPanel {

    private static final long serialVersionUID = 1L;
    private final ThongKeService thongKeService;
    
    // Hệ màu Dashboard hiện đại
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    
    private JPanel panelChartContainer;
    private JLabel lblChartTitle;
    private JButton btnTabOverview, btnTabRevenue, btnTabService;
    
    // --- MỚI THÊM: Combobox chọn năm ---
    private JComboBox<Integer> cboYear;
    private String currentTab = "OVERVIEW"; // Biến theo dõi tab đang mở

    public panel_report() {
        this.thongKeService = SpringContext.getBean(ThongKeService.class);
        setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(0, 0));

        // --- 1. HEADER ---
        add(createHeader(), BorderLayout.NORTH);

        // --- 2. MAIN CONTENT ---
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBorder(new EmptyBorder(25, 25, 25, 25));
        panelMain.setOpaque(false);
        add(panelMain, BorderLayout.CENTER);

        // --- 3. NAVIGATION TABS & FILTER ---
        JPanel panelNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelNav.setOpaque(false);
        
        btnTabOverview = createNavButton("Tổng quan", true);
        btnTabRevenue = createNavButton("Doanh thu", false);
        btnTabService = createNavButton("Dịch vụ", false);

        panelNav.add(btnTabOverview);
        panelNav.add(btnTabRevenue);
        panelNav.add(btnTabService);

        // --- MỚI THÊM: Khoảng cách và Combobox Năm ---
        panelNav.add(Box.createHorizontalStrut(30)); // Tạo khoảng cách
        
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlFilter.setOpaque(false);
        
        JLabel lblNam = new JLabel("Chọn năm:");
        lblNam.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNam.setForeground(new Color(100, 100, 100));
        
        // Khởi tạo combobox năm
        cboYear = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 5; i--) {
            cboYear.addItem(i);
        }
        cboYear.setPreferredSize(new Dimension(100, 35));
        cboYear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cboYear.setBackground(Color.WHITE);
        cboYear.setFocusable(false);

        pnlFilter.add(lblNam);
        pnlFilter.add(cboYear);
        panelNav.add(pnlFilter);
        // ---------------------------------------------

        panelMain.add(panelNav);
        panelMain.add(Box.createVerticalStrut(25));

        // --- 4. SUMMARY CARDS ---
        JPanel panelCards = new JPanel(new GridLayout(1, 4, 20, 0));
        panelCards.setOpaque(false);
        panelCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        String totalPhong = String.valueOf(thongKeService.getTotalPhong());
        String phongDangThue = String.valueOf(thongKeService.getPhongDaThue());
        String hoaDonThang = String.valueOf(thongKeService.getSoHoaDonThang());
        String doanhThuThang = thongKeService.getDoanhThuThangHienTai();

        panelCards.add(createStatCard("TỔNG SỐ PHÒNG", totalPhong, PRIMARY_COLOR, ""));
        panelCards.add(createStatCard("PHÒNG ĐANG THUÊ", phongDangThue, SUCCESS_COLOR, ""));
        panelCards.add(createStatCard("HÓA ĐƠN THÁNG", hoaDonThang, WARNING_COLOR, ""));
        panelCards.add(createStatCard("DOANH THU THÁNG", doanhThuThang, DANGER_COLOR, ""));

        panelMain.add(panelCards);
        panelMain.add(Box.createVerticalStrut(25));

        // --- 5. CHART AREA ---
        JPanel panelChartArea = new JPanel(new BorderLayout());
        panelChartArea.setBackground(Color.WHITE);
        panelChartArea.setBorder(new CompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel pnlChartHeader = new JPanel(new BorderLayout());
        pnlChartHeader.setOpaque(false);
        lblChartTitle = new JLabel("Phân tích doanh thu & Xu hướng");
        lblChartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlChartHeader.add(lblChartTitle, BorderLayout.WEST);
        
        panelChartArea.add(pnlChartHeader, BorderLayout.NORTH);
        
        panelChartContainer = new JPanel(new GridLayout(1, 1));
        panelChartContainer.setOpaque(false);
        panelChartArea.add(panelChartContainer, BorderLayout.CENTER);
        
        panelMain.add(panelChartArea);
        
        setupEvents();
        loadOverviewChart();
    }

    private void setupEvents() {
        btnTabOverview.addActionListener(e -> {
            currentTab = "OVERVIEW";
            setActiveTab(btnTabOverview);
            loadOverviewChart();
        });
        btnTabRevenue.addActionListener(e -> {
            currentTab = "REVENUE";
            setActiveTab(btnTabRevenue);
            loadRevenueCharts();
        });
        btnTabService.addActionListener(e -> {
            currentTab = "SERVICE";
            setActiveTab(btnTabService);
            loadServiceCharts();
        });

        // --- MỚI THÊM: Sự kiện khi chọn năm ---
        cboYear.addActionListener(e -> {
            int selectedYear = (int) cboYear.getSelectedItem();
            System.out.println("Đã chọn năm: " + selectedYear);
            
            // Reload lại tab hiện tại với năm mới
            // Lưu ý: Bạn cần sửa các hàm load...Chart() để truyền tham số year vào service
            switch (currentTab) {
                case "OVERVIEW": loadOverviewChart(); break;
                case "REVENUE": loadRevenueCharts(); break;
                case "SERVICE": loadServiceCharts(); break;
            }
        });
    }

    private void loadOverviewChart() {
        lblChartTitle.setText("Doanh thu tổng hợp theo từng phòng");
        panelChartContainer.removeAll();
        panelChartContainer.setLayout(new GridLayout(1, 1));

        int year = (int) cboYear.getSelectedItem(); // Get selected year

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<ThongKeDto> data = thongKeService.getDoanhThuPhong(year);

        for (ThongKeDto item : data) {
            dataset.addValue(item.getGiaTri(), "Doanh Thu", "P." + item.getNhan());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                null, "Phòng", "VND", dataset,
                PlotOrientation.VERTICAL, false, true, false);

        styleChart(chart);
        applyRevenueAxis(chart);
        panelChartContainer.add(new ChartPanel(chart));
        refreshChartPanel();
    }

    private void loadRevenueCharts() {
        lblChartTitle.setText("Biểu đồ doanh thu theo Tháng & Quý");
        panelChartContainer.removeAll();
        panelChartContainer.setLayout(new GridLayout(1, 2, 20, 0));

        int year = (int) cboYear.getSelectedItem(); // Get selected year

        DefaultCategoryDataset dsMonth = new DefaultCategoryDataset();
        List<ThongKeDto> dataMonth = thongKeService.getDoanhThuThang(year);
        for (ThongKeDto item : dataMonth) {
            dsMonth.addValue(item.getGiaTri(), "Doanh Thu", "Tháng " + item.getNhan());
        }
        JFreeChart chartMonth = ChartFactory.createLineChart(
                "Theo Tháng", "Tháng", "VND", dsMonth,
                PlotOrientation.VERTICAL, false, true, false);
        styleChart(chartMonth);
        applyRevenueAxis(chartMonth);

        CategoryPlot plot = chartMonth.getCategoryPlot();
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, DANGER_COLOR);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        plot.setRenderer(renderer);

        DefaultCategoryDataset dsQuarter = new DefaultCategoryDataset();
        List<ThongKeDto> dataQuarter = thongKeService.getDoanhThuQui(year);
        for (ThongKeDto item : dataQuarter) {
            dsQuarter.addValue(item.getGiaTri(), "Doanh Thu", "Quý " + item.getNhan());
        }
        JFreeChart chartQuarter = ChartFactory.createBarChart(
                "Theo Quý", "Quý", "VND", dsQuarter,
                PlotOrientation.VERTICAL, false, true, false);
        styleChart(chartQuarter);
        applyRevenueAxis(chartQuarter);

        panelChartContainer.add(new ChartPanel(chartMonth));
        panelChartContainer.add(new ChartPanel(chartQuarter));
        refreshChartPanel();
    }

    private void loadServiceCharts() {
        lblChartTitle.setText("Thống kê tiêu thụ Điện/Nước & Doanh thu dịch vụ");
        panelChartContainer.removeAll();
        panelChartContainer.setLayout(new GridLayout(1, 2, 20, 0));

        int year = (int) cboYear.getSelectedItem(); // Get selected year

        DefaultCategoryDataset dsConsump = new DefaultCategoryDataset();
        List<ThongKeDto> dataConsump = thongKeService.getTieuThuDienNuoc(year);
        for (ThongKeDto item : dataConsump) {
            dsConsump.addValue(item.getGiaTri(), "Điện (KWh)", item.getNhan());
            dsConsump.addValue(item.getGiaTri2(), "Nước (m3)", item.getNhan());
        }
        JFreeChart chartConsump = ChartFactory.createBarChart(
                "Tiêu thụ Điện & Nước", "Tháng", "VND", dsConsump,
                PlotOrientation.VERTICAL, true, true, false);
        styleChart(chartConsump);

        CategoryPlot plot = chartConsump.getCategoryPlot();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, WARNING_COLOR);
        renderer.setSeriesPaint(1, PRIMARY_COLOR);

        DefaultCategoryDataset dsServiceRev = new DefaultCategoryDataset();
        dsServiceRev.addValue(1500000, "Dịch vụ", "T1");
        dsServiceRev.addValue(1800000, "Dịch vụ", "T2");
        dsServiceRev.addValue(1200000, "Dịch vụ", "T3");
        JFreeChart chartServiceRev = ChartFactory.createBarChart(
                "Doanh thu Dịch vụ khác", "Tháng", "VND", dsServiceRev,
                PlotOrientation.VERTICAL, false, true, false);
        styleChart(chartServiceRev);
        applyRevenueAxis(chartServiceRev);

        panelChartContainer.add(new ChartPanel(chartConsump));
        panelChartContainer.add(new ChartPanel(chartServiceRev));
        refreshChartPanel();
    }

    private void refreshChartPanel() {
        panelChartContainer.revalidate();
        panelChartContainer.repaint();
    }

    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(new Color(240, 240, 240));
        plot.setRangeGridlinePaint(new Color(240, 240, 240));
        plot.setOutlineVisible(false);
    }

    private void applyRevenueAxis(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        NumberAxis axis = (NumberAxis) plot.getRangeAxis();
        
        // Define standard integer format (100,000)
        NumberFormat currencyFormat = new DecimalFormat("#,##0");
        axis.setNumberFormatOverride(currencyFormat);

        // Allow dynamic auto-scaling (Removed hardcoded setTickUnit)
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        // Ensure axis starts at 0
        axis.setAutoRangeIncludesZero(true);
    }

    private void setActiveTab(JButton activeBtn) {
        resetTabStyle(btnTabOverview);
        resetTabStyle(btnTabRevenue);
        resetTabStyle(btnTabService);

        activeBtn.setBackground(PRIMARY_COLOR);
        activeBtn.setForeground(Color.WHITE);
        activeBtn.setBorder(new LineBorder(PRIMARY_COLOR));
    }

    private void resetTabStyle(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(100, 100, 100));
        btn.setBorder(new LineBorder(new Color(218, 220, 224)));
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new MatteBorder(0, 0, 1, 0, new Color(218, 220, 224)));
        
        JLabel lblTitle = new JLabel("Báo Cáo & Thống Kê");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(new EmptyBorder(0, 25, 0, 0));
        header.add(lblTitle, BorderLayout.WEST);
        
      

        return header; // Đã sửa: Trả về header thay vì new JPanel()
    }

    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (isActive) {
            btn.setBackground(PRIMARY_COLOR);
            btn.setForeground(Color.WHITE);
            btn.setBorder(new LineBorder(PRIMARY_COLOR));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(new Color(100, 100, 100));
            btn.setBorder(new LineBorder(new Color(218, 220, 224)));
        }
        return btn;
    }

    private JPanel createStatCard(String title, String value, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel pnlText = new JPanel();
        pnlText.setLayout(new BoxLayout(pnlText, BoxLayout.Y_AXIS));
        pnlText.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(new Color(150, 150, 150));

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(new Color(50, 50, 50));

        pnlText.add(lblTitle);
        pnlText.add(Box.createVerticalStrut(5));
        pnlText.add(lblValue);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        lblIcon.setForeground(color);

        card.add(pnlText, BorderLayout.CENTER);
        card.add(lblIcon, BorderLayout.EAST);
        
        card.add(new JPanel() {{
            setBackground(color);
            setPreferredSize(new Dimension(0, 4));
        }}, BorderLayout.SOUTH);

        return card; // Đã sửa: Trả về card thay vì new JPanel()
    }
}