package org.mdpnp.smartcardio.db;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.mdpnp.smartcardio.dto.CardDTO;

public class NotificationPopUp {

	String header = "Notification";
	JFrame frame = new JFrame();
	JLabel messageLabel = new JLabel();
	EmployeeManager eManager = new EmployeeManager();

	public NotificationPopUp() {
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(350, 200);
		frame.setLayout(new GridBagLayout());
		frame.setTitle(header);
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);

		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		frame.add(messageLabel);

		frame.setLocation(scrSize.width - frame.getWidth(), 0);
	}

	public void displayFor() {
		displayFor(3000L);
	}
	public void displayFor(long milliseconds) {
		frame.setVisible(true);
		Thread Notey = new Thread(() -> { 
			try {
				Thread.sleep(milliseconds);
				SwingUtilities.invokeLater(()->frame.setVisible(false));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		Notey.start();
	}

	public void accessNotification(String notification, String UID) {
		CardDTO cardDto = eManager.findByUID(UID);
		
		if (UID != null)
			messageLabel.setText(notification + cardDto.getUserName());
		else
			messageLabel.setText(notification);
		
		displayFor();
	}

	public void notificationSent(String notice) {
		messageLabel.setText(notice);
		displayFor();
	}

	public void terminalNotification(String terminalNotice) {
		messageLabel.setText(terminalNotice);
		displayFor();
	}
	
	private static final NotificationPopUp _instance = new NotificationPopUp();
	public static NotificationPopUp getInstance() {
		return _instance;
	}
}
