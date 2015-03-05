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
	static Manager manager = new Manager();

	// static GridBagConstraints constraints = new GridBagConstraints();

	public static void NotificationWindow() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(300, 150);
		frame.setLayout(new GridBagLayout());
		// GridBagConstraints constraints = new GridBagConstraints();
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
					Thread.sleep(2000); // time after which pop up will be
										// disappeared.
					frame.dispose();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}

	public static void AccessGrantedNotification(String ag, String UID) {
		CardDTO cardDto = manager.findByUID(UID);
		frame.repaint();
		frame.validate();
		NotificationWindow();
		messageLabel.setText(ag /*+ cardDto.getUserName()*/);

	}

	public static void AccessDeniedNotification(String ad, String UID) {
		frame.repaint();
		frame.validate();
		NotificationWindow();
		messageLabel.setText(ad + "Unauthorized User");
	}

	public static void InitialAccessDeniedNotification(String iad, String UID) {
		CardDTO cardDto = manager.findByUID(UID);
		frame.repaint();
		frame.validate();
		NotificationWindow();
		messageLabel.setText(iad /*+ cardDto.getUserName()*/);
	}

	public static void NotificationSent(String notice) {
		frame.repaint();
		frame.validate();
		NotificationWindow();
		messageLabel.setText(notice);
	}

}
