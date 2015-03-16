<<<<<<< HEAD:src.test.java/Test_DB.java
import java.util.Date;
=======
package org.mdpnp.smartcardio;

import java.util.List;
>>>>>>> 088d9e5c9b2adccbc548ec6a7f4464cb21851346:src/test/java/org/mdpnp/smartcardio/Test_DB.java

import javax.smartcardio.CardTerminal;

<<<<<<< HEAD:src.test.java/Test_DB.java
import org.mdpnp.smartcardio.activity.ActivityLogger;
import org.mdpnp.smartcardio.db.ActivityManager;
=======
import org.mdpnp.smartcardio.db.EmployeeManager;
import org.mdpnp.smartcardio.dto.CardDTO;
>>>>>>> 088d9e5c9b2adccbc548ec6a7f4464cb21851346:src/test/java/org/mdpnp/smartcardio/Test_DB.java
import org.mdpnp.smartcardio.rfid.ReadCard;
import org.mdpnp.smartcardio.util.HibernateUtil;

public class Test_DB {

	static ReadCard Read = new ReadCard();
	static CardTerminal terminal = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

<<<<<<< HEAD:src.test.java/Test_DB.java
		/*EmployeeManager manager = new EmployeeManager();

		CardDTO cardDto = new CardDTO();
		 cardDto.setUserName("testuser");
		 cardDto.setCardNumber("000001");

		manager.delete(cardDto);*/
		
		ActivityManager am = new ActivityManager();
		ActivityLogger logger = new ActivityLogger();
		
		logger.setUserName("diego");
		logger.setDate(new Date());
		logger.setEmergencyButton(false);
		logger.setAccess("Granted");
		
		am.create(logger);
		
		
		
/*		try {
=======
		EmployeeManager mg = new EmployeeManager();

		CardDTO cardDto = new CardDTO();
		cardDto.setUserName("testuser2");
		cardDto.setCardNumber("000002");
		cardDto.setClinicalAccess("y");

		mg.create(cardDto);

		try {
>>>>>>> 088d9e5c9b2adccbc548ec6a7f4464cb21851346:src/test/java/org/mdpnp/smartcardio/Test_DB.java

			// get the list of available terminals
			TerminalFactory factory = TerminalFactory
					.getInstance("PC/SC", null);
			List<CardTerminal> terminalList = factory.terminals().list();

			// take the first terminal in the list
			terminal = (CardTerminal) terminalList.get(0);

			String UID = bytesToHex(Read.Reader(terminal).getData());
<<<<<<< HEAD:src.test.java/Test_DB.java

=======
			
>>>>>>> 088d9e5c9b2adccbc548ec6a7f4464cb21851346:src/test/java/org/mdpnp/smartcardio/Test_DB.java
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
*/
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
