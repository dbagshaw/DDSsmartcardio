package org.mdpnp.smartcardio.rfid;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;


import org.mdpnp.smartcardio.activity.ActivityLog;
import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.db.JavaFXNotificationPopUp;
import org.mdpnp.smartcardio.db.NotificationPopUp;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.lock.LockScreen;

public class ReadCard {

	static CardTerminal terminal = null;

	public static CardTerminal TerminalSetUp() {

		Thread terminalThread = new Thread(
				() -> {
					// CardTerminal terminal = null;
					String searchNotice = "Searching for Terminals...";
					NotificationPopUp.getInstance().terminalNotification(searchNotice);
					
					for (int i = 0; i <= 100; i++) {
						while (terminal == null) {
							try {

								// get the list of available terminals
								TerminalFactory factory = TerminalFactory
										.getInstance("PC/SC", null);
								List<CardTerminal> terminalList = factory
										.terminals().list();
								terminal = (CardTerminal) terminalList.get(i);

								// System.out.println(terminalList);

							} catch (Exception ex) {
								// ex.printStackTrace();
							}
						}
					}
					if (terminal != null) {
						String connectedNotice = "<html>Connected to Terminal:<br/>"
								+ terminal + "</html>";
						NotificationPopUp.getInstance().terminalNotification(connectedNotice);
						Reader(terminal);
					}

				});
		terminalThread.start();

		return terminal;
	}

	private static ResponseAPDU Reader(CardTerminal terminal) {
		ResponseAPDU response = null;
		CardChannel channel;

		try {

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
			// byte[] commandUID = new byte[] { (byte) 0xFF, (byte) 0xCA, 0x00,
			// 0x00, 0x00 };
			byte[] commandUID = { (byte) 0xFF, (byte) 0xCA, 0x00, 0x00, 0x00 };

			CommandAPDU command = new CommandAPDU(commandUID);

			response = channel.transmit(command);

			// disconnect
			card.disconnect(false);

		} catch (Throwable t) {
			// t.printStackTrace();
			String lostConnNotice = "<html>Lost Connection to Terminal:<br/>"
					+ terminal + "</html>";
			// System.out.println(lostConnNotice);
			NotificationPopUp.getInstance().terminalNotification(lostConnNotice);
			/*
			 * create a program restart method and call it here
			 */
			if (terminal == null)
				TerminalSetUp();
		}

		return response;
	}

	@SuppressWarnings("unused")
	private static void restartApplication() {
		final String javaBin = System.getProperty("java.home") + File.separator
				+ "bin" + File.separator + "java";
		// File currentJar = null;
		// File currentJar =
		// "C:/Users/bagshawd/Documents/mdpnp/DDSsmartcardio/target/DDSsmartcardio-1.0-SNAPSHOT-shaded.jar";

		File currentJar = new File(
				"C:/Users/bagshawd/Documents/mdpnp/DDSsmartcardio/target/DDSsmartcardio-1.0-SNAPSHOT.jar");

		// try {
		// currentJar = new File(SmartCardReader.class.getProtectionDomain()
		// .getCodeSource().getLocation().toURI());
		// } catch (URISyntaxException e1) {
		// e1.printStackTrace();
		// }

		// System.out.println(currentJar);

		/* is it a jar file? */
		if (!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add("java -cp C:/Users/bagshawd/Documents/mdpnp/DDSsmartcardio/target/DDSsmartcardio-1.0-SNAPSHOT.jar org.mdpnp.smartcardio.rfid.SmartCardReader");
		// command.add("java");
		// command.add("-cp");
		// command.add(currentJar.getPath() +
		// "org.mdpnp.smartcardio.rfid.SmartCardReader");

		// System.out.println(command);

		final ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread thread = new Thread(() -> {
			try {
				Thread.sleep(3000);
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		thread.start();

	}

	public static void Master(CardTerminal terminal, String UID) {
		try {
			terminal.waitForCardAbsent(0);
			/**
			 * If the user wants to use the master tag to access the device then
			 * tap again to be granted access
			 */
			NotificationPopUp.getInstance()
					.terminalNotification("Present New Badge or Tap Again for Access");
			// System.out.println("Present New Badge or Tap Again for Access");

			AddCard.addCard(terminal);
		} catch (CardException e) {
			e.printStackTrace();
		}

	}

	public static void reLock(boolean lock, boolean masterTag/*
															 * , CardTerminal
															 * terminal
															 */) {
		EmployeeManager eManager = new EmployeeManager();
		String UID = getUID();

		CardDTO myList = eManager.findByUID(UID);

		if (masterTag == true || myList != null)
			LockScreen.WindowLock(lock);

		ActivityLog Log = new ActivityLog();
		Log.windowLockLog(UID);

	}

	public static String getUID() {
		String UID = bytesToHex(Reader(terminal).getData());
		return UID;
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
