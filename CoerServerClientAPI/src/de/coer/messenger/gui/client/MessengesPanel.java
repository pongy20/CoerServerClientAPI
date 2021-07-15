package de.coer.messenger.gui.client;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.ScrollPaneConstants;

public class MessengesPanel extends JScrollPane {

	JTextArea textArea;
	
	/**
	 * Create the panel.
	 */
	public MessengesPanel() {
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setMinimumSize(new Dimension(200, 250));
		
		textArea = new JTextArea(40, 30);
		textArea.setEditable(false);
		
		setViewportView(textArea);
	}
	
	public void addMessage(String msg) {
		textArea.setText(textArea.getText() + msg + "\n");
	}

}
