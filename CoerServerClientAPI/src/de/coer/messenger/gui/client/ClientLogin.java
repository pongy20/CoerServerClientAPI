package de.coer.messenger.gui.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import de.coer.messenger.CoerMessengerInfo;
import de.coer.messenger.client.MessengerClient;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientLogin extends JFrame {

	private static final long serialVersionUID = 6973947184292655981L;
	private JPanel contentPane;
	private JTextField usernameField;
	private JButton loginButton;
	private JLabel outputLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLogin frame = new ClientLogin();
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
	public ClientLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(CoerMessengerInfo.frameName);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(20, 15, 15, 15));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel headerLabel = new JLabel("Coer Messenger");
		headerLabel.setFont(new Font("Goudy Stout", Font.PLAIN, 35));
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_headerLabel = new GridBagConstraints();
		gbc_headerLabel.insets = new Insets(0, 0, 5, 0);
		gbc_headerLabel.gridx = 0;
		gbc_headerLabel.gridy = 0;
		contentPane.add(headerLabel, gbc_headerLabel);
		
		JLabel coerDevLabel = new JLabel("@CoerDevelopment 2021");
		coerDevLabel.setFont(new Font("Tahoma", Font.PLAIN, 10));
		coerDevLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_coerDevLabel = new GridBagConstraints();
		gbc_coerDevLabel.insets = new Insets(0, 0, 5, 0);
		gbc_coerDevLabel.gridx = 0;
		gbc_coerDevLabel.gridy = 1;
		contentPane.add(coerDevLabel, gbc_coerDevLabel);
		
		JLabel usernameLabel = new JLabel("Bitte gebe deinen Benutzernamen ein:");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.insets = new Insets(45, 0, 10, 0);
		gbc_usernameLabel.gridx = 0;
		gbc_usernameLabel.gridy = 2;
		contentPane.add(usernameLabel, gbc_usernameLabel);
		
		usernameField = new JTextField();
		usernameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (usernameField.getText().isEmpty() || usernameField.getText().length() + 1 < 3) {
					ClientLogin.this.loginButton.setEnabled(false);
					return;
				} else {
					ClientLogin.this.loginButton.setEnabled(true);
				}
				if (usernameField.getText().length() >= 16) {
					ClientLogin.this.loginButton.setEnabled(false);
				} else {
					ClientLogin.this.loginButton.setEnabled(true);
				}
				
			}
		});
		usernameLabel.setLabelFor(usernameField);
		usernameField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameField.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_usernameField = new GridBagConstraints();
		gbc_usernameField.insets = new Insets(0, 0, 5, 0);
		gbc_usernameField.gridx = 0;
		gbc_usernameField.gridy = 3;
		contentPane.add(usernameField, gbc_usernameField);
		usernameField.setColumns(25);
		
		outputLabel = new JLabel("");
		outputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		outputLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_outputLabel = new GridBagConstraints();
		gbc_outputLabel.insets = new Insets(0, 0, 45, 0);
		gbc_outputLabel.gridx = 0;
		gbc_outputLabel.gridy = 4;
		contentPane.add(outputLabel, gbc_outputLabel);
		
		loginButton = new JButton("einloggen");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessengerClient client;
				try {
					client = new MessengerClient("116.202.214.68", 8088, usernameField.getText());
					client.startListining();
					
					MessengerFrame frame = new MessengerFrame(client);
					frame.setLocationRelativeTo(ClientLogin.this);
					frame.setVisible(true);

					client.setFrame(frame);
				} catch (Exception e1) {
					e1.printStackTrace();
				} finally {
					setVisible(false);
				}
			}
		});
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_loginButton = new GridBagConstraints();
		gbc_loginButton.gridx = 0;
		gbc_loginButton.gridy = 5;
		contentPane.add(loginButton, gbc_loginButton);
		
		if (usernameField.getText().isEmpty()) {
			loginButton.setEnabled(false);
		}
		
		pack();
		setMinimumSize(getSize());
	}

}
