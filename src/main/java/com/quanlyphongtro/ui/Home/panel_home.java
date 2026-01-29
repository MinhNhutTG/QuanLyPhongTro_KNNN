package com.quanlyphongtro.ui.Home;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.Box;
import java.awt.Font;
@org.springframework.stereotype.Component
public class panel_home extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public panel_home() {
		setMinimumSize(new Dimension(1000, 500));
		this.setSize(1920,1080);
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setMinimumSize(new Dimension(1000, 500));
		panel.setPreferredSize(new Dimension(9, 1080));
		panel.setBackground(new Color(255, 255, 255));
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		Component verticalStrut = Box.createVerticalStrut(20);
		verticalStrut.setMaximumSize(new Dimension(32767, 100));
		panel.add(verticalStrut);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setAlignmentX(0.5f);
		java.net.URL imgURL = getClass().getResource("/images/logo.png");

		if (imgURL != null) {
		    ImageIcon icon = new ImageIcon(imgURL);

		    Image img = icon.getImage().getScaledInstance(500, 400, Image.SCALE_SMOOTH);
		    
		    lblNewLabel.setIcon(new ImageIcon(img));
		} else {
		    lblNewLabel.setText("LOGO NOT FOUND");
		    lblNewLabel.setForeground(Color.GRAY);
		    System.err.println("Lỗi: Không tìm thấy file logo.png trong src/main/resources/images/");
		}

	
		lblNewLabel.setBorder(null);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBackground(Color.GREEN);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setPreferredSize(new Dimension(1000, 400));
		panel.add(lblNewLabel);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		verticalStrut_1.setMaximumSize(new Dimension(32767, 100));
		verticalStrut_1.setPreferredSize(new Dimension(0, 100));
		panel.add(verticalStrut_1);
		
		JLabel lblNewLabel_1 = new JLabel("RETAL ROOM MANAGEMENT");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 88));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_1.setMaximumSize(new Dimension(1920, 130));
		lblNewLabel_1.setPreferredSize(new Dimension(1920, 130));
		panel.add(lblNewLabel_1);
	}
}
