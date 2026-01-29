package com.quanlyphongtro.utils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TableUtils {

    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font CELL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Color HEADER_BG = Color.WHITE;
    private static final Color HEADER_FG = new Color(108, 117, 125); // Greyish
    private static final Color SELECTION_BG = new Color(232, 241, 249);
    private static final Color SELECTION_FG = Color.BLACK;

    public static void applyStandardStyling(JTable table) {
        table.setRowHeight(35);
        table.setFont(CELL_FONT);
        table.setSelectionBackground(SELECTION_BG);
        table.setSelectionForeground(SELECTION_FG);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setFont(HEADER_FONT);
        header.setForeground(HEADER_FG);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(218, 220, 224)));
    }

    public static DefaultTableCellRenderer getCenterRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        return renderer;
    }

    public static DefaultTableCellRenderer getRightRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.RIGHT);
        return renderer;
    }

    public static DefaultTableCellRenderer getCurrencyRenderer() {
        return new DefaultTableCellRenderer() {
            private final DecimalFormat df = new DecimalFormat("#,###");

            @Override
            public void setValue(Object value) {
                if (value == null) {
                    setText("");
                } else {
                    if (value instanceof BigDecimal) {
                        setText(df.format(value));
                    } else if (value instanceof Double) {
                        setText(df.format(value));
                    } else if (value instanceof Integer) {
                        setText(df.format(value));
                    } else if (value instanceof String) {
                        // Try to parse if string contains raw number? 
                        // Or just set text. For safety assume already formatted or raw string.
                        // Ideally we pass numbers.
                        setText(value.toString());
                    } else {
                        setText(value.toString());
                    }
                }
                setHorizontalAlignment(JLabel.RIGHT);
            }
        };
    }
    
    public static DefaultTableCellRenderer getDateRenderer() {
         return new DefaultTableCellRenderer() {
            private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            @Override
            public void setValue(Object value) {
               if (value instanceof LocalDateTime) {
                   setText(((LocalDateTime) value).format(dtf));
               } else if (value instanceof Date) {
                   // Fallback for util.Date if used
                   setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format((Date)value));
               } else {
                   super.setValue(value);
               }
               setHorizontalAlignment(JLabel.CENTER);
            }
         };
    }
}
