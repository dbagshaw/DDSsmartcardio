package org.mdpnp.smartcardio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.util.List;

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

public class Test {

	private static String CardUID;

	private static final Test _instance = new Test();

	public static Test getInstance() {
		return _instance;
	}

	public Test() {

	}

	private String getUID() {
		return CardUID;
	}

	public static void main(String[] args) {

		WindowLock();
		while (true) {
			CardTerminal terminal;
			ResponseAPDU response = null;
			CardChannel channel;
			try {
				// get the list of available terminals
				TerminalFactory factory = TerminalFactory.getInstance("PC/SC",
						null);
				List<CardTerminal> terminalList = factory.terminals().list();

				// System.out.println(terminalList);

				// take the first terminal in the list
				terminal = (CardTerminal) terminalList.get(0);

				System.out.println("Waiting for Card to be present");
				terminal.waitForCardPresent(0);

				// establish a connection with the card
				Card card = terminal.connect("*");
				// System.out.println("Card: " + card);
				channel = card.getBasicChannel();

				// reset the card
				@SuppressWarnings("unused")
				ATR atr = card.getATR();
				// System.out.println("ATR: " + bytesToHex(atr.getBytes()));

				// APDU Command to get UID
				// byte[] commandUID = new byte[] { (byte) 0xFF, (byte) 0xCA,
				// 0x00,
				// 0x00, 0x00 };
				byte[] commandUID = { (byte) 0xFF, (byte) 0xCA, 0x00, 0x00,
						0x00 };

				CommandAPDU command = new CommandAPDU(commandUID);

				response = channel.transmit(command);

				// disconnect
				card.disconnect(false);

				String UID = bytesToHex(response.getData());
				System.out.println(UID);

				if (UID != null) {
					WindowUnlock();
					terminal.waitForCardAbsent(0);
				} else
					System.out.println("Could not unlock");

				terminal.waitForCardPresent(0);
				// String UID2 = bytesToHex(response.getData());
				if (UID != null) {
					WindowLock();
					terminal.waitForCardAbsent(0);
				} else
					System.out.println("Could not lock");

				CardUID = UID;

			} catch (Throwable t) {
				// t.printStackTrace();
			}
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