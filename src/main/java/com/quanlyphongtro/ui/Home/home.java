package com.quanlyphongtro.ui.Home;

import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

//import ui.Config.panel_config;
//import ui.Contract.panel_contract;
//import ui.Guest.panel_guest;
//import ui.Invoice.panel_invoice;
//import ui.Report.panel_report;
//import ui.Room.panel_room;
//import ui.Service.panel_service;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import java.awt.Color;
import javax.swing.JList;
import java.awt.Component;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.border.LineBorder;

import com.quanlyphongtro.ui.Config.panel_config;
import com.quanlyphongtro.ui.Contract.panel_contract;
import com.quanlyphongtro.ui.Guest.panel_guest;
import com.quanlyphongtro.ui.Invoice.panel_invoice;
import com.quanlyphongtro.ui.Report.panel_report;
import com.quanlyphongtro.ui.Room.panel_room;
import com.quanlyphongtro.ui.Service.panel_service;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component
public class home extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String[] lable =  {"Trang chủ", "Khách thuê", "Hợp đồng", "Phòng", "Dịch vụ", "Hóa đơn", "Báo cáo", "Cấu hình","Thoát"};
	private String[] features =  {"home", "guest", "contract", "room", "service", "invoice", "report", "config", "exit"};
	private CardLayout cardLayout;
	private JButton[] btnCategories;
	private final panel_home panelHome;
	private final panel_guest panelGuest;
	private final panel_contract panelContract;
	private final panel_room panelRoom;
	private final panel_service panelService;
	private final panel_invoice panelInvoice;
	private final panel_report panelReport;
	private final panel_config panelConfig;
	/**
	 * Create the frame.
	 */
	@Autowired
	public home(panel_home panelHome, panel_guest panelGuest, panel_contract panelContract,
				panel_room panelRoom, panel_service panelService, panel_invoice panelInvoice,
				panel_report panelReport, panel_config panelConfig) {

		this.panelHome = panelHome;
		this.panelGuest = panelGuest;
		this.panelContract = panelContract;
		this.panelRoom = panelRoom;
		this.panelService = panelService;
		this.panelInvoice = panelInvoice;
		this.panelReport = panelReport;
		this.panelConfig = panelConfig;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		this.setSize(1920,1080); 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel side_panel = new JPanel();
		side_panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		side_panel.setBackground(new Color(39, 83, 138));
		side_panel.setPreferredSize(new Dimension(300, 10));
		contentPane.add(side_panel, BorderLayout.WEST);
		side_panel.setLayout(new BoxLayout(side_panel, BoxLayout.Y_AXIS));
		
		
		
		
		JPanel main_panel = new JPanel();
		cardLayout = new CardLayout();
		main_panel.setLayout(cardLayout);
		main_panel.setPreferredSize(new Dimension(10, 60));
		contentPane.add(main_panel, BorderLayout.CENTER);

		main_panel.add(this.panelHome, "panelhome");
		main_panel.add(this.panelGuest, "panelguest");
		main_panel.add(this.panelContract, "panelcontract");
		main_panel.add(this.panelRoom, "panelroom");
		main_panel.add(this.panelService, "panelservice");
		main_panel.add(this.panelInvoice, "panelinvoice");
		main_panel.add(this.panelReport, "panelreport");
		main_panel.add(this.panelConfig, "panelconfig");
		
		JLabel lblNewLabel_1 = new JLabel("QUẢN LÝ PHÒNG TRỌ");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setHorizontalTextPosition(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 22));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblNewLabel_1.setPreferredSize(new Dimension(300, 300));
		lblNewLabel_1.setMaximumSize(new Dimension(300, 300));
		side_panel.add(lblNewLabel_1);
		
		btnCategories = new JButton[features.length];
		for (int i=0 ; i< features.length -1; i++ ) {
			final int index = i;
			btnCategories[i] = new JButton(lable[i]);
			btnCategories[i].setBorder(null);
			btnCategories[i].setBackground(new Color(255, 255, 255));
			btnCategories[i].setFont(new Font("Dialog", Font.BOLD, 18));
			btnCategories[i].setForeground(new Color(39, 83, 138));
			btnCategories[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cardLayout.show(main_panel, "panel" + features[index]);
				}
			});
			btnCategories[i].setMinimumSize(new Dimension(105, 20));
			btnCategories[i].setMaximumSize(new Dimension(400, 80));
			btnCategories[i].setAlignmentX(Component.CENTER_ALIGNMENT);
			btnCategories[i].setPreferredSize(new Dimension(30, 27));
			side_panel.add(btnCategories[i]);
		}


		JButton btnNewButton = new JButton(lable[features.length -1]);
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBorder(null);
		btnNewButton.setBackground(new Color(39, 83, 138));
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 18));
		
		btnNewButton.setMinimumSize(new Dimension(105, 20));
		btnNewButton.setMaximumSize(new Dimension(400, 80));
		btnNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnNewButton.setPreferredSize(new Dimension(30, 27));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirmed = javax.swing.JOptionPane.showConfirmDialog(null, 
					"Bạn có chắc chắn muốn thoát không?", "Xác nhận thoát",
					javax.swing.JOptionPane.YES_NO_OPTION);

				if (confirmed == javax.swing.JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		side_panel.add(btnNewButton);
		
		

	}

}
