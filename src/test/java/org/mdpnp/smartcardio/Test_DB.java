package org.mdpnp.smartcardio;

import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

import org.mdpnp.smartcardio.db.NotificationPopUp;
import org.mdpnp.smartcardio.rfid.ReadCard;

public class Test_DB {

	static CardTerminal terminal = null;
	static boolean run = true;

	public static void main(String[] args) {

		TerminalSetUp();

		while (run) {

			// Makes UID a string variable
			String UID = null;
			if (terminal != null) {
				UID = ReadCard.getUID();
			}
		}

	}

	public static CardTerminal TerminalSetUp() {

		Thread terminalThread = new Thread(() -> {
			String searchNotice = "Searching for Terminals...";
			NotificationPopUp.getInstance().terminalNotification(searchNotice);

			for (int i = 0; i <= 100; i++) {
				while (terminal == null) {
					try {

						// get the list of available terminals
				TerminalFactory factory = TerminalFactory.getInstance("PC/SC",
						null);
				List<CardTerminal> terminalList = factory.terminals().list();

				terminal = (CardTerminal) terminalList.get(i);

				System.out.println(terminalList);
			} catch (Exception ex) {
				// ex.printStackTrace();
				clearPCSC();
			}
		}
	}
	if (terminal != null) {
		String connectedNotice = "<html>Connected to Terminal:<br/>" + terminal
				+ "</html>";
		NotificationPopUp.getInstance().terminalNotification(connectedNotice);
		Reader(terminal);
	}

}		);
		terminalThread.start();

		return terminal;
	}

	private static final ResponseAPDU Reader(CardTerminal terminal) {
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
			String lostConnNotice = "<html>Lost Connection to Terminal:<br/>"
					+ terminal + "</html>";
			NotificationPopUp.getInstance().terminalNotification(lostConnNotice);

			// clearPCSC();
			terminal = null;
			if (terminal == null)
				TerminalSetUp();
		}

		return response;
	}

	private static void clearPCSC() {
		try {
			// get the list of available terminals
			TerminalFactory factory = TerminalFactory
					.getInstance("PC/SC", null);
			List<CardTerminal> terminalList = factory.terminals().list();

			terminal = (CardTerminal) terminalList.get(0);
			System.out.println(terminalList);
			// terminalList.clear();

		} catch (Exception ex) {
			// ex.printStackTrace();
		}

	}

	public static final String getUID() {
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
