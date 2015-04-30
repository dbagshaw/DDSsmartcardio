package org.mdpnp.smartcardio.lock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mdpnp.smartcardio.activity.ActivityLog;
import org.mdpnp.smartcardio.email.SendEmail;

public class LockScreen {

	// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	static JDialog dialog = new JDialog();
	static JButton emergency = new JButton("BREAK THE GLASS!");

	static String buttonInfo1 = "This button will override the lock screen for access to the system.";
	String buttonInfo2 = "By Using This Button, You Are Unlocking The System In An Emergency "
			+ "And Your Credentials Will Be Logged Into A Database For Future Investigation. The "
			+ "Supervisior Will Be Notified Immediately.";

	public static void Window() {

		dialog.setLayout(new BorderLayout());
		dialog.add(buttonPanel());
		dialog.setTitle("MD PnP ICE Supervisor");
		dialog.setUndecorated(true);
		dialog.setOpacity(0.7f);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setSize(screenSize);

		dialog.add(textPanel(), BorderLayout.CENTER);
		dialog.add(buttonPanel(), BorderLayout.SOUTH);

		dialog.setResizable(false);
		// dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		

		
		// dialog.setAlwaysOnTop(true);

		

	}

	private static JPanel buttonPanel() {
		JPanel buttonPanel = new JPanel();
		// buttonPanel.setBackground(Color.YELLOW);

		emergency.setFont(new Font("sansserif", Font.BOLD, 40));
		emergency.setForeground(Color.WHITE);
		emergency.setBackground(Color.RED);
		emergency.setToolTipText(buttonInfo1);
		emergency.setVisible(false);

		buttonPanel.add(emergency);

		return buttonPanel;
	}

	private static JPanel textPanel() {
		JPanel textPanel = new JPanel(new BorderLayout());
		// textPanel.setBackground(Color.BLUE);

		JLabel text = new JLabel();
		text.setVerticalAlignment(JLabel.CENTER);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setText("LOCKED");
		text.setFont(new Font("sansserif", Font.BOLD, 80));
		text.setForeground(Color.RED);

		textPanel.add(text, BorderLayout.CENTER);

		return textPanel;
	}

	
	 public static void WindowLock(boolean lock) {
		 Window();
	  
		 dialog.setVisible(true);
		 dialog.setModalityType(ModalityType.TOOLKIT_MODAL);
	  
		 lock = true;
	  
	  }
	 

	public static void WindowUnlock() {

		/**
		 * DISPOSE_ON_CLOSE closes the JDialog and allows the program to
		 * continue to run. Alternatively, EXIT_ON_CLOSE closes the JDialog and
		 * exits the program.
		 */
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));

	}

	public static void BreakGlass(final String cardnumber, final String username) {
		final ActivityLog log = new ActivityLog();
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					
					emergency.setVisible(true);
					dialog.validate();
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		}.start();
		
		emergency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowUnlock();
				log.emergencyButtonLog(cardnumber);
				SendEmail.Email(cardnumber, username);
				// SendText.Sender(cardnumber);

			}

		});

		/**
		 * Add action listener to button. If pressed call @WindowUnlock method
		 * and log activity. Send notification to my phone.
		 */

	}
	/**
	 * BREAK THE GLASS
	 * 
	 * To override the system and gain access without permission the user must
	 * first present their badge to the reader. The window will display that
	 * access has been denied but an emergency override button will appear on
	 * the screen. Pressing the button will unlock the system and log the users
	 * card data.
	 * 
	 */

}