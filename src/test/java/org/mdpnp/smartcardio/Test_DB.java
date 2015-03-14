package org.mdpnp.smartcardio;

import java.util.List;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.dto.CardDTO;
import org.mdpnp.smartcardio.rfid.ReadCard;
import org.mdpnp.smartcardio.util.HibernateUtil;

public class Test_DB {

	static ReadCard Read = new ReadCard();
	static CardTerminal terminal = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		EmployeeManager mg = new EmployeeManager();

		CardDTO cardDto = new CardDTO();
		cardDto.setUserName("testuser2");
		cardDto.setCardNumber("000002");
		cardDto.setClinicalAccess("y");

		mg.create(cardDto);

		try {

			// get the list of available terminals
			TerminalFactory factory = TerminalFactory
					.getInstance("PC/SC", null);
			List<CardTerminal> terminalList = factory.terminals().list();

			// take the first terminal in the list
			terminal = (CardTerminal) terminalList.get(0);

			String UID = bytesToHex(Read.Reader(terminal).getData());
			
			List<CardDTO> myList = mg.findByAll();
			// System.out.println(card.getCardNumber());
			for (CardDTO card : myList)
				if (!myList.contains(UID)) {
					System.out.print("Access Denied: ");
				} else {
					System.out.print("Access Granted: ");
				}
		} catch (Exception ex) {
			ex.printStackTrace();
			// System.out.println("Reader Not Present.");
		}

		HibernateUtil.getSessionFactory().close();
		System.out.println("\ndone");

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
