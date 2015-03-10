package org.mdpnp.smartcardio.lock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.mdpnp.smartcardio.email.SendEmail;

public class LockUnlock {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	static JDialog dialog = new JDialog();
	static JButton emergency = new JButton("BREAK THE GLASS!");

	static String buttonInfo1 = "Press this button to override the lock screen and access the system.";
	static String buttonInfo2 = "By Using This Button, You Are Unlocking The System In An Emergency "
			+ "And Your Credentials Will Be Logged Into A Database For Future Investigation. The "
			+ "Supervisior Will Be Notified Immediately.";
//	static boolean lock;

	static String access;

	public static void Window() {

		dialog.setLayout(new BorderLayout());
		dialog.setTitle("MD PnP ICE Supervisor");
		dialog.setUndecorated(true);
		dialog.setOpacity(0.5f);

		JPanel panel = new JPanel();

		emergency.setFont(new Font("sansserif", Font.BOLD, 40));
		emergency.setForeground(Color.WHITE);
		emergency.setBackground(Color.RED);
		emergency.setToolTipText(buttonInfo2);
		panel.add(emergency);

		dialog.add(panel, BorderLayout.SOUTH);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setSize(screenSize);

		dialog.setResizable(false);
		// dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	public static void WindowLock(boolean lock) {
		Window();

		dialog.setVisible(true);
		// dialog.setAlwaysOnTop(true);

		emergency.setVisible(false);
		// emergency.setVisible(true);

		// frame.setVisible(true);
		// frame.setAlwaysOnTop(true);
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

		// frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// frame.dispatchEvent(new WindowEvent(frame,
		// WindowEvent.WINDOW_CLOSING));
		
//		return true;

	}

	public static void BreakGlass(final String cardnumber, final String username) {

		emergency.setVisible(true);
		dialog.validate();
		// frame.validate();
		emergency.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WindowUnlock();
				SendEmail.Email(cardnumber, username);

			}

		});

		/**
		 * Add action listener to button. If pressed call @WindowUnlock method
		 * and log activity. Send notification to my phone/email.
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
	 * Things to consider: Any card not in the database that is presented to the
	 * reader will display the emergency override button so 2 databases may be
	 * needed. One containing all hospital employee badges and a second
	 * containing badges of employees eligible for clinical access. Thus, only
	 * employee badges that do not have clinical access can override the lock
	 * screen since employees with clinical access will already be granted
	 * access.
	 */
}