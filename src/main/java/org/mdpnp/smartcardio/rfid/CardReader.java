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

public class CardReader {

	private static ResponseAPDU response = null;
	private static CardChannel channel;
	private static CardTerminal terminal = null;

	// private String UID;

	private static ResponseAPDU Reader() {

		try {
			// get the list of available terminals
			TerminalFactory factory = TerminalFactory
					.getInstance("PC/SC", null);
			List<CardTerminal> terminalList = factory.terminals().list();

			// System.out.println(terminalList);

			// take the first terminal in the list
			terminal = (CardTerminal) terminalList.get(0);

			// String connectedNotice = "<html>Connected to Terminal:<br/>"
			// + terminal + "</html>";
			//
			// NotificationPopUp.getInstance().terminalNotification(
			// connectedNotice);

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

		} catch (Exception ex) {
			ex.printStackTrace();
			// System.out.println("No Terminals Found");
		}

		return response;
	}

	public static String getUID() {
		String UID = bytesToHex(Reader().getData());
		return UID;
	}

	public static void Master(String UID) {

		try {
			terminal.waitForCardAbsent(0);
			/**
			 * If the user wants to use the master tag to access the device then
			 * tap again to be granted access
			 */
			NotificationPopUp.getInstance().terminalNotification(
					"Present New Badge or Tap Again for Access");
			// System.out.println("Present New Badge or Tap Again for Access");

			// AddCard.addCard(terminal);
		} catch (CardException e) {
			e.printStackTrace();
		}
	}

	public static void Lock(boolean masterTag) {
		// Boolean lock;
		// while (true) {
		// lock = LockScreen.isLocked();
		try {
			// terminal.waitForCardPresent(0);
			// Makes UID a string variable
			String UID = getUID();
			// terminal.waitForCardAbsent(0);
			// masterTag = BCrypt.checkpw(UID, getMasterTag());

			EmployeeManager eManager = new EmployeeManager();
			CardDTO myList = eManager.findByUID(UID);

			String username = myList.getUserName();
			NotificationPopUp.getInstance().accessNotification("Good Bye: ",
					myList.getCardNumber());
			System.out.print("Good Bye: " + username + ". ");

			// if (!lock) {
			if (masterTag == true || myList != null) {
				LockScreen.WindowLock();
				terminal.waitForCardAbsent(0);
			}
			terminal.waitForCardPresent(0);
			
			ActivityLog Log = new ActivityLog();
			Log.windowLockLog(UID);

		} catch (Throwable t) {
			// t.printStackTrace();
			System.out.println("Bad Read. Try Again.");
		}
		// }
	}

	public static void Unlock(boolean masterTag) {

		try {

			// terminal.waitForCardPresent(0);

			String UID = getUID();
			EmployeeManager eManager = new EmployeeManager();
			CardDTO myList = eManager.findByUID(UID);

			if (masterTag == true || myList != null) {
				LockScreen.WindowUnlock();
				terminal.waitForCardAbsent(0);
			} else
				return;
			terminal.waitForCardPresent(0);
			
			ActivityLog Log = new ActivityLog();
			Log.windowLockLog(UID);

		} catch (CardException e) {
			e.printStackTrace();
		}

		
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
