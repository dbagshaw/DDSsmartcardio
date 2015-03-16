package org.mdpnp.smartcardio.rfid;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class ReadCard {

	private ResponseAPDU Reader(CardTerminal terminal) {
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
			// 		0x00, 0x00 };
			byte[] commandUID = { (byte) 0xFF, (byte) 0xCA, 0x00,
					0x00, 0x00 };

			// This prints the APDU command passed to the reader
			// System.out.println(bytesToHex(commandUID));

			CommandAPDU command = new CommandAPDU(commandUID);
			response = channel.transmit(command);

			// disconnect
			card.disconnect(true);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Card Not Present.");
		}
		return response;
	}
	
	public String getUID() {
		
		String UID = bytesToHex(Reader(null).getData());
		
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
