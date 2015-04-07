package org.mdpnp.smartcardio.db;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.mdpnp.smartcardio.dto.CardDTO;

public class NotificationPopUp {

	static String header = "Notification";
	static JFrame frame = new JFrame();
	static JLabel messageLabel = new JLabel();
	static EmployeeManager manager = new EmployeeManager();

	public static void NotificationWindow() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(350, 200);
		frame.setLayout(new GridBagLayout());
		frame.setTitle(header);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.add(messageLabel);

		frame.setLocation(scrSize.width - frame.getWidth(), 0);

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					frame.dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	public static void AccessNotification(String notification, String UID) {
		CardDTO cardDto = manager.findByUID(UID);
		frame.repaint();
		frame.validate();
		NotificationWindow();
		
		if (UID != null)
			messageLabel.setText(notification + cardDto.getUserName());
		else
			messageLabel.setText(notification);

	}

	/*
	 * public static void MasterNotification(String ag) { frame.repaint();
	 * frame.validate(); NotificationWindow(); messageLabel.setText(ag +
	 * "Master Key Card."); }
	 * 
	 * public static void AccessGrantedNotification(String ag, String UID) {
	 * CardDTO cardDto = manager.findByUID(UID); frame.repaint();
	 * frame.validate(); NotificationWindow(); messageLabel.setText(ag +
	 * cardDto.getUserName());
	 * 
	 * }
	 * 
	 * public static void AccessDeniedNotification(String ad, String UID) {
	 * frame.repaint(); frame.validate(); NotificationWindow();
	 * messageLabel.setText(ad + "Unauthorized User."); }
	 * 
	 * public static void InitialAccessDeniedNotification(String iad, String
	 * UID) { CardDTO cardDto = manager.findByUID(UID); frame.repaint();
	 * frame.validate(); NotificationWindow(); messageLabel.setText(iad +
	 * cardDto.getUserName()); }
	 */

	public static void NotificationSent(String notice) {
		frame.repaint();
		frame.validate();
		NotificationWindow();
		messageLabel.setText(notice);
	}

	public static void TerminalNotification(String terminalNotice) {
		frame.repaint();
		frame.validate();
		NotificationWindow();
		messageLabel.setText(terminalNotice);
	}
}
