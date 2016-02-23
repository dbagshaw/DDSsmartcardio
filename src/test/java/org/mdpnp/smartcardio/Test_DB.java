package org.mdpnp.smartcardio;

import java.util.List;

import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class Test_DB {

	public static void main(String[] args) {

		CardTerminal terminal;
		ResponseAPDU response = null;
		CardChannel channel;

		try {
			// get the list of available terminals
			TerminalFactory factory = TerminalFactory
					.getInstance("PC/SC", null);
			List<CardTerminal> terminalList = factory.terminals().list();

			// System.out.println(terminalList);

			// take the first terminal in the list
			terminal = (CardTerminal) terminalList.get(0);

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
			
			String UID = bytesToHex(response.getData());
			System.out.println(UID);

		} catch (Throwable t) {
			// t.printStackTrace();
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