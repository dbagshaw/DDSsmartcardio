package org.mdpnp.smartcardio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Scanner;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Test_wo_rfid {

	private static String CardUID;

	private static final Test _instance = new Test();

	public static Test getInstance() {
		return _instance;
	}

	public Test_wo_rfid() {

	}

	private String getUID() {
		return CardUID;
	}

	public static void main(String[] args) {

		WindowLock();
		while (true) {

			Scanner userinput1 = new Scanner(System.in);
			
			/**
			 * Press ALT + TAB to focus on the Eclipse console
			 */
			
			System.out.println("Enter your username:");
			String username;
			username = userinput1.next();
			
			System.out.println("Enter your password:");
			String password;
			password = userinput1.next();

			if (username != null && password != null) {
				WindowUnlock();
			} else
				System.out.println("Could not unlock");
			
			System.out.println("Enter your username:");
			username = userinput1.next();
			System.out.println("Enter your password:");
			password = userinput1.next();

			if (username != null && password != null) {
				WindowLock();
			} else
				System.out.println("Could not lock");

		}
	}

	static JDialog dialog = new JDialog();

	static private boolean locked; // the screen is locked

	static String buttonInfo1 = "This button will override the lock screen for access to the system.";
	String buttonInfo2 = "By Using This Button, You Are Unlocking The System In An Emergency "
			+ "And Your Credentials Will Be Logged Into A Database For Future Investigation. The "
			+ "Supervisior Will Be Notified Immediately.";

	public static boolean isLocked() {
		return locked;
	}

	private static JPanel textPanel() {
		JPanel textPanel = new JPanel(new BorderLayout());
		// textPanel.setBackground(Color.BLUE);

		JLabel text = new JLabel();
		text.setVerticalAlignment(JLabel.CENTER);
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setText("SCAN BADGE FOR ACCESS");
		text.setFont(new Font("sansserif", Font.BOLD, 80));
		text.setForeground(Color.RED);

		textPanel.add(text, BorderLayout.CENTER);

		return textPanel;
	}

	public static void WindowLock() {
		dialog.setLayout(new BorderLayout());
		dialog.setTitle("MD PnP ICE Supervisor");
		dialog.setUndecorated(true);
		dialog.setOpacity(0.7f);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setSize(screenSize);
		// dialog.setSize(600, 800);

		dialog.add(textPanel(), BorderLayout.CENTER);

		dialog.setResizable(false);
		// dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		// dialog.setModalityType(ModalityType.TOOLKIT_MODAL);
		dialog.setVisible(true);

		locked = true;// window is locked
		System.out.println("Window was locked");

	}

	public static void WindowUnlock() {

		/**
		 * DISPOSE_ON_CLOSE closes the JDialog and allows the program to
		 * continue to run. Alternatively, EXIT_ON_CLOSE closes the JDialog and
		 * exits the program.
		 */

		locked = false; // window is unlocked
		System.out.println("Window was Unlocked");

		// dialog.setUndecorated(false);
		// dialog.setVisible(false);

		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));

	}

	private static final char wordToHexChar(byte b) {
		if (b < 10) {
			return (char) ('0' + b);
		} else {
			return (char) ('A' + (b - 10));
		}
	}

	private static final String bytesToHex(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			sb.append(wordToHexChar((byte) (0x0F & (b[i] >> 4))));
			sb.append(wordToHexChar((byte) (0x0F & b[i])));
			sb.append(' ');
			if (0 == ((i + 1) % 10)) {
				sb.append("\n");
			}
		}
		return sb.toString();
	}
}