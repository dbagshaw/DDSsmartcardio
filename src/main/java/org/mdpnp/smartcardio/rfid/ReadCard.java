package org.mdpnp.smartcardio.rfid;

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
import org.mdpnp.smartcardio.db.NotificationPopUp;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.lock.LockScreen;

public class ReadCard {

	public static CardTerminal TerminalSetUp() {
		CardTerminal terminal = null;
		String searchNotice = "Searching for Terminals...";
		NotificationPopUp.TerminalNotification(searchNotice);

		while (terminal == null) {

			try {
				// get the list of available terminals
				TerminalFactory factory = TerminalFactory.getInstance("PC/SC",
						null);
				List<CardTerminal> terminalList = factory.terminals().list();

				// System.out.println(terminalList);

				// take the first terminal in the list
				terminal = (CardTerminal) terminalList.get(0);

			} catch (Exception ex) {
				// ex.printStackTrace();
				// System.out.println("No Terminals Found");
			}
		}

		String connectedNotice = "<html>Connected to Terminal:<br/>" + terminal + "</html>";
		// System.out.println(connectedNotice);
		NotificationPopUp.TerminalNotification(connectedNotice);

		// Reader();

		return terminal;

	}

	private static ResponseAPDU Reader() {
		ResponseAPDU response = null;
		CardChannel channel;
		CardTerminal terminal = TerminalSetUp();

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
			String lostConnNotice = "<html>Lost Connection to Terminal:<br/>" + terminal + "</html>";
			// System.out.println(lostConnNotice);
			NotificationPopUp.TerminalNotification(lostConnNotice);
			// TerminalSetUp();
			
			/*
			 * create a program restart method and call it here
			 */

		}

		return response;
	}

	public static void Master(CardTerminal terminal, String UID) {
		try {
			terminal.waitForCardAbsent(0);
			/**
			 * If the user wants to use the master tag to access the device then
			 * tap again to be granted access
			 */
			NotificationPopUp
					.TerminalNotification("Present New Badge or Tap Again for Access");
			// System.out.println("Present New Badge or Tap Again for Access");

			AddCard.addCard(terminal);
		} catch (CardException e) {
			e.printStackTrace();
		}

	}

	public static void reLock(boolean lock, boolean masterTag,
			CardTerminal terminal) {
		EmployeeManager eManager = new EmployeeManager();
		String UID = getUID();

		CardDTO myList = eManager.findByUID(UID);

		if (masterTag == true || myList != null)
			LockScreen.WindowLock(lock);

		ActivityLog Log = new ActivityLog();
		Log.windowLockLog(UID);

	}

	public static String getUID() {
		String UID = bytesToHex(Reader().getData());

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
