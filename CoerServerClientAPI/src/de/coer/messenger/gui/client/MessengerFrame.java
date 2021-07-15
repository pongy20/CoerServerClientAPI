package de.coer.messenger.gui.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import de.coer.messenger.CoerMessengerInfo;
import de.coer.messenger.client.MessengerClient;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.DropMode;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MessengerFrame extends JFrame {

	private JPanel contentPane;
	public MessengesPanel msgPanel;
	private JPanel buttomPanel;
	private JTextField textField;
	private JButton sendButton;
	
	private MessengerClient client;

	/**
	 * Create the frame.
	 */
	public MessengerFrame(MessengerClient client) {
		this.client = client;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 517, 500);
		setTitle(CoerMessengerInfo.frameName);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel headerLabel = new JLabel("Coer Messenger");
		headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerLabel.setFont(new Font("Goudy Stout", Font.PLAIN, 35));
		contentPane.add(headerLabel, BorderLayout.NORTH);
		
		msgPanel = new MessengesPanel();
		contentPane.add(msgPanel);
		
		buttomPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttomPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPane.add(buttomPanel, BorderLayout.SOUTH);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (textField.getText().isEmpty() || textField.getText().length() >= 60) {
					sendButton.setEnabled(false);
				} else {
					sendButton.setEnabled(true);
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (sendButton.isEnabled()) performSend();
				}
			}
		});
		textField.setColumns(50);
		textField.setToolTipText("");
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 12));
		buttomPanel.add(textField);
		
		sendButton = new JButton("senden");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performSend();
			}
		});
		sendButton.setHorizontalAlignment(SwingConstants.RIGHT);
		sendButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttomPanel.add(sendButton);
		
		if (textField.getText() == null || textField.getText().isEmpty()) {
			sendButton.setEnabled(false);
		}
		
		setMinimumSize(new Dimension(300, 350));
		pack();
	}
	
	public void performSend() {
		if (textField.getText().isEmpty()) {
			return;
		}
		String msg = textField.getText();
		client.sendMessage(msg);
		textField.setText("");
		sendButton.setEnabled(false);
	}
}
